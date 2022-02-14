package top.buukle.login.cube.session.tenant;


/**
 * @Author: elvin
 * @Date: 2019/7/28/028 3:56
 */
public class TenantCommonEnums {


    public enum fixedValues {

        DEFAULT_TENANT_VALUE(-1,"默认主租户"),
        SUPER_ADMIN_VALUE(1,"超管主租户"),
        SUPER_ADMIN_PID_VALUE(0,"超管租户pid"),
        ;

        private Integer status;
        private String description;

        fixedValues(int status, String description) {
            this.description = description;
            this.status = status;
        }
        public String getDescription() {
            return description;
        }
        public Integer value() {
            return status;
        }

        public static String getPairs() {
            fixedValues[] values = fixedValues.values();
            StringBuffer stringBuffer = new StringBuffer();
            for (fixedValues status : values) {
                stringBuffer.append(status.value());
                stringBuffer.append(";");
                stringBuffer.append(status.getDescription());
                stringBuffer.append(",");
            }
            return stringBuffer.toString();
        }
    }
}
