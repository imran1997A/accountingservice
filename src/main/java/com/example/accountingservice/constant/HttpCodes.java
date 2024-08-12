package com.example.accountingservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpCodes {
    public static final int BAD_REQUEST = 400;

    public static final int INTERNAL_SERVER_ERROR = 500;

    public static final int UNAUTHOURIZED = 403;

    public static final int NOT_FOUND = 404;
}
