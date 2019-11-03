package com.czk.forum.dto;

/**
 * created by srdczk 2019/11/3
 */
public class LoginDTO {

    private String username;

    private String password;

    private String verifycode;

    private String rember;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", verifycode='" + verifycode + '\'' +
                ", rember=" + rember +
                '}';
    }

    public String getRember() {
        return rember;
    }

    public void setRember(String rember) {
        this.rember = rember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

}
