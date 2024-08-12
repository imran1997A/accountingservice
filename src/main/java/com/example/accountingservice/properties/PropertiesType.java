package com.example.accountingservice.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PropertiesType {

    ERROR_CODES_PROPERTIES("error-code.properties");

    private final String fileName;
}