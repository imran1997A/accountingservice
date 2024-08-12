package com.example.accountingservice.controllers.advice;


import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.responses.AccountingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ControllerAdviceHandler {
    private static final String ERROR_PRO_REQ_CONSTANT = "Error in processing request {}";

    @ExceptionHandler(value = {AccountingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AccountingResponse<Object> handleException(AccountingException exception) {
        log.error(ERROR_PRO_REQ_CONSTANT, exception);
        AccountingResponse<Object> response = new AccountingResponse<>();
        response.setStatus("FAILURE");
        response.setStatusCode(exception.getCode());
        response.setStatusMessage(exception.getMessage());
        return response;
    }

}
