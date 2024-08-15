package com.example.accountingservice.exceptions;


import com.example.accountingservice.constant.HttpCodes;
import lombok.Getter;

@Getter
public class ConflictException extends AccountingException {

    public ConflictException(String code) {
        super(code, HttpCodes.CONFLICT);
    }
}
