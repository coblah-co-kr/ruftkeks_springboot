package com.example.ruftkeks_java_spring.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Expense findExpenseByYearAndMonth(String year, String month);
}
