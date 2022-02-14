package top.buukle.login.cube.session.tenant;

public class TenantHelper {

    private static final ThreadLocal<TenantHelperDTO> LOCAL_TENANT_DTO = new ThreadLocal<>();

    public static void startTenant(String tableName) {
        TenantHelperDTO tenantHelperDTO = new TenantHelperDTO();
        tenantHelperDTO.setTableName(tableName);
        LOCAL_TENANT_DTO.set(tenantHelperDTO);
    }

    public static TenantHelperDTO get() {
        return LOCAL_TENANT_DTO.get();
    }

    public static void closeTenant() {
        LOCAL_TENANT_DTO.remove();
    }
}
