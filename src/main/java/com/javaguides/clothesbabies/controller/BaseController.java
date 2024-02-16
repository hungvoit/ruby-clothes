package com.javaguides.clothesbabies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguides.clothesbabies.common.Rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected HttpStatus status;

    public BaseController() {
    }

    protected ResponseEntity<Rest> responseEntity(Rest rest) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(new ObjectMapper().writeValueAsBytes(rest).length));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Rest>(rest, headers, HttpStatus.valueOf(rest
                .getStatus()));
    }

    protected ResponseEntity<Rest> responseEntity(HttpStatus httpStatus) {
        Rest rest = new Rest(httpStatus);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(new ObjectMapper().writeValueAsString(rest).length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Rest>(rest, headers, httpStatus);
    }

    protected ResponseEntity<Rest> responseEntity(HttpStatus httpStatus,
                                                  String message) {
        Rest rest = new Rest(httpStatus, message);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(new ObjectMapper().writeValueAsString(rest).length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Rest>(rest, headers, httpStatus);
    }

    protected ResponseEntity<Rest> responseEntity(Exception ex) {
        Rest rest = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(new ObjectMapper().writeValueAsString(rest).length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Rest>(rest, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
