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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        Accounts account = new Accounts();

        Transactions transaction = new Transactions();

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transactions.class))).thenReturn(transaction);

        CreateTransactionResponse response = transactionService.createTransaction(request);

        assertNotNull(response);
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_AccountNotFound() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND, exception.getCode());
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_BlankAccountId() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.BLANK_ACCOUNT_ID, exception.getCode());
        verify(accountRepository, times(0)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_InvalidAmount() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.001);
        request.setOperationTypeId(1);

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.INVALID_AMOUNT, exception.getCode());
        verify(accountRepository, times(0)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }


    @Test
    public void testCreateTransaction_InvalidOperationType() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.01);
        request.setOperationTypeId(8);
        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.INVALID_OPERATION_TYPE, exception.getCode());
        verify(accountRepository, times(0)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_BlankOperationType() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.00);

        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals(ErrorConstants.BLANK_OPERATION_TYPE, exception.getCode());
        verify(accountRepository, times(0)).findById(request.getAccountId());
        verify(transactionRepository, never()).save(any(Transactions.class));
    }

    @Test
    public void testCreateTransaction_UpdateBalanceSuccessOnCredit() throws AccountingException {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(4);

        Accounts account = new Accounts();

        Transactions debitTransaction = new Transactions();
        debitTransaction.setOperationType(OperationTypes.WITHDRAWAL);
        debitTransaction.setBalance(BigDecimal.valueOf(-50.00));

        List<Transactions> debitTransactions  =new ArrayList<>();
        debitTransactions.add(debitTransaction);

        Transactions creditTransaction = new Transactions();
        creditTransaction.setBalance(BigDecimal.valueOf(50.00));
        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transactions.class))).thenReturn(creditTransaction);
        when(transactionRepository.findAllByAccountId(anyLong())).thenReturn(debitTransactions);
        CreateTransactionResponse response = transactionService.createTransaction(request);

        assertNotNull(response);
        assertEquals(creditTransaction.getBalance(), BigDecimal.valueOf(request.getAmount()));
        verify(accountRepository, times(1)).findById(request.getAccountId());
        verify(transactionRepository, times(1)).save(any(Transactions.class));
        verify(transactionRepository, times(1)).findAllByAccountId(anyLong());
        verify(transactionRepository, times(1)).saveAll(any());
    }

    @Test
    public void testCreateTransaction_DatabaseError() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setAmount(100.00);
        request.setOperationTypeId(1);

        Accounts account = new Accounts();

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