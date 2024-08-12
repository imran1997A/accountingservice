package com.example.accountingservice.services;


import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateAccountRequest;
import com.example.accountingservice.models.responses.CreateAccountResponse;
import com.example.accountingservice.models.responses.GetAccountResponse;

public interface IAccountService {
    CreateAccountResponse createAccount(CreateAccountRequest request) throws AccountingException;

    GetAccountResponse getAccount(Long accountId) throws AccountingException;
}

