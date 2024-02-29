package com.devsling.fr.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String TOKEN = "token";
    public static final String BEARER = "Bearer ";
    public static final int AUTH_HEADER_LENGTH = 7;
    public static final String VALID_TOKEN ="Valid token";
    public static final String  INVALID_TOKEN ="Invalid token";

    public static final String NOT_AUTHORIZED = "Unauthorized : Invalid or expired token";
    public static final String ERROR ="Error calling auth service";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    public static final String MISSING_AUTHORIZATION_HEADER ="Missing authorization Header in the request";


    //Service Name
    public static final String AUTH_SERVICE_NAME = "Authentication Service";
    public static final String CANDIDATE_SERVICE_NAME = "Candidate Service";
    public static final String EMPLOYER_SERVICE_NAME = "Employer Service";
    public static final String AUTH_SERVICE_TOKEN_VALIDATION = "Validation token service";




}
