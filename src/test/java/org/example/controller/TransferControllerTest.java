package org.example.controller;

import org.example.exception.AccountClosedException;
import org.example.exception.AccountNotFoundException;
import org.example.exception.BankException;
import org.example.exception.ForbiddenException;
import org.example.exception.InsufficientFundsException;
import org.example.exception.InvalidAmountException;
import org.example.exception.InvalidTransferException;
import org.example.service.AccountService;
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
import java.util.stream.Stream;

import static org.example.controller.ControllerTestSupport.USER_ID;
import static org.example.controller.ControllerTestSupport.account;
import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.exception;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.example.controller.ControllerTestSupport.json;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    private static final BigDecimal AMOUNT = new BigDecimal("30.00");
    private static final String BODY = "{\"fromAccountId\":3,\"toAccountId\":4,\"amount\":30.00}";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @Test
    void transferReturnsCreatedWithSourceBalance() throws Exception {
        when(accountService.transfer(3, 4, AMOUNT, USER_ID, "CUSTOMER")).thenReturn(account());

        mockMvc.perform(json(post("/transfers"), BODY).session(customer()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fromAccountId").value(3))
                .andExpect(jsonPath("$.toAccountId").value(4))
                .andExpect(jsonPath("$.amount").value(30.00))
                .andExpect(jsonPath("$.newBalance").value(70.00))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    static Stream<Arguments> errors() {
        return Stream.of(
                Arguments.of(AccountNotFoundException.class, 404),
                Arguments.of(ForbiddenException.class, 403),
                Arguments.of(InvalidAmountException.class, 400),
                Arguments.of(InvalidTransferException.class, 400),
                Arguments.of(AccountClosedException.class, 400),
                Arguments.of(InsufficientFundsException.class, 400));
    }

    @ParameterizedTest
    @MethodSource("errors")
    void transferMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.transfer(3, 4, AMOUNT, USER_ID, "CUSTOMER")).thenThrow(exception(type));

        expectError(mockMvc.perform(json(post("/transfers"), BODY).session(customer())), code, "boom");
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "{\"toAccountId\":4,\"amount\":1}                       | fromAccountId must not be null",
            "{\"fromAccountId\":3,\"amount\":1}                     | toAccountId must not be null",
            "{\"fromAccountId\":0,\"toAccountId\":4,\"amount\":1}   | fromAccountId must be greater than 0",
            "{\"fromAccountId\":3,\"toAccountId\":-4,\"amount\":1}  | toAccountId must be greater than 0",
            "{\"fromAccountId\":3,\"toAccountId\":4}                | amount must not be null",
            "{\"fromAccountId\":3,\"toAccountId\":4,\"amount\":0}   | amount must be greater than 0",
            "{                                                      | Malformed JSON request"
    })
    void rejectsInvalidBodies(String body, String message) throws Exception {
        expectError(mockMvc.perform(json(post("/transfers"), body).session(customer())), 400, message);

        verifyNoInteractions(accountService);
    }

    @Test
    void requiresLogin() throws Exception {
        expectError(mockMvc.perform(json(post("/transfers"), BODY)), 401, "Login required");

        verifyNoInteractions(accountService);
    }
}
