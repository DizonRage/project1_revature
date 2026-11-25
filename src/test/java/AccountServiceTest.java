import org.example.entity.Account;
import org.example.exception.AccountClosedException;
import org.example.exception.AccountNotFoundException;
import org.example.exception.InsufficientFundsException;
import org.example.exception.InvalidAmountException;
import org.example.exception.InvalidTransferException;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private static final Integer USER_ID = 7;
    private static final String ROLE = "CUSTOMER";

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;

    // deposit

    @Test
    void depositAddsAmountAndSaves() {
        Account account = stored(1, "100.00", "OPEN");
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.deposit(1, money("25.50"), USER_ID, ROLE);

        assertSame(account, result);
        assertMoney("125.50", result.getBalance());
        verify(accountRepository).save(account);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"0", "-5.00"})
    void depositRejectsInvalidAmount(String amount) {
        BigDecimal value = amount == null ? null : money(amount);

        assertThrows(InvalidAmountException.class, () -> accountService.deposit(1, value, USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void depositRejectsMissingAccount() {
        missing(1);

        assertThrows(AccountNotFoundException.class, () -> accountService.deposit(1, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void depositRejectsCancelledAccount() {
        Account account = stored(1, "100.00", "CANCELLED");

        assertThrows(AccountClosedException.class, () -> accountService.deposit(1, money("10"), USER_ID, ROLE));

        assertMoney("100.00", account.getBalance());
        verifyNothingSaved();
    }

    // withdraw

    @Test
    void withdrawSubtractsAmountAndSaves() {
        Account account = stored(1, "100.00", "OPEN");
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.withdraw(1, money("40.25"), USER_ID, ROLE);

        assertSame(account, result);
        assertMoney("59.75", result.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void withdrawAllowsEntireBalance() {
        Account account = stored(1, "100.00", "OPEN");
        when(accountRepository.save(account)).thenReturn(account);

        assertMoney("0", accountService.withdraw(1, money("100"), USER_ID, ROLE).getBalance());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"0", "-5.00"})
    void withdrawRejectsInvalidAmount(String amount) {
        BigDecimal value = amount == null ? null : money(amount);

        assertThrows(InvalidAmountException.class, () -> accountService.withdraw(1, value, USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void withdrawRejectsMissingAccount() {
        missing(1);

        assertThrows(AccountNotFoundException.class, () -> accountService.withdraw(1, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void withdrawRejectsCancelledAccount() {
        stored(1, "100.00", "CANCELLED");

        assertThrows(AccountClosedException.class, () -> accountService.withdraw(1, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void withdrawRejectsInsufficientFunds() {
        Account account = stored(1, "100.00", "OPEN");

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(1, money("100.01"), USER_ID, ROLE));

        assertMoney("100.00", account.getBalance());
        verifyNothingSaved();
    }

    // transfer

    @Test
    void transferMovesFundsAndReturnsSourceAccount() {
        Account from = stored(1, "100.00", "OPEN");
        Account to = stored(2, "50.00", "OPEN");
        when(accountRepository.save(from)).thenReturn(from);
        when(accountRepository.save(to)).thenReturn(to);

        Account result = accountService.transfer(1, 2, money("30.00"), USER_ID, ROLE);

        assertSame(from, result);
        assertMoney("70.00", from.getBalance());
        assertMoney("80.00", to.getBalance());
        verify(accountRepository).save(from);
        verify(accountRepository).save(to);
    }

    @Test
    void transferRejectsSameAccount() {
        // Distinct Integer objects outside the cache, so this fails if ids are compared with ==.
        Integer fromId = Integer.valueOf(1000);
        Integer toId = Integer.parseInt("1000");

        assertThrows(InvalidTransferException.class,
                () -> accountService.transfer(fromId, toId, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"0", "-5.00"})
    void transferRejectsInvalidAmount(String amount) {
        BigDecimal value = amount == null ? null : money(amount);

        assertThrows(InvalidAmountException.class, () -> accountService.transfer(1, 2, value, USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void transferRejectsMissingSourceAccount() {
        missing(1);

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer(1, 2, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void transferRejectsMissingDestinationAccount() {
        stored(1, "100.00", "OPEN");
        missing(2);

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer(1, 2, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void transferRejectsCancelledSourceAccount() {
        stored(1, "100.00", "CANCELLED");
        stored(2, "50.00", "OPEN");

        assertThrows(AccountClosedException.class,
                () -> accountService.transfer(1, 2, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void transferRejectsCancelledDestinationAccount() {
        stored(1, "100.00", "OPEN");
        stored(2, "50.00", "CANCELLED");

        assertThrows(AccountClosedException.class,
                () -> accountService.transfer(1, 2, money("10"), USER_ID, ROLE));

        verifyNothingSaved();
    }

    @Test
    void transferRejectsInsufficientFunds() {
        Account from = stored(1, "100.00", "OPEN");
        Account to = stored(2, "50.00", "OPEN");

        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer(1, 2, money("100.01"), USER_ID, ROLE));

        assertMoney("100.00", from.getBalance());
        assertMoney("50.00", to.getBalance());
        verifyNothingSaved();
    }

    private Account stored(int id, String balance, String status) {
        Account account = new Account(id, 1, "ACC-" + id, "CHECKING", money(balance));
        account.setStatus(status);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        return account;
    }

    private void missing(int id) {
        when(accountRepository.findById(id)).thenReturn(Optional.empty());
    }

    private void verifyNothingSaved() {
        verify(accountRepository, never()).save(any());
    }

    private static BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, money(expected).compareTo(actual), "expected " + expected + " but was " + actual);
    }
}
