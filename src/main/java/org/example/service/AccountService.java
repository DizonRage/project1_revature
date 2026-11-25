package org.example.service;

import org.example.entity.Account;
import org.example.exception.AccountClosedException;
import org.example.exception.AccountNotFoundException;
import org.example.exception.InsufficientFundsException;
import org.example.exception.InvalidAmountException;
import org.example.exception.InvalidTransferException;
import org.example.repository.AccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account deposit(Integer accountId, BigDecimal amount) {
        requirePositive(amount);
        Account account = findAccount(accountId);
        requireNotCancelled(account);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    public Account withdraw(Integer accountId, BigDecimal amount) {
        requirePositive(amount);
        Account account = findAccount(accountId);
        requireNotCancelled(account);
        requireSufficientFunds(account, amount);
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }

    @Transactional
    public Account transfer(Integer fromId, Integer toId, BigDecimal amount) {
        if (fromId.equals(toId)) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }
        requirePositive(amount);
        Account from = findAccount(fromId);
        Account to = findAccount(toId);
        requireNotCancelled(from);
        requireNotCancelled(to);
        requireSufficientFunds(from, amount);
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        Account savedFrom = accountRepository.save(from);
        accountRepository.save(to);
        return savedFrom;
    }

    public int addAccount(Account account, int applicationId) {
        account.setApplicationId(applicationId);
        return accountRepository.save(account).getAccountId();
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account getAccountByNumber(String number) {
        return accountRepository.findByAccountNumber(number).orElse(null);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll(Sort.by("accountId"));
    }

    // Copies fields onto the stored row so that its status is preserved.
    public boolean updateAccount(Account account, int id) {
        return accountRepository.findById(id).map(existing -> {
            existing.setApplicationId(account.getApplicationId());
            existing.setAccountNumber(account.getAccountNumber());
            existing.setAccountType(account.getAccountType());
            existing.setBalance(account.getBalance());
            accountRepository.save(existing);
            return true;
        }).orElse(false);
    }

    public boolean removeAccount(int id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("amount must be greater than 0");
        }
    }

    private Account findAccount(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account " + accountId + " not found"));
    }

    private void requireNotCancelled(Account account) {
        if ("CANCELLED".equals(account.getStatus())) {
            throw new AccountClosedException("Account " + account.getAccountId() + " is closed");
        }
    }

    private void requireSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account " + account.getAccountId());
        }
    }
}
