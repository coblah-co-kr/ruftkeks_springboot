package com.example.ruftkeks_java_spring.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense create(String year, String month,
                          String date, String location, String members,
                          List<List<String>> expenses) {
        Expense expense = Expense.builder()
                .year(year)
                .month(month)
                .date(date)
                .location(location)
                .members(members)
                .expenses(expenses)
                .build();
        this.expenseRepository.save(expense);
        return expense;
    }

    public Expense update(String year, String month,
                          String date, String location, String members,
                          List<List<String>> expenses) {
        Expense expense = this.expenseRepository.findExpenseByYearAndMonth(year, month);
        expense.updateExpenseInfo(year, month, date, location, members, expenses);
        this.expenseRepository.save(expense);
        return expense;
    }
}
