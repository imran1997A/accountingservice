package com.example.accountingservice.exceptions;


import com.example.accountingservice.properties.PropertiesLoader;
import com.example.accountingservice.properties.PropertiesType;
import lombok.Getter;

@Getter
public class AccountingException extends Throwable {

    private final String message;
    private final String code;
    private int httpCode = 200;

    public AccountingException(String code) {
        this.message = PropertiesLoader.getProperties(PropertiesType.ERROR_CODES_PROPERTIES).getProperty(code);
        this.code = code;
    }

    public AccountingException(String code, String msg) {
        this.message = msg;
        this.code = code;
    }

    public AccountingException(String code, int httpCode) {
        this.message = PropertiesLoader.getProperties(PropertiesType.ERROR_CODES_PROPERTIES).getProperty(code);
        this.code = code;
        this.httpCode = httpCode;
    }
}
