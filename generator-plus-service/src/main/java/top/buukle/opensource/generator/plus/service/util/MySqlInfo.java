package top.buukle.opensource.generator.plus.service.util;

public class MySqlInfo {
    private String jdbcUrl;
    private String user;
    private String password;
    private String exportPath;
 
    public String getExportPath() {
        return exportPath;
    }
    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }
 
    public MySqlInfo(String jdbcUrl,  String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }
 
    public String getJdbcUrl() {
        return jdbcUrl;
    }
 
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
 
    public String getUser() {
        return user;
    }
 
    public void setUser(String user) {
        this.user = user;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
}