package com.example.accountingservice.exceptions;


import com.example.accountingservice.constant.HttpCodes;
import lombok.Getter;

@Getter
public class NotFoundException extends AccountingException {

    public NotFoundException(String code) {
        super(code, HttpCodes.NOT_FOUND);
    }
}
