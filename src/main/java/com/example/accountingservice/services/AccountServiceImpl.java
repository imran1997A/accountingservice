package com.example.accountingservice.services;

import com.example.accountingservice.constant.ErrorConstants;
import com.example.accountingservice.exceptions.AccountingException;
import com.example.accountingservice.models.Accounts;
import com.example.accountingservice.models.requests.CreateAccountRequest;
import com.example.accountingservice.models.responses.CreateAccountResponse;
import com.example.accountingservice.models.responses.GetAccountResponse;
import com.example.accountingservice.repository.AccountRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountRepository accountRepository;
    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     * @param request create account request
     * @return created accountId in response
     * @throws AccountingException when any error occurs in saving entity to database
     */
    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) throws AccountingException {
        validateRequest(request);
        Accounts account = convertRequestToAccounts(request);
        try {
            account = accountRepository.save(account);
        }
        catch (DataIntegrityViolationException violationException) {
            logger.error("Error Occurred while saving account details in DB for request{}, exception {}", request, violationException.getMessage());
            throw new AccountingException(ErrorConstants.DUPLICATE_ACCOUNT_ENTRY);
        }
        catch (Exception e) {
            logger.error("Error Occurred while saving account details in DB for request{}, exception {}", request, e.getMessage());
            throw e;
        }
        return convertAccountsToCreateAccountResponse(account);
    }

    /**
     * @param accountId
     * @return
     * @throws AccountingException if any database  error occurs or account is not found for input accountId
     */
    @Override
    public GetAccountResponse getAccount(Long accountId) throws AccountingException {
        try {
            Optional<Accounts> account = accountRepository.findById(accountId);
            if (account.isPresent()) {
                return convertAccountsToGetAccountResponse(account.get());
            } else {
                throw new AccountingException(ErrorConstants.ACCOUNT_ID_DETAILS_NOT_FOUND);
            }
        } catch (AccountingException e) {
            logger.error("Error Occurred while fetching account details from DB for accountId {}, exception {}", accountId, e.getMessage());
            throw e;
        }
    }

    private GetAccountResponse convertAccountsToGetAccountResponse(Accounts account) {
        GetAccountResponse response = new GetAccountResponse();
        response.setStatusCode(200);
        response.setStatusMessage("SUCCESS");
        response.setAccountId(account.getId());
        response.setDocumentNumber(account.getDocumentNumber());
        return response;
    }

    private CreateAccountResponse convertAccountsToCreateAccountResponse(Accounts account) {
        CreateAccountResponse response = new CreateAccountResponse();
        response.setStatusCode(200);
        response.setStatusMessage("SUCCESS");
        response.setAccountId(account.getId());
        return response;
    }

    private Accounts convertRequestToAccounts(CreateAccountRequest request) {
        Accounts account = new Accounts();
        account.setDocumentNumber(request.getDocumentNumber());
        return account;
    }

    private void validateRequest(CreateAccountRequest request) throws AccountingException {
        if (StringUtils.isBlank(request.getDocumentNumber())) {
            throw new AccountingException(ErrorConstants.BLANK_DOCUMENT_NUMBER);
        }
        if(request.getDocumentNumber().length() > 20) {
            throw new AccountingException(ErrorConstants.DOCUMENT_NUMBER_TOO_LONG);
        }
    }
}
