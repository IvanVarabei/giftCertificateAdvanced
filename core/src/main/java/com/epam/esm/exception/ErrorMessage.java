package com.epam.esm.exception;

public class ErrorMessage {
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found (id = %s)";
    public static final String PAGE_NOT_FOUND = "Requested page not found (size = %s, page = %s). The last page is %s";
    public static final String USER_NOT_FOUND = "User not found (username = %s)";
    public static final String USER_ALREADY_EXISTS = "User already exists (email = %s)";

    private ErrorMessage() {
    }
}
