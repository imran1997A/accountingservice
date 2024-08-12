package com.example.accountingservice.controllers;

import com.example.accountingservice.constant.ServiceUris;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateAccountRequest;
import com.example.accountingservice.models.responses.CreateAccountResponse;
import com.example.accountingservice.models.responses.GetAccountResponse;
import com.example.accountingservice.services.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountService accountService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateAccount() throws Exception, AccountingException {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setDocumentNumber("12345678900");

        CreateAccountResponse response = new CreateAccountResponse();
        response.setAccountId(1L);

        Mockito.when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(post(ServiceUris.CREATE_ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"document_number\": \"12345678900\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"account_id\": 1}"));

        Mockito.verify(accountService).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    public void testGetAccount() throws Exception, AccountingException {
        GetAccountResponse response = new GetAccountResponse();
        response.setAccountId(1L);
        response.setDocumentNumber("12345678900");

        Mockito.when(accountService.getAccount(eq(1L))).thenReturn(response);

        mockMvc.perform(get(ServiceUris.GET_ACCOUNT_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"account_id\": 1, \"document_number\": \"12345678900\"}"));

        Mockito.verify(accountService).getAccount(eq(1L));
    }
}
