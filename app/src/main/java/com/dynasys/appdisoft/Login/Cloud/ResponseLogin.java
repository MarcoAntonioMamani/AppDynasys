package com.dynasys.appdisoft.Login.Cloud;

public class ResponseLogin {
   int code;
   String message;
   String token;
   int id;

    public ResponseLogin(int code, String message, String token, int id) {
        this.code = code;
        this.message = message;
        this.token = token;
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
