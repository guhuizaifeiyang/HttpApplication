package com.chandler.http;

/**
 * Created by yaomenglin on 2018/3/24.
 */

public class AppException extends Exception {
    public int statusCode;
    public String responseMessage;

    public enum ErrorType {TIMEOUT, SERVER, JSON, IO, FILE_NOT_FOUND, MANUAL}
    public ErrorType type;

    public AppException(ErrorType type, String responseMessage) {
        super(responseMessage);
        this.type = type;
        this.responseMessage = responseMessage;
    }

    public AppException(int status, String responseMessage) {
        super(responseMessage);
        this.statusCode = status;
        this.responseMessage = responseMessage;
    }

    public AppException(String responseMessage) {
        super(responseMessage);
    }
}
