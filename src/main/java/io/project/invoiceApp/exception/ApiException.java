package io.project.invoiceApp.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
