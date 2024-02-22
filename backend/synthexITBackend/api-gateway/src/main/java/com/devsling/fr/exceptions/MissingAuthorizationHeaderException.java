package com.devsling.fr.exceptions;

public class MissingAuthorizationHeaderException  extends RuntimeException{

        public MissingAuthorizationHeaderException() {
            super("Missing authorization header");
        }

}
