package com.expense.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense.tracker.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t.category, SUM(t.amount) as total FROM Transaction t WHERE t.user.id = :userId GROUP BY t.category")
    List<Object[]> findTotalSpendingByCategory(@Param("userId") Long userId);

    @Query("SELECT MONTH(t.date) as month, SUM(t.amount) as total FROM Transaction t WHERE t.user.id = :userId GROUP BY MONTH(t.date) ORDER BY month ASC")
    List<Object[]> findMonthlySpendingTrends(@Param("userId") Long userId);

    @Query("SELECT t.category AS category, SUM(t.amount) AS total " +
           "FROM Transaction t " +
           "WHERE t.user.id = :userId AND t.date BETWEEN :startDate AND :endDate " +
           "GROUP BY t.category")
    List<Map<String, Object>> findSpendingByCategoryAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
