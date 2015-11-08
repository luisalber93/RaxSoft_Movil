package com.maven.raxsoft.models;

/**
 * Created by Luisarriaga on 05/11/2015.
 */
public class Usuario {

    private String user;
    private String passwd;
    private String roleAcceso;
    private String mac;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRoleAcceso() {
        return roleAcceso;
    }

    public void setRoleAcceso(String roleAcceso) {
        this.roleAcceso = roleAcceso;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
