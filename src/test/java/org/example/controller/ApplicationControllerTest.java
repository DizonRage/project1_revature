package org.example.controller;

import org.example.entity.AccountApplication;
import org.example.service.AccountApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.example.controller.ControllerTestSupport.USER_ID;
import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.example.controller.ControllerTestSupport.json;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    private static final String BODY = "{\"accountType\":\"SAVINGS\"}";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountApplicationService applicationService;

    private static AccountApplication pending() {
        return new AccountApplication(5, USER_ID, 0, "SAVINGS", "PENDING");
    }

    @Test
    void submitReturnsCreated() throws Exception {
        when(applicationService.submitApplication(USER_ID, "SAVINGS")).thenReturn(pending());

        mockMvc.perform(json(post("/applications"), BODY).session(customer()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value(5))
                .andExpect(jsonPath("$.applicantId").value(7))
                .andExpect(jsonPath("$.approverId").isEmpty())
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "{}                          | accountType must not be blank",
            "{\"accountType\":\"GOLD\"}  | accountType must be CHECKING or SAVINGS",
            "{                           | Malformed JSON request"
    })
    void submitValidatesBody(String body, String message) throws Exception {
        expectError(mockMvc.perform(json(post("/applications"), body).session(customer())), 400, message);

        verifyNoInteractions(applicationService);
    }

    @Test
    void returnsCallersApplications() throws Exception {
        when(applicationService.getApplicationsForUser(USER_ID)).thenReturn(List.of(pending()));

        mockMvc.perform(get("/applications/me").session(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].applicationId").value(5));
    }

    @Test
    void submitRequiresLogin() throws Exception {
        expectError(mockMvc.perform(json(post("/applications"), BODY)), 401, "Login required");

        verifyNoInteractions(applicationService);
    }

    @Test
    void listRequiresLogin() throws Exception {
        expectError(mockMvc.perform(get("/applications/me")), 401, "Login required");

        verifyNoInteractions(applicationService);
    }
}
