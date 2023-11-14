package com.francisco.castaneda.bcitest.model.constants;

public class CustomConstants {
    public static final String REG_EXP_FOR_EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z]+\\.[a-zA-Z]+$";
    public static final String REG_EXP_FOR_PASSWORD = "(?=.*[0-9]{2})(?=.*[a-z])(?=.*[A-Z].{1}).{8,12}$";
    public static final String SEED_ENCRYPTION = "SemillaSuperSegura1234";
    public static final String PBEWITHHMACSHA_512_ANDAES_256 = "PBEWITHHMACSHA512ANDAES_256";
    public static final String JASYPT_SALT_RANDOM_SALT_GENERATOR = "org.jasypt.salt.RandomSaltGenerator";
    public static final String RANDOM_IV_GENERATOR = "org.jasypt.iv.RandomIvGenerator";
    public static final String STRING_OUTPUT_TYPE = "base64";
    public static final String PROVIDER_NAME = "SunJCE";

}
