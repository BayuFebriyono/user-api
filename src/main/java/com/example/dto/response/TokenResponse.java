package com.example.dto.response;

public class TokenResponse {
    private String access_type;
    private String token;

    public TokenResponse(String access_type, String token) {
        this.access_type = access_type;
        this.token = token;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
