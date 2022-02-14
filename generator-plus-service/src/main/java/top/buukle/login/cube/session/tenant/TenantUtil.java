package top.buukle.login.cube.session.tenant;

import lombok.extern.slf4j.Slf4j;
import top.buukle.login.cube.session.OperatorUserDTO;
import top.buukle.login.cube.session.SessionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TenantUtil {


	/** in条件前缀 */
	private static final String CODE_IN_PREFIX = " in ( ";

	private static final String CODE_EQUAL = " = ";

	/** in条件后缀 */
	private static final String CODE_IN_SUFFIX = " )";

	/** CONDITION条件前缀 */
	private static final String CONDITION_PREFIX = " ( ";

	/** CONDITION条件后缀 */
	private static final String CONDITION_SUFFIX = " ) ";

	/** 拼接片段常量 : where */
	private static final String SPLICING_WHERE = " where ";

	/** 拼接片段常量 : and */
	private static final String SPLICING_AND = " and ";

	/** 拼接片段常量 : or */
	private static final String SPLICING_OR = " or ";

	/** 拼接片段常量 : group */
	private static final String SPLICING_GROUP = " group ";

	/** 拼接片段常量 : order */
	private static final String SPLICING_ORDER = " order ";

	public static boolean containsSqlInjection(Object obj){
		Pattern pattern= Pattern.compile("\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)");
		Matcher matcher=pattern.matcher(obj.toString().toLowerCase());
		return matcher.find();
	}
	/**
	 * 构建查询条件,调用解析方法
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static String handleSql(String sql) throws Exception {
		// 获取当前用户
		OperatorUserDTO operator = SessionUtils.getOperator();
		// 获取当前租户信息
		TenantHelperDTO tenantHelperDTO = TenantHelper.get();
		TenantHelper.closeTenant();
		// 非登录状态或admin或没有开启租户
		if(!operator.isOnline() || operator.admin() || null == tenantHelperDTO){
			return sql;
		}
		// 处理 :回车,制表符,等
		sql = sql.replace(TenantStringUtil.NEW_LINE, TenantStringUtil.BLANK).replace(TenantStringUtil.TABULACTOR, TenantStringUtil.BLANK);
		String tableName = tenantHelperDTO.getTableName();
		String tenantFieldName = TenantStringUtil.isEmpty(tenantHelperDTO.getTenantFieldName()) ? "tenant_id" : tenantHelperDTO.getTenantFieldName();
		log.info("解析sql,拼接开始:原始sql :{},表名 :{},维度字段名 :{},",sql, tableName, tenantFieldName);
		String codeInCondition;
		// 没有下辖租户
		if(operator.getSubTenantIds() == null || "".equals(operator.getSubTenantIds())){
			codeInCondition = new StringBuilder("creator_code").append(CODE_EQUAL).append("'").append(operator.getUserId()).append("'").toString();
		}
		// 有下辖租户
		else {
			codeInCondition = new StringBuilder(CONDITION_PREFIX).append(tenantFieldName).append(CODE_IN_PREFIX).append(operator.getSubTenantIds()).append(CODE_IN_SUFFIX)
					.append(SPLICING_OR).append("creator_code").append(CODE_EQUAL).append("'").append(operator.getUserId()).append("'").append(CONDITION_SUFFIX).toString();
		}
		String finalSql = handleSql(sql, tableName, codeInCondition);
		log.info("解析sql,拼接结束 :finalSql:{}",finalSql);
		return finalSql;
	}

	/**
	 * 解析sql,调用拼接方法
	 * @param sql
	 * @param tableName
	 * @param codeInCondition
	 * @return
	 */
	public static String handleSql(String sql, String tableName, String codeInCondition) {
		Matcher matcher;
		if (sql.startsWith("select")) {
			matcher = Pattern.compile("select\\s.+from\\s" + tableName + "\\s(.*)where\\s(.*)").matcher(sql);
			if (matcher.find()) {
				String trim = matcher.group(1).trim();
				// 多表联查
				if(trim.startsWith(TenantStringUtil.COMMA)){
					return match(TenantStringUtil.EMPTY,sql,tableName + TenantStringUtil.DOT + codeInCondition);
				}
				// 尝试处理单表别名
				else{
					return match(trim,sql,codeInCondition);
				}
			} else {
				matcher = Pattern.compile("select\\s.+from\\s(.*)" + tableName + "\\s(.*)where\\s(.*)").matcher(sql);
				if (matcher.find()) {
					if (TenantStringUtil.isEmpty(matcher.group(2).trim())) {
						return splicingSql(sql, codeInCondition);
					} else {
						int index = matcher.group(2).trim().indexOf(TenantStringUtil.BLANK);
						if (index > 0) {
							return splicingSql(sql, matcher.group(2).trim().substring(0, index) + TenantStringUtil.DOT + codeInCondition);
						} else {
							return splicingSql(sql, matcher.group(2).trim() + TenantStringUtil.DOT + codeInCondition);
						}

					}
				} else {
					matcher = Pattern.compile("select\\s.+from\\s" + tableName + "\\s(.*)").matcher(sql);
					if (matcher.find()) {
                        return match(matcher.group(1).trim(),sql,codeInCondition);
					} else {
						matcher = Pattern.compile("select\\s.+from\\s(.*)" + tableName + "\\s(.*)").matcher(sql);
						if (matcher.find()) {
                            return match(matcher.group(2).trim(),sql,codeInCondition);
						}

					}
				}
			}
		}
		return sql;
	}

    private static String match(String match, String sql, String codeInCondition) {
        if (TenantStringUtil.isEmpty(match)) {
            return splicingSql(sql, codeInCondition);
        } else {
            if (match.indexOf(TenantStringUtil.BLANK) > 0) {
                match = match.substring(0, match.indexOf(TenantStringUtil.BLANK));
                return splicingSql(sql, match + TenantStringUtil.DOT + codeInCondition);
            } else {
                return splicingSql(sql, match + TenantStringUtil.DOT + codeInCondition);
            }
        }
    }

    /**
	 * 拼接
	 * @param sql
	 * @param tableNameAndCondition
	 * @return
	 */
	public static String splicingSql(String sql, String tableNameAndCondition) {
		int index = sql.indexOf(SPLICING_WHERE);
		//处理 where
		if (index > 0) {
			return sql.replace(SPLICING_WHERE, SPLICING_WHERE + tableNameAndCondition + SPLICING_AND);
		} else {
			//处理 group by
			index = sql.indexOf(SPLICING_GROUP);
			if (index > 0) {
				return sql.replace(SPLICING_GROUP, SPLICING_WHERE + tableNameAndCondition + SPLICING_GROUP);
			}
			//处理 order by
			index = sql.indexOf(SPLICING_ORDER);
			if (index > 0) {
				return sql.replace(SPLICING_ORDER, SPLICING_WHERE + tableNameAndCondition + SPLICING_ORDER);
			}
			//处理 默认情况
			else {
				return sql + SPLICING_WHERE + tableNameAndCondition;
			}
		}
	}

}
