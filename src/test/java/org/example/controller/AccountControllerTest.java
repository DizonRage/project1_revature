package org.example.controller;

import org.example.entity.AccountOwner;
import org.example.entity.Transaction;
import org.example.exception.AccountClosedException;
import org.example.exception.AccountNotFoundException;
import org.example.exception.AlreadyOwnerException;
import org.example.exception.BankException;
import org.example.exception.ForbiddenException;
import org.example.exception.InsufficientFundsException;
import org.example.exception.InvalidAmountException;
import org.example.exception.UserNotFoundException;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.example.controller.ControllerTestSupport.USER_ID;
import static org.example.controller.ControllerTestSupport.account;
import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.exception;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.example.controller.ControllerTestSupport.json;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    private static final BigDecimal AMOUNT = new BigDecimal("25.50");
    private static final String MONEY = "{\"amount\":25.50}";
    private static final String OWNER = "{\"username\":\"pat\"}";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @MockitoBean
    TransactionService transactionService;

    // GET /accounts/{id}

    @Test
    void getAccountReturnsAccount() throws Exception {
        when(accountService.getAccount(3, USER_ID, "CUSTOMER")).thenReturn(account());

        mockMvc.perform(get("/accounts/3").session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.accountNumber").value("ACC-3"))
                .andExpect(jsonPath("$.accountType").value("CHECKING"))
                .andExpect(jsonPath("$.balance").value(70.00))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.applicationId").doesNotExist());
    }

    static Stream<Arguments> readErrors() {
        return Stream.of(
                Arguments.of(AccountNotFoundException.class, 404),
                Arguments.of(ForbiddenException.class, 403));
    }

    @ParameterizedTest
    @MethodSource("readErrors")
    void getAccountMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.getAccount(3, USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(get("/accounts/3").session(customer())), code, "boom");
    }

    // GET /accounts/{id}/transactions

    @Test
    void getTransactionsReturnsList() throws Exception {
        when(transactionService.getTransactionsForAccount(3, USER_ID, "CUSTOMER"))
                .thenReturn(List.of(new Transaction(11, 3, "DEPOSIT", 25.5)));

        mockMvc.perform(get("/accounts/3/transactions").session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value(11))
                .andExpect(jsonPath("$[0].accountId").value(3))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[0].amount").value(25.5));
    }

    @ParameterizedTest
    @MethodSource("readErrors")
    void getTransactionsMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(transactionService.getTransactionsForAccount(3, USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(get("/accounts/3/transactions").session(customer())), code, "boom");
    }

    // POST /accounts/{id}/owners

    @Test
    void addOwnerReturnsCreated() throws Exception {
        when(accountService.addOwner(3, "pat", USER_ID, "CUSTOMER")).thenReturn(new AccountOwner(3, 12));

        mockMvc.perform(json(post("/accounts/3/owners"), OWNER).session(customer()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.userId").value(12));
    }

    @Test
    void addOwnerValidatesUsername() throws Exception {
        expectError(mockMvc.perform(json(post("/accounts/3/owners"), "{\"username\":\" \"}").session(customer())),
                400, "username must not be blank");
        verifyNoInteractions(accountService);
    }

    static Stream<Arguments> ownerErrors() {
        return Stream.of(
                Arguments.of(AccountNotFoundException.class, 404),
                Arguments.of(UserNotFoundException.class, 404),
                Arguments.of(ForbiddenException.class, 403),
                Arguments.of(AccountClosedException.class, 400),
                Arguments.of(AlreadyOwnerException.class, 409));
    }

    @ParameterizedTest
    @MethodSource("ownerErrors")
    void addOwnerMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.addOwner(3, "pat", USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(json(post("/accounts/3/owners"), OWNER).session(customer())), code, "boom");
    }

    // POST /accounts/{id}/deposit and /withdraw

    @Test
    void depositPassesCallerToService() throws Exception {
        when(accountService.deposit(3, AMOUNT, USER_ID, "CUSTOMER")).thenReturn(account());

        mockMvc.perform(json(post("/accounts/3/deposit"), MONEY).session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.balance").value(70.00));
    }

    @Test
    void withdrawPassesCallerToService() throws Exception {
        when(accountService.withdraw(3, AMOUNT, USER_ID, "CUSTOMER")).thenReturn(account());

        mockMvc.perform(json(post("/accounts/3/withdraw"), MONEY).session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(3))
                .andExpect(jsonPath("$.balance").value(70.00));
    }

    static Stream<Arguments> depositErrors() {
        return Stream.of(
                Arguments.of(AccountNotFoundException.class, 404),
                Arguments.of(ForbiddenException.class, 403),
                Arguments.of(InvalidAmountException.class, 400),
                Arguments.of(AccountClosedException.class, 400));
    }

    @ParameterizedTest
    @MethodSource("depositErrors")
    void depositMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.deposit(3, AMOUNT, USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(json(post("/accounts/3/deposit"), MONEY).session(customer())), code, "boom");
    }

    static Stream<Arguments> withdrawErrors() {
        return Stream.concat(depositErrors(),
                Stream.of(Arguments.of(InsufficientFundsException.class, 400)));
    }

    @ParameterizedTest
    @MethodSource("withdrawErrors")
    void withdrawMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.withdraw(3, AMOUNT, USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(json(post("/accounts/3/withdraw"), MONEY).session(customer())), code, "boom");
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "deposit  | {}                 | amount must not be null",
            "deposit  | {\"amount\":0}     | amount must be greater than 0",
            "deposit  | {\"amount\":0.005} | amount numeric value out of bounds (<13 digits>.<2 digits> expected)",
            "withdraw | {}                 | amount must not be null",
            "withdraw | {\"amount\":-1}    | amount must be greater than 0",
            "withdraw | {\"amount\":0.005} | amount numeric value out of bounds (<13 digits>.<2 digits> expected)",
            "deposit  | {                  | Malformed JSON request",
            "withdraw | {                  | Malformed JSON request",
            "owners   | {                  | Malformed JSON request"
    })
    void rejectsInvalidBodies(String action, String body, String message) throws Exception {
        expectError(mockMvc.perform(json(post("/accounts/3/" + action), body).session(customer())), 400, message);

        verifyNoInteractions(accountService);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "GET  | /accounts/3",
            "GET  | /accounts/3/transactions",
            "POST | /accounts/3/owners",
            "POST | /accounts/3/deposit",
            "POST | /accounts/3/withdraw"
    })
    void requiresLogin(String method, String url) throws Exception {
        var request = method.equals("GET")
                ? get(url)
                : json(post(url), url.endsWith("owners") ? OWNER : MONEY);

        expectError(mockMvc.perform(request), 401, "Login required");

        verifyNoInteractions(accountService, transactionService);
    }
}
