package edu.mum.exception;

public class UploadImageException extends RuntimeException {

    public UploadImageException(String message) {
        super(message);
    }

    public UploadImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
