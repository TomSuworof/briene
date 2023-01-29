package com.salat.briene.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@AllArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}
