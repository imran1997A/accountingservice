package com.example.accountingservice.controllers;

import com.example.accountingservice.constant.ServiceUris;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.requests.CreateTransactionRequest;
import com.example.accountingservice.models.responses.CreateTransactionResponse;
import com.example.accountingservice.services.ITransactionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITransactionService transactionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateTransaction() throws Exception, AccountingException {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountId(1L);
        request.setOperationTypeId(1);
        request.setAmount(50.0);

        CreateTransactionResponse response = new CreateTransactionResponse();
        response.setTransactionId(1L);

        Mockito.when(transactionService.createTransaction(any(CreateTransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post(ServiceUris.CREATE_TRANSACTION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account_id\": 1, \"operation_type_id\": 1, \"amount\": 50.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"transaction_id\": 1}"));

        Mockito.verify(transactionService).createTransaction(any(CreateTransactionRequest.class));
    }
}
