// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

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
