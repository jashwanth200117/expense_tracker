package com.expense.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracker.entity.Transaction;
import com.expense.tracker.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Create a new transaction
    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        transactionService.createTransaction(transaction);
        return ResponseEntity.ok("Transaction created successfully!");
    }

    // Get all transactions for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(transactions);
    }

    // Update a transaction
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        boolean isUpdated = transactionService.updateTransaction(id, updatedTransaction);
        if (isUpdated) {
            return ResponseEntity.ok("Transaction updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Transaction not found.");
        }
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        boolean isDeleted = transactionService.deleteTransaction(id);
        if (isDeleted) {
            return ResponseEntity.ok("Transaction deleted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Transaction not found.");
        }
    }


}
