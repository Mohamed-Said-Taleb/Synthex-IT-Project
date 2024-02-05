package com.devsling.fr.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String AUTHORISATION = "auth";
    public static final int MINUTES = 10;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String EMAIL_SENDER = "mohamed.s.taleb@devsling.com";
    public static final String EMAIL_PERSONAL = "Coding SynthexIT Support";

    public static String Secret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public static String Prefixe = "Bearer ";

    //Authentication Response
    public static final String  VALID_TOKEN ="Valid token";
    public static final String  INVALID_TOKEN ="Invalid token";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    public static final String  INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static final String WRONG_PASSWORD = "Weak Password: Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\"";



}
