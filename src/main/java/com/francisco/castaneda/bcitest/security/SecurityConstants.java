package com.francisco.castaneda.bcitest.security;

public abstract class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users/sign-up";
    public static final String[] WHITE_LIST = { "/webjars/**","/h2console/**/**" };
}
