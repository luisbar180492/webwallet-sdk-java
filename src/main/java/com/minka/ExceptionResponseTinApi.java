package com.minka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExceptionResponseTinApi extends Exception{

    private int errorCode;
    public String message;

    public ExceptionResponseTinApi(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

}
