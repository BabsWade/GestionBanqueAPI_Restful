package com.example.demo.controller;

import com.example.demo.model.BankAccount;
import com.example.demo.model.Transaction;
import com.example.demo.service.BankAccountService;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    /**
     * Creates a new bank account.
     * @param account the account to be created.
     * @return the created account.
     */
    @PostMapping
    public BankAccount createAccount(@RequestBody BankAccount account) {
        return service.createAccount(account);
    }

    /**
     * Retrieves an account by its ID.
     * @param id the ID of the account.
     * @return the account.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    @GetMapping("/{id}")
    public BankAccount getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }

    /**
     * Retrieves the balance of an account.
     * @param id the ID of the account.
     * @return the balance of the account.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    @GetMapping("/{id}/balance")
    public BigDecimal getAccountBalance(@PathVariable Long id) {
        return service.getAccountBalance(id);
    }

    /**
     * Retrieves the transaction history of an account with pagination.
     * @param id the ID of the account.
     * @param page the page number (0-based).
     * @param size the size of the page.
     * @return a page of transactions.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    @GetMapping("/{id}/transactions")
    public Page<Transaction> getAccountTransactions(@PathVariable Long id,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return service.getAccountTransactions(id, page, size);
    }

    /**
     * Performs a transfer between two accounts.
     * @param fromAccountId the ID of the account from which the amount will be transferred.
     * @param toAccountId the ID of the account to which the amount will be transferred.
     * @param amount the amount to be transferred.
     * @throws AccountNotFoundException if any of the accounts is not found.
     * @throws InsufficientFundsException if the source account has insufficient funds.
     */
    

    @PostMapping("/{id}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Long id,
                                            @RequestParam Long toAccountId,
                                            @RequestParam BigDecimal amount) {
        try {
            service.transfer(id, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (AccountNotFoundException | InsufficientFundsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Lists all bank accounts with pagination.
     * @param page the page number (0-based).
     * @param size the size of the page.
     * @return a page of bank accounts.
     */
    @GetMapping
    public Page<BankAccount> listAccounts(@RequestParam int page, @RequestParam int size) {
        return service.listAccounts(page, size);
    }

    /**
     * Deletes an account by its ID.
     * @param id the ID of the account to be deleted.
     * @throws AccountNotFoundException if no account is found with the given ID.
     */
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
    }
}
