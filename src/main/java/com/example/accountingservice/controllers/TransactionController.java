package com.example.accountingservice.controllers;

import com.example.accountingservice.constant.ServiceUris;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateTransactionRequest;
import com.example.accountingservice.models.responses.CreateTransactionResponse;
import com.example.accountingservice.services.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService transactionService;

    @PostMapping(value = ServiceUris.CREATE_TRANSACTION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateTransactionResponse createTransaction(@RequestBody CreateTransactionRequest request) throws AccountingException {
        return transactionService.createTransaction(request);
    }
}
