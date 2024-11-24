package com.expense.tracker.service;

import com.expense.tracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private final TransactionRepository transactionRepository;

    // Constructor for dependency injection
    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Get total spending by category for a specific user.
     * @param userId - ID of the user.
     * @return List of categories with total spending.
     */
    public List<Map<String, Object>> getTotalSpendingByCategory(Long userId) {
        List<Object[]> results = transactionRepository.findTotalSpendingByCategory(userId);
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("total", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * Get monthly spending trends for a specific user.
     * @param userId - ID of the user.
     * @return List of monthly spending totals.
     */
    public List<Map<String, Object>> getMonthlySpendingTrends(Long userId) {
        List<Object[]> results = transactionRepository.findMonthlySpendingTrends(userId);
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("month", row[0]);
            map.put("total", row[1]);
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * Get spending by category within a date range for a specific user.
     * @param userId - ID of the user.
     * @param startDate - Start date of the range.
     * @param endDate - End date of the range.
     * @return List of categories with spending totals within the range.
     */
    public List<Map<String, Object>> getSpendingByCategoryAndDateRange(Long userId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactionRepository.findSpendingByCategoryAndDateRange(userId, start, end);
    }
}
