package top.buukle.login.cube.session;

import lombok.Data;
import top.buukle.login.cube.session.tenant.TenantCommonEnums;

@Data
public class UserInfo {
    private String username;
    private String expire = "604800";
    private String userId;
    private String mainTenant;
    private String subTenantIds;
    private boolean online;

    public boolean admin() {
        return TenantCommonEnums.fixedValues.SUPER_ADMIN_VALUE.value().equals(this.getMainTenant());
    }
}
