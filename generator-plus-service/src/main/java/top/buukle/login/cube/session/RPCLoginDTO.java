package top.buukle.login.cube.session;

import lombok.Data;

import java.io.Serializable;

@Data
public class RPCLoginDTO implements Serializable {

    private String username;

    private String password;

}
