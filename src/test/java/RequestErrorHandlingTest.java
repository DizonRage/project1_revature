import org.example.controller.AccountController;
import org.example.controller.TransferController;
import org.example.exception.GlobalExceptionHandler;
import org.example.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestErrorHandlingTest {

    @Mock
    AccountService accountService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AccountController(accountService), new TransferController(accountService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "/accounts/1/deposit  | {\"amount\": 0.005}",
            "/accounts/1/withdraw | {\"amount\": 0.005}",
            "/transfers           | {\"fromAccountId\": 1, \"toAccountId\": 2, \"amount\": 0.005}"
    })
    void rejectsSubCentAmount(String url, String body) throws Exception {
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("amount numeric value out of bounds (<13 digits>.<2 digits> expected)"));

        verifyNoInteractions(accountService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/accounts/1/deposit", "/accounts/1/withdraw", "/transfers"})
    void rejectsMalformedJson(String url) throws Exception {
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content("{\"amount\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));

        verifyNoInteractions(accountService);
    }
}
