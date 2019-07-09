package com.dynasys.appdisoft.Login.Cloud;

public class Bodylogin {
    String mail;
    String password_cli;

    public Bodylogin(String mail, String password_cli) {
        this.mail = mail;
        this.password_cli = password_cli;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword_cli() {
        return password_cli;
    }

    public void setPassword_cli(String password_cli) {
        this.password_cli = password_cli;
    }
}
