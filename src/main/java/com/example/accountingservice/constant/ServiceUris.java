package com.example.accountingservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceUris {
    public static final String HEALTH_CHECK_URL = "/accounts/health";
    public static final String CREATE_ACCOUNT_URL = "/accounts";
    public static final String GET_ACCOUNT_URL = "/accounts/{accountId}";
    public static final String CREATE_TRANSACTION_URL = "/transactions";
}
