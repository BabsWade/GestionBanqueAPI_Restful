package sn.gestionbanque.gestioncompte.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sn.gestionbanque.gestioncompte.exception.CompteInexistantException;
import sn.gestionbanque.gestioncompte.exception.SoldeInsuffisantException;
import sn.gestionbanque.gestioncompte.model.CompteBancaire;
import sn.gestionbanque.gestioncompte.model.Transaction;
import sn.gestionbanque.gestioncompte.repository.CompteBancaireRepository;
import sn.gestionbanque.gestioncompte.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CompteBancaireService {

    @Autowired
    private CompteBancaireRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Creates a new bank account.
     * @param account the account to be created.
     * @return the created account.
     */
    public CompteBancaire createAccount(CompteBancaire account) {
        return repository.save(account);
    }

    /**
     * Retrieves an account by its ID.
     * @param id the ID of the account.
     * @return the account.
     * @throws CompteInexistantException if no account is found with the given ID.
     */
    public CompteBancaire getAccount(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CompteInexistantException("Account not found with id: " + id));
    }

    /**
     * Lists all bank accounts with pagination.
     * @param page the page number (0-based).
     * @param size the size of the page.
     * @return a page of bank accounts.
     */
    public Page<CompteBancaire> listAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    /**
     * Deletes an account by its ID.
     * @param id the ID of the account to be deleted.
     */
    public void deleteAccount(Long id) {
        if (!repository.existsById(id)) {
            throw new CompteInexistantException("Account not found with id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Retrieves the balance of an account.
     * @param accountId the ID of the account.
     * @return the balance of the account.
     * @throws CompteInexistantException if no account is found with the given ID.
     */
    public BigDecimal getAccountBalance(Long accountId) {
        CompteBancaire account = getAccount(accountId);
        return account.getBalance();
    }

    /**
     * Retrieves the transaction history of an account with pagination.
     * @param accountId the ID of the account.
     * @param page the page number (0-based).
     * @param size the size of the page.
     * @return a page of transactions.
     * @throws CompteInexistantException if no account is found with the given ID.
     */
    public Page<Transaction> getAccountTransactions(Long accountId, int page, int size) {
        getAccount(accountId); // Ensure the account exists
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByAccountId(accountId, pageable);
    }

    /**
     * Performs a transfer between two accounts.
     * @param fromAccountId the ID of the account from which the amount will be transferred.
     * @param toAccountId the ID of the account to which the amount will be transferred.
     * @param amount the amount to be transferred.
     * @throws CompteInexistantException if any of the accounts is not found.
     * @throws SoldeInsuffisantException if the source account has insufficient funds.
     */
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        CompteBancaire fromAccount = repository.findById(fromAccountId)
                .orElseThrow(() -> new CompteInexistantException("Account not found with id: " + fromAccountId));
        CompteBancaire toAccount = repository.findById(toAccountId)
                .orElseThrow(() -> new CompteInexistantException("Account not found with id: " + toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new SoldeInsuffisantException("Insufficient funds in account with id: " + fromAccountId);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        repository.save(fromAccount);
        repository.save(toAccount);

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setAmount(amount.negate()); // Debit from source account
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        Transaction receivedTransaction = new Transaction();
        receivedTransaction.setAccount(toAccount);
        receivedTransaction.setAmount(amount); // Credit to destination account
        receivedTransaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(receivedTransaction);
    }

    /**
     * Records a transaction in the transaction repository.
     * @param account the account associated with the transaction.
     * @param amount the amount of the transaction.
     */
    private void recordTransaction(CompteBancaire account, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
