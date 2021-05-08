package com.example.demo.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
public final class ApiError implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2655579448683083664L;

    private HttpStatus status;
    private int code;
    private String message;
    private List<String> errors;
    private LocalDateTime timeStap;

    public ApiError() {
        super();
        this.timeStap = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
        this.code = status.value();

    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.code = status.value();
    }

    public ApiError(HttpStatus status, String message, String errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(errors);
        this.code = status.value();
    }


}
