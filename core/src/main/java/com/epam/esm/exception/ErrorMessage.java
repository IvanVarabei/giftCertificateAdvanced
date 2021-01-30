package com.epam.esm.exception;

public class ErrorMessage {
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found (id = %s)";
    public static final String PAGE_NOT_FOUND = "Requested page not found (size = %s, page = %s). The last page is %s";

    private ErrorMessage() {
    }
}
