package top.buukle.login.cube.session.tenant;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;


/**
 * 租户隔离插件
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Slf4j
public class TenantInterceptor implements Interceptor {


	/** queryArgs[] 对象索引 : 原始 mappedStatement 请求语句对象索引*/
	static int MAPPED_STATEMENT_INDEX = 0;

	/** queryArgs[] 对象索引 : parameter 参数管理对象索引*/
	static int PARAMETER_INDEX = 1;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		if (invocation.getTarget() instanceof Executor) {
        	//初始化准备参数对象
        	//拦截参数数组
        	final Object[] queryArgs = invocation.getArgs();
        	//请求语句(sql)管理对象
        	final MappedStatement mappedStatement = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
        	//请求参数对象
        	final Object param = queryArgs[PARAMETER_INDEX];
        	//绑定sql信息管理对象
        	final BoundSql boundSql = mappedStatement.getBoundSql(queryArgs[PARAMETER_INDEX]);
        	// 獲取原始sql
        	String originalSql = getOriginalSQL(boundSql);
			// sql为空,不做处理
			if(StringUtils.isBlank(originalSql)){ return invocation.proceed(); }

			this.resetSQL(originalSql,mappedStatement,boundSql,queryArgs);
			// 放行
			return invocation.proceed();
		}
		return invocation.proceed();
	}

	/**
	 * 获取原始sql
	 * @param boundSql
	 * @return
	 */
	private String getOriginalSQL(BoundSql boundSql) {
		//统一转成小写处理
		return boundSql.getSql().toLowerCase();
	}

	/**
	 * 重置sql
	 * @param originalSql
	 * @param mappedStatement
	 * @param boundSql
	 * @param queryArgs
	 */
	private void resetSQL( String originalSql, MappedStatement mappedStatement, BoundSql boundSql, Object[] queryArgs) throws Exception {

		String resultSql = TenantUtil.handleSql(originalSql);
		// 重新new一个查询语句对像
		BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), resultSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		// copy设置参数(这很重要!)
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
		// 把新的查询放到statement里
		MappedStatement newMs = copyFromMappedStatement(mappedStatement,new BoundSqlSqlSource(newBoundSql));
		// 把新的MappedStatement放到queryArgs里
		queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
	}

	/**
	 * 拷贝 MappedStatement 对象
	 * @param mappedStatement
	 * @param newSqlSource
	 * @return
	 */
	private MappedStatement copyFromMappedStatement(MappedStatement mappedStatement, BoundSqlSqlSource newSqlSource) {

		MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), newSqlSource, mappedStatement.getSqlCommandType());
		builder.resource(mappedStatement.getResource());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.statementType(mappedStatement.getStatementType());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		if (mappedStatement.getKeyProperties() != null && mappedStatement.getKeyProperties().length > 0) {
		builder.keyProperty(mappedStatement.getKeyProperties()[0]);
		}
		builder.timeout(mappedStatement.getTimeout());
		builder.parameterMap(mappedStatement.getParameterMap());
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.resultSetType(mappedStatement.getResultSetType());
		builder.cache(mappedStatement.getCache());
		builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
		builder.useCache(mappedStatement.isUseCache());

		return builder.build();
	}

	@Override
	public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

	/**
	 * BoundSqlSqlSource 内部类
	 * @author elvin
	 *
	 */
	public static class BoundSqlSqlSource implements SqlSource {
		private BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
			}
		}

	public static String dealLikeSql(String param){
		param=param.replace("%","\\%");
		param=param.replace("_","\\_");
		param=param.trim();
		return param;
	}
}
