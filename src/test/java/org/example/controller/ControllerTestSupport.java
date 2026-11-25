package org.example.controller;

import org.example.entity.Account;
import org.example.exception.BankException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class ControllerTestSupport {

    static final Integer USER_ID = 7;
    static final Integer ADMIN_ID = 1;

    private ControllerTestSupport() {
    }

    static MockHttpSession customer() {
        return session(USER_ID, "CUSTOMER");
    }

    static MockHttpSession admin() {
        return session(ADMIN_ID, "ADMIN");
    }

    private static MockHttpSession session(Integer userId, String role) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);
        session.setAttribute("role", role);
        return session;
    }

    static MockHttpServletRequestBuilder json(MockHttpServletRequestBuilder request, String body) {
        return request.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    static Account account() {
        Account account = new Account(3, 9, "ACC-3", "CHECKING", new BigDecimal("70.00"));
        account.setStatus("OPEN");
        return account;
    }

    static BankException exception(Class<? extends BankException> type) {
        try {
            return type.getConstructor(String.class).newInstance("boom");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static void expectError(ResultActions result, int expectedStatus, String message) throws Exception {
        result.andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.status").value(expectedStatus))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
