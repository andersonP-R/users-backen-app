package com.anderson.users.auth;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJWTConfig {
    public static final SecretKey SECRET = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTH = "Authorization";

}
