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
    public static String ACCEESS_TOKEN = "access-token";
    public static String REFRESH_TOKEN = "refresh-token";


    //Authentication Response
    public static final String  VALID_TOKEN ="Valid token";
    public static final String  INVALID_TOKEN ="Invalid token";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    public static final String  INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static final String WRONG_PASSWORD = "Weak Password: Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\"";
    public static final String SUCCESS_VERIFICATION ="Verification succeeded";
    public static final String FAILED_VERIFICATION ="Verification failed";
    public static final String SUCCESS_VALIDATION ="Validation successful";

    public static final String REGISTRATION_CANDIDATE_MESSAGE = "Congratulations! You have successfully registered as a candidate.\n"
            + "To activate your account, please check your email inbox and verify your email address.\n"
            + "Once verified, you will be able to access all features of our platform";

    public static final String REGISTRATION_EMPLOYER_MESSAGE = "Congratulations! You have successfully registered as an Employer.\n"
            + "To activate your account, please check your email inbox and verify your email address.\n"
            + "Once verified, you will be able to access all features of our platform";




}
