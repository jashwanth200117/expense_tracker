package com.expense.tracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracker.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Get total spending by category for a specific user.
     * @param userId - ID of the user.
     * @return List of categories with total spending.
     */
    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    @GetMapping("/category/{userId}")
    public List<Map<String, Object>> getTotalSpendingByCategory(@PathVariable Long userId) {
        return reportService.getTotalSpendingByCategory(userId);
    }

    /**
     * Get monthly spending trends for a specific user.
     * @param userId - ID of the user.
     * @return List of monthly spending totals.
     */
    @GetMapping("/monthly/{userId}")
    public List<Map<String, Object>> getMonthlySpendingTrends(@PathVariable Long userId) {
        return reportService.getMonthlySpendingTrends(userId);
    }

    /**
     * Get spending by category within a date range for a specific user.
     * @param userId - ID of the user.
     * @param startDate - Start date of the range (format: yyyy-MM-dd).
     * @param endDate - End date of the range (format: yyyy-MM-dd).
     * @return List of categories with spending totals within the range.
     */
    @GetMapping("/date-range/{userId}")
    public List<Map<String, Object>> getSpendingByCategoryAndDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return reportService.getSpendingByCategoryAndDateRange(userId, startDate, endDate);
    }
}
