package com.devsling.fr.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_SESSION_ID = "X-Session-ID";
    public static final String NO_CANDIDATE_FOUND = "No candidates found in the data base";
    public static final String NO_CANDIDATE_FOUND_WITH_ID_ = "No candidates found with id ";
    public static final String NO_CANDIDATE_WITH_EMAIL_FOUND = "No candidates found with this email ";
    public static final String CANDIDATE_SERVICE_NAME = "Candidate Service";
    public static final String SAVE_CANDIDATE_ERROR_MESSAGE = "Error saving candidate";
    public static final String UPDATE_CANDIDATE_ERROR_MESSAGE = "Error updating candidate";
    public static final String DELETE_CANDIDATE_ERROR_MESSAGE = "Error deleting candidate";


}
