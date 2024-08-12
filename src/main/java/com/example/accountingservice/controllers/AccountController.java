package com.example.accountingservice.controllers;

import com.example.accountingservice.constant.ServiceUris;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateAccountRequest;
import com.example.accountingservice.models.responses.CreateAccountResponse;
import com.example.accountingservice.models.responses.GetAccountResponse;
import com.example.accountingservice.services.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @PostMapping(value = ServiceUris.CREATE_ACCOUNT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateAccountResponse createAccount(@RequestBody @Validated CreateAccountRequest request) throws AccountingException {
        return accountService.createAccount(request);
    }

    @GetMapping(value = ServiceUris.GET_ACCOUNT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAccountResponse getAccount(@PathVariable Long accountId) throws AccountingException {
        return accountService.getAccount(accountId);
    }
}
