package com.sb.main.DTO;

public class JwtTokenResponse
{
    private String token;

    public JwtTokenResponse(String token)
    {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
