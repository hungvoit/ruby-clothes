package com.javaguides.clothesbabies.common;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
public class Rest implements Serializable {

    private int status;

    private String message;

    private Object data;

    public Rest(int code) {
        this.status = code;
    }

    public Rest() {
    }

    public Rest(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.message = message;
    }

    public Rest(HttpStatus httpStatus) {
        this.status = httpStatus.value();
        this.message = httpStatus.name();
    }

    public Rest(Exception e) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = e.getMessage();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.status = httpStatus.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return status + "," + message;
    }
}
