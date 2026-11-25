package org.example.controller;

import org.example.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.example.controller.ControllerTestSupport.USER_ID;
import static org.example.controller.ControllerTestSupport.account;
import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @Test
    void returnsCallersAccounts() throws Exception {
        when(accountService.getAccountsForUser(USER_ID)).thenReturn(List.of(account()));

        mockMvc.perform(get("/users/me/accounts").session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].accountId").value(3))
                .andExpect(jsonPath("$[0].applicationId").doesNotExist());
    }

    @Test
    void requiresLogin() throws Exception {
        expectError(mockMvc.perform(get("/users/me/accounts")), 401, "Login required");

        verifyNoInteractions(accountService);
    }
}
