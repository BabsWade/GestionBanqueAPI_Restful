package sn.gestionbanque.gestioncompte.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sn.gestionbanque.gestioncompte.exception.CompteInexistantException;
import sn.gestionbanque.gestioncompte.exception.SoldeInsuffisantException;
import sn.gestionbanque.gestioncompte.model.CompteBancaire;
import sn.gestionbanque.gestioncompte.model.Transaction;
import sn.gestionbanque.gestioncompte.service.CompteBancaireService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class CompteBancairetController {

    @Autowired
    private CompteBancaireService service;

    /**
     * Creates a new bank account.
     * @param account the account to be created.
     * @return the created account.
     */
    @PostMapping
    public CompteBancaire createAccount(@RequestBody CompteBancaire account) {
        return service.createAccount(account);
    }

    /**
     * Retrieves an account by its ID.
     * @param id the ID of the account.
     * @return the account.
     * @throws CompteInexistantException if no account is found with the given ID.
     */
    @GetMapping("/{id}")
    public CompteBancaire getAccount(@PathVariable Long id) {
        return service.getAccount(id);
    }

    /**
     * Retrieves the balance of an account.
     * @param id the ID of the account.
     * @return the balance of the account.
     * @throws CompteInexistantException if no account is found with the given ID.
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
     * @throws CompteInexistantException if no account is found with the given ID.
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
     * @throws CompteInexistantException if any of the accounts is not found.
     * @throws SoldeInsuffisantException if the source account has insufficient funds.
     */
    

    @PostMapping("/{id}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Long id,
                                            @RequestParam Long toAccountId,
                                            @RequestParam BigDecimal amount) {
        try {
            service.transfer(id, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (CompteInexistantException | SoldeInsuffisantException ex) {
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
    public Page<CompteBancaire> listAccounts(@RequestParam int page, @RequestParam int size) {
        return service.listAccounts(page, size);
    }

    /**
     * Deletes an account by its ID.
     * @param id the ID of the account to be deleted.
     * @throws CompteInexistantException if no account is found with the given ID.
     */
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        service.deleteAccount(id);
    }
}
