package com.expense.tracker.service;

import com.expense.tracker.entity.Transaction;
import com.expense.tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Constructor for dependency injection
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create a new transaction
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    // Get all transactions for a specific user
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Update an existing transaction
    public boolean updateTransaction(Long id, Transaction updatedTransaction) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            transaction.setType(updatedTransaction.getType());
            transaction.setCategory(updatedTransaction.getCategory());
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setDate(updatedTransaction.getDate());
            transactionRepository.save(transaction);
            return true;
        }
        return false;
    }

    // Delete a transaction
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
