package com.example.accountingservice.services;

import com.example.accountingservice.constant.ErrorConstants;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.Accounts;
import com.example.accountingservice.models.Transactions;
import com.example.accountingservice.models.requests.CreateTransactionRequest;
import com.example.accountingservice.models.responses.CreateTransactionResponse;
import com.example.accountingservice.repository.AccountRepository;
import com.example.accountingservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction_Success() throws AccountingException {
        CreateTransactionRequest request = new CreateTransactionRequest();
        // Set necessary fields in the request object
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        Accounts account = new Accounts();
        // Set necessary fields in the account object

        Transactions transaction = new Transactions();
        // Set necessary fields in the transaction object

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transactions.class))).thenReturn(transaction);

        CreateTransactionResponse response = transactionService.createTransaction(request);

        assertNotNull(response);
        // Add more assertions based on your response object
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_AccountNotFound() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        // Set necessary fields in the request object
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        AccountingException exception = assertThrows(AccountingException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND, exception.getCode());
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_DatabaseError() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        // Set necessary fields in the request object
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        Accounts account = new Accounts();
        // Set necessary fields in the account object

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transactions.class))).thenThrow(new RuntimeException("Database Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals("Database Error", exception.getMessage());
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

}