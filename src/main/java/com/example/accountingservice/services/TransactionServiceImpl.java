package com.example.accountingservice.services;

import com.example.accountingservice.constant.ErrorConstants;
import com.example.accountingservice.enums.OperationTypes;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.exceptions.NotFoundException;
import com.example.accountingservice.exceptions.ValidationException;
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
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    /**
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
                throw new NotFoundException(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND);
            }
            transaction = convertToTransactionsFromCreateTransactionRequest(request, currentAccount.get());
            //
            if (!OperationTypes.isNegativeAmount(request.getOperationTypeId())) {
                request.setAmount(compensatePreviousDebitTransactions(request.getAccountId(), request.getAmount()));
                transaction.setBalance(BigDecimal.valueOf(request.getAmount()));
            }
            transaction = transactionRepository.save(transaction);
        } catch (AccountingException e) {
            logger.error("Error while creating transaction details in db for request {} , exception {}", request, e.getMessage());
            throw e;
        }
        return convertTransactionsToCreateTransactionResponse(transaction);
    }

    private Double compensatePreviousDebitTransactions(Long accountId, Double creditAmount) throws AccountingException {
        List<Transactions> debitTransactions = transactionRepository.findAllByAccountId(accountId);
        for (Transactions currentTransactions : debitTransactions) {
            double currentBalance = currentTransactions.getBalance().doubleValue();
            if (currentBalance >= 0.0) {
                continue;
            }
            if (creditAmount > abs(currentBalance)) {
                creditAmount += currentBalance;
                currentBalance = 0.00;
                currentTransactions.setBalance(BigDecimal.valueOf(currentBalance));
            } else {
                currentBalance += creditAmount;
                creditAmount = 0.00;
                currentTransactions.setBalance(BigDecimal.valueOf(currentBalance));
                break;
            }
        }
        try {
            transactionRepository.saveAll(debitTransactions);
        } catch (Exception e) {
            logger.error("Error While updating the debit transactions balance, exception {}", e.getMessage());
            throw new AccountingException(ErrorConstants.INTERNAL_SERVER_ERROR);
        }
        return creditAmount;
    }

    private void validateRequest(CreateTransactionRequest request) throws ValidationException {
        if (request.getAccountId() == null) {
            throw new ValidationException(ErrorConstants.BLANK_ACCOUNT_ID);
        }
        if (!isValidAmount(request.getAmount())) {
            throw new ValidationException(ErrorConstants.INVALID_AMOUNT);
        }
        if (request.getOperationTypeId() == null) {
            throw new ValidationException(ErrorConstants.BLANK_OPERATION_TYPE);
        }
        if (!OperationTypes.isValidOperationTypeId(request.getOperationTypeId())) {
            throw new ValidationException(ErrorConstants.INVALID_OPERATION_TYPE);
        }
    }

    private boolean isValidAmount(Double amount) {
        if (amount == null || amount < 0.00) {
            return false;
        }
        return BigDecimal.valueOf(amount).scale() <= 2;
    }

    private Transactions convertToTransactionsFromCreateTransactionRequest(CreateTransactionRequest request, Accounts account) {
        Transactions transaction = new Transactions();
        transaction.setAccount(account);
        adjustAmountBasedOnOperationType(request);
        transaction.setAmount(BigDecimal.valueOf(request.getAmount()));
        transaction.setBalance(BigDecimal.valueOf(request.getAmount()));
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
