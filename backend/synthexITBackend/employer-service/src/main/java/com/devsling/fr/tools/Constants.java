package com.devsling.fr.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    public static final String NO_EMPLOYER_FOUND = "No employer found in the data base";
    public static final String NO_EMPLOYER_FOUND_WITH_ID_ = "No employer found with id ";
    public static final String NO_EMPLOYER_WITH_EMAIL_FOUND = "No employer found with this email ";
    public static final String EMPLOYER_SERVICE_NAME = "Employer Service";
    public static final String SAVE_EMPLOYER_ERROR_MESSAGE = "Error saving employer";
    public static final String UPDATE_EMPLOYER_ERROR_MESSAGE = "Error updating employer";
    public static final String DELETE_EMPLOYER_ERROR_MESSAGE = "Error deleting employer";
}
