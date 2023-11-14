package com.francisco.castaneda.bcitest.utils;

import com.francisco.castaneda.bcitest.model.constants.CustomConstants;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;


public class JasyptUtil {


    private JasyptUtil() {
    }

    public static String encyptPwd(String password, String value){
            PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
            encryptor.setConfig(cryptor(password));
        return encryptor.encrypt(value);
        }


        public static String decyptPwd(String password,String value){
            PooledPBEStringEncryptor encryptor;
            encryptor = new PooledPBEStringEncryptor();
            encryptor.setConfig(cryptor(password));
            encryptor.decrypt(value);
            return encryptor.decrypt(value);
        }

        public static SimpleStringPBEConfig cryptor(String password){
            SimpleStringPBEConfig config = new SimpleStringPBEConfig();
            config.setPassword(password);
            config.setAlgorithm(CustomConstants.PBEWITHHMACSHA_512_ANDAES_256);
            config.setKeyObtentionIterations("1000");
            config.setPoolSize(1);
            config.setProviderName(CustomConstants.PROVIDER_NAME);
            config.setSaltGeneratorClassName(CustomConstants.JASYPT_SALT_RANDOM_SALT_GENERATOR);
            config.setIvGeneratorClassName(CustomConstants.RANDOM_IV_GENERATOR);
            config.setStringOutputType(CustomConstants.STRING_OUTPUT_TYPE);
            return config;
        }
}
