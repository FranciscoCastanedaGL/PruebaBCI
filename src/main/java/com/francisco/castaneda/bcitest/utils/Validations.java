package com.francisco.castaneda.bcitest.utils;

import com.francisco.castaneda.bcitest.model.constants.CustomConstants;

public class Validations {

    public static boolean paswordValidation (String pass) {
        return pass.matches(CustomConstants.REG_EXP_FOR_PASSWORD);
    }

    public static boolean emailValidation (String email){
        return  email.matches(CustomConstants.REG_EXP_FOR_EMAIL);
    }
}
