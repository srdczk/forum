package com.czk.forum.dto;

/**
 * created by srdczk 2019/11/4
 */
public class PasswordDTO {

    private String opassword;

    private String npassword;

    private String cpassword;

    public String getNpassword() {
        return npassword;
    }

    public void setNpassword(String npassword) {
        this.npassword = npassword;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getOpassword() {

        return opassword;
    }

    public void setOpassword(String opassword) {
        this.opassword = opassword;
    }

    @Override
    public String toString() {
        return "PasswordDTO{" +
                "opassword='" + opassword + '\'' +
                ", npassword='" + npassword + '\'' +
                ", cpassword='" + cpassword + '\'' +
                '}';
    }
}
