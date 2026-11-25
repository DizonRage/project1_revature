package org.example.controller;

import org.example.entity.AccountApplication;
import org.example.entity.User;
import org.example.exception.AccountClosedException;
import org.example.exception.AccountNotFoundException;
import org.example.exception.ApplicationNotFoundException;
import org.example.exception.BankException;
import org.example.exception.InvalidApplicationStateException;
import org.example.service.AccountApplicationService;
import org.example.service.AccountService;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.stream.Stream;

import static org.example.controller.ControllerTestSupport.ADMIN_ID;
import static org.example.controller.ControllerTestSupport.account;
import static org.example.controller.ControllerTestSupport.admin;
import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.exception;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.example.controller.ControllerTestSupport.json;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    private static final String CANCEL = "{\"status\":\"CANCELLED\"}";
    private static final String APPROVE = "{\"status\":\"APPROVED\"}";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    AccountApplicationService applicationService;

    private static AccountApplication application(String status) {
        return new AccountApplication(5, 7, ADMIN_ID, "SAVINGS", status);
    }

    @Test
    void listsAccountsByStatus() throws Exception {
        when(accountService.getAccounts("OPEN")).thenReturn(List.of(account()));

        mockMvc.perform(get("/admin/accounts").param("status", "OPEN").session(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountId").value(3))
                .andExpect(jsonPath("$[0].applicationId").doesNotExist());
    }

    @Test
    void listsAllAccountsWithoutStatus() throws Exception {
        mockMvc.perform(get("/admin/accounts").session(admin()))
                .andExpect(status().isOk());

        verify(accountService).getAccounts(null);
    }

    @Test
    void cancelsAccount() throws Exception {
        var cancelled = account();
        cancelled.setStatus("CANCELLED");
        when(accountService.updateAccountStatus(3, "CANCELLED")).thenReturn(cancelled);

        mockMvc.perform(json(patch("/admin/accounts/3"), CANCEL).session(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    static Stream<Arguments> accountErrors() {
        return Stream.of(
                Arguments.of(AccountNotFoundException.class, 404),
                Arguments.of(AccountClosedException.class, 400));
    }

    @ParameterizedTest
    @MethodSource("accountErrors")
    void cancelAccountMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(accountService.updateAccountStatus(3, "CANCELLED")).thenThrow(exception(type));

        expectError(mockMvc.perform(json(patch("/admin/accounts/3"), CANCEL).session(admin())), code, "boom");
    }

    @Test
    void listsUsersWithoutPasswords() throws Exception {
        when(userService.listUsers()).thenReturn(
                List.of(new User(7, "sam", "secret", "Sam", "Lee", "sam@example.com", "CUSTOMER")));

        mockMvc.perform(get("/admin/users").session(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("sam"))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].pass").doesNotExist());
    }

    @Test
    void listsApplicationsByStatus() throws Exception {
        when(applicationService.getApplications("PENDING")).thenReturn(List.of(application("PENDING")));

        mockMvc.perform(get("/admin/applications").param("status", "PENDING").session(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicationId").value(5))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @ParameterizedTest
    @CsvSource({"APPROVED", "DENIED"})
    void decidesApplicationAsCallingAdmin(String decision) throws Exception {
        when(applicationService.decideApplication(5, decision, ADMIN_ID)).thenReturn(application(decision));

        mockMvc.perform(json(patch("/admin/applications/5"), "{\"status\":\"" + decision + "\"}").session(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approverId").value(1))
                .andExpect(jsonPath("$.status").value(decision));
    }

    static Stream<Arguments> applicationErrors() {
        return Stream.of(
                Arguments.of(ApplicationNotFoundException.class, 404),
                Arguments.of(InvalidApplicationStateException.class, 400));
    }

    @ParameterizedTest
    @MethodSource("applicationErrors")
    void decideApplicationMapsErrors(Class<? extends BankException> type, int code) throws Exception {
        when(applicationService.decideApplication(5, "APPROVED", ADMIN_ID)).thenThrow(exception(type));

        expectError(mockMvc.perform(json(patch("/admin/applications/5"), APPROVE).session(admin())),
                code, "boom");
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "/admin/accounts/3     | {}                       | status must not be blank",
            "/admin/accounts/3     | {\"status\":\"OPEN\"}    | status must be CANCELLED",
            "/admin/accounts/3     | {                        | Malformed JSON request",
            "/admin/applications/5 | {}                       | status must not be blank",
            "/admin/applications/5 | {\"status\":\"PENDING\"} | status must be APPROVED or DENIED",
            "/admin/applications/5 | {                        | Malformed JSON request"
    })
    void rejectsInvalidBodies(String url, String body, String message) throws Exception {
        expectError(mockMvc.perform(json(patch(url), body).session(admin())), 400, message);

        verifyNoInteractions(accountService, applicationService);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "GET   | /admin/accounts       |",
            "PATCH | /admin/accounts/3     | {\"status\":\"CANCELLED\"}",
            "GET   | /admin/users          |",
            "GET   | /admin/applications   |",
            "PATCH | /admin/applications/5 | {\"status\":\"APPROVED\"}"
    })
    void rejectsAnonymousAndNonAdminCallers(String method, String url, String body) throws Exception {
        expectError(mockMvc.perform(build(method, url, body)), 401, "Login required");
        expectError(mockMvc.perform(build(method, url, body).session(customer())),
                403, "Admin access required");

        verifyNoInteractions(accountService, userService, applicationService);
    }

    private static MockHttpServletRequestBuilder build(String method, String url, String body) {
        MockHttpServletRequestBuilder builder = request(HttpMethod.valueOf(method), url);
        return body == null ? builder : json(builder, body);
    }
}
