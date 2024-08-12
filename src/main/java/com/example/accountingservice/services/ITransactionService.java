package com.example.accountingservice.services;

import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateTransactionRequest;
import com.example.accountingservice.models.responses.CreateTransactionResponse;

public interface ITransactionService {
    CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws AccountingException;
}
