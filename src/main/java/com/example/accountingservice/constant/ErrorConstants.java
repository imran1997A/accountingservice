package com.example.accountingservice.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    public static final String INTERNAL_SERVER_ERROR = "000000";
    public static final String BAD_REQUEST = "000001";
    public static final String BLANK_DOCUMENT_NUMBER = "100000";
    public static final String ACCOUNT_ID_DETAILS_NOT_FOUND = "100001";
    public static final String INVALID_OPERATION_TYPE = "100002";
    public static final String DOCUMENT_NUMBER_TOO_LONG = "100003";
    public static final String DUPLICATE_ACCOUNT_ENTRY = "100004";
}
