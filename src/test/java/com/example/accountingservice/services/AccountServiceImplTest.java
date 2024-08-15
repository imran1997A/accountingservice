package com.example.accountingservice.services;

import com.example.accountingservice.constant.ErrorConstants;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.exceptions.NotFoundException;
import com.example.accountingservice.models.Accounts;
import com.example.accountingservice.models.requests.CreateAccountRequest;
import com.example.accountingservice.models.responses.CreateAccountResponse;
import com.example.accountingservice.models.responses.GetAccountResponse;
import com.example.accountingservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount_Success() throws AccountingException {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setDocumentNumber("123456780");

        Accounts account = new Accounts();

        when(accountRepository.save(any(Accounts.class))).thenReturn(account);

        CreateAccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Accounts.class));
    }

    @Test
    public void testCreateAccount_Failure() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setDocumentNumber("123456780");

        when(accountRepository.save(any(Accounts.class))).thenThrow(new RuntimeException("Database Error"));

        assertThrows(AccountingException.class, () -> {
            accountService.createAccount(request);
        });

        verify(accountRepository, times(1)).save(any(Accounts.class));
    }

    @Test
    public void testGetAccount_Success() throws AccountingException {
        Long accountId = 1L;
        Accounts account = new Accounts();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        GetAccountResponse response = accountService.getAccount(accountId);

        assertNotNull(response);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void testGetAccount_NotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            accountService.getAccount(accountId);
        });

        assertEquals(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND, exception.getCode());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void testGetAccount_Failure() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenThrow(new RuntimeException("Database Error"));

        assertThrows(RuntimeException.class, () -> {
            accountService.getAccount(accountId);
        });

        verify(accountRepository, times(1)).findById(accountId);
    }


}