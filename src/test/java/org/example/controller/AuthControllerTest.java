package org.example.controller;

import org.example.entity.User;
import org.example.exception.BankException;
import org.example.exception.InvalidCredentialsException;
import org.example.exception.UsernameTakenException;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.controller.ControllerTestSupport.customer;
import static org.example.controller.ControllerTestSupport.expectError;
import static org.example.controller.ControllerTestSupport.json;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String REGISTER = """
            {"username":"sam","password":"secret","firstName":"Sam","lastName":"Lee","email":"sam@example.com"}""";
    private static final String LOGIN = """
            {"username":"sam","password":"secret"}""";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    private static User user() {
        return new User(7, "sam", "secret", "Sam", "Lee", "sam@example.com", "CUSTOMER");
    }

    @Test
    void registerReturnsCreatedUserWithoutPassword() throws Exception {
        when(userService.register("sam", "secret", "Sam", "Lee", "sam@example.com")).thenReturn(user());

        mockMvc.perform(json(post("/auth/register"), REGISTER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.username").value("sam"))
                .andExpect(jsonPath("$.firstName").value("Sam"))
                .andExpect(jsonPath("$.lastName").value("Lee"))
                .andExpect(jsonPath("$.email").value("sam@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.pass").doesNotExist());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "username  | {\"password\":\"p\",\"firstName\":\"S\",\"lastName\":\"L\",\"email\":\"s@example.com\"}",
            "password  | {\"username\":\"s\",\"firstName\":\"S\",\"lastName\":\"L\",\"email\":\"s@example.com\"}",
            "firstName | {\"username\":\"s\",\"password\":\"p\",\"lastName\":\"L\",\"email\":\"s@example.com\"}",
            "lastName  | {\"username\":\"s\",\"password\":\"p\",\"firstName\":\"S\",\"email\":\"s@example.com\"}",
            "email     | {\"username\":\"s\",\"password\":\"p\",\"firstName\":\"S\",\"lastName\":\"L\"}"
    })
    void registerRequiresEveryField(String field, String body) throws Exception {
        expectError(mockMvc.perform(json(post("/auth/register"), body)), 400, field + " must not be blank");

        verifyNoInteractions(userService);
    }

    @Test
    void registerRejectsBadEmail() throws Exception {
        String body = REGISTER.replace("sam@example.com", "not-an-email");

        expectError(mockMvc.perform(json(post("/auth/register"), body)),
                400, "email must be a well-formed email address");
        verifyNoInteractions(userService);
    }

    @Test
    void registerReportsTakenUsername() throws Exception {
        when(userService.register("sam", "secret", "Sam", "Lee", "sam@example.com"))
                .thenThrow(new UsernameTakenException("boom"));

        expectError(mockMvc.perform(json(post("/auth/register"), REGISTER)), 409, "boom");
    }

    @Test
    void loginStoresUserInFreshSession() throws Exception {
        when(userService.authenticate("sam", "secret")).thenReturn(user());
        MockHttpSession previous = new MockHttpSession();

        MockHttpSession session = (MockHttpSession) mockMvc
                .perform(json(post("/auth/login"), LOGIN).session(previous))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.pass").doesNotExist())
                .andReturn().getRequest().getSession(false);

        assertNotSame(previous, session);
        assertTrue(previous.isInvalid());
        assertEquals(7, session.getAttribute("userId"));
        assertEquals("CUSTOMER", session.getAttribute("role"));
    }

    @Test
    void loginValidatesBody() throws Exception {
        expectError(mockMvc.perform(json(post("/auth/login"), "{\"username\":\"sam\"}")),
                400, "password must not be blank");
        verifyNoInteractions(userService);
    }

    @Test
    void loginReportsInvalidCredentials() throws Exception {
        BankException failure = new InvalidCredentialsException("boom");
        when(userService.authenticate("sam", "secret")).thenThrow(failure);

        expectError(mockMvc.perform(json(post("/auth/login"), LOGIN)), 401, "boom");
    }

    @Test
    void logoutInvalidatesSession() throws Exception {
        MockHttpSession session = customer();

        mockMvc.perform(post("/auth/logout").session(session))
                .andExpect(status().isNoContent());

        assertTrue(session.isInvalid());
    }

    @Test
    void logoutWithoutSessionStillSucceeds() throws Exception {
        mockMvc.perform(post("/auth/logout")).andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @CsvSource({"/auth/register", "/auth/login"})
    void rejectsMalformedJson(String url) throws Exception {
        expectError(mockMvc.perform(json(post(url), "{")), 400, "Malformed JSON request");
    }
}
