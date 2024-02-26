package com.devsling.fr.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String TOKEN = "token";
    public static final String BEARER = "Bearer ";
    public static final int AUTH_HEADER_LENGTH = 7;
    public static final String VALID_TOKEN ="Valid token";
    public static final String NOT_AUTHORIZED ="Not Authorized";
    public static final String ERROR ="Error calling auth service";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";




}
