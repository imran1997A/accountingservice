package com.example.accountingservice.exceptions;


import com.example.accountingservice.constant.HttpCodes;
import lombok.Getter;

@Getter
public class ValidationException extends AccountingException {

    public ValidationException(String code) {
        super(code, HttpCodes.BAD_REQUEST);
    }
}
