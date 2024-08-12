package com.example.accountingservice.services;

import com.example.accountingservice.constant.ErrorConstants;
import com.example.accountingservice.enums.OperationTypes;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.Accounts;
import com.example.accountingservice.models.Transactions;
import com.example.accountingservice.models.requests.CreateTransactionRequest;
import com.example.accountingservice.models.responses.CreateTransactionResponse;
import com.example.accountingservice.repository.AccountRepository;
import com.example.accountingservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    /**
     *
     * @param request
     * @return
     * @throws AccountingException if any database  error occurs or account is not found for input accountId
     */
    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws AccountingException {
        validateRequest(request);
        Transactions transaction = null;
        try {
            Optional<Accounts> currentAccount = accountRepository.findById(request.getAccountId());
            if (currentAccount.isEmpty()) {
                throw new AccountingException(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND);
            }
            transaction = convertToTransactionsFromCreateTransactionRequest(request, currentAccount.get());
            transaction = transactionRepository.save(transaction);
        } catch (AccountingException e) {
            logger.error("Error while creating transaction details in db for request {} , exception {}", request,  e.getMessage());
            throw e;
        }
        return convertTransactionsToCreateTransactionResponse(transaction);
    }

    private void validateRequest(CreateTransactionRequest request) throws AccountingException {
        //todo
        if(request.getAccountId() == null) {
            throw new AccountingException(ErrorConstants.BAD_REQUEST);
        }
        if(request.getAmount() == null) {
            throw new AccountingException(ErrorConstants.BAD_REQUEST);
        }
        if(request.getOperationTypeId() == null) {
            throw new AccountingException(ErrorConstants.BAD_REQUEST);
        }
        if(!OperationTypes.isValidOperationTypeId(request.getOperationTypeId())) {
            throw new AccountingException(ErrorConstants.INVALID_OPERATION_TYPE);
        }
    }

    private Transactions convertToTransactionsFromCreateTransactionRequest(CreateTransactionRequest request, Accounts account) {
        Transactions transaction = new Transactions();
        transaction.setAccount(account);
        adjustAmountBasedOnOperationType(request);
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setOperationType(OperationTypes.convertOperationTypeIdToEnum(request.getOperationTypeId()));
        return transaction;
    }

    private CreateTransactionResponse convertTransactionsToCreateTransactionResponse(Transactions transaction) {
        CreateTransactionResponse response = new CreateTransactionResponse();
        response.setStatusCode(200);
        response.setTransactionId(transaction.getId());
        response.setStatusMessage("SUCCESS");
        return response;
    }
    private void adjustAmountBasedOnOperationType(CreateTransactionRequest request) {
        if (OperationTypes.isNegativeAmount(request.getOperationTypeId())) {
            request.setAmount(request.getAmount() * -1);
        }
    }
}
