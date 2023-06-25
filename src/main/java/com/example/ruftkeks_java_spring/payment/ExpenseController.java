package com.example.ruftkeks_java_spring.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    private final ExpenseService expenseService;

    @RequestMapping(value = "/make", method = RequestMethod.POST)
    public ResponseEntity makeExpense(@RequestBody ExpenseBill expenseBill) {
        String year = expenseBill.getYear();
        String month = expenseBill.getMonth();
        String date = expenseBill.getDate();
        String location = expenseBill.getLocation();
        String members = expenseBill.getMembers();
        List<List<String>> expenses = expenseBill.getExpenses();

        Expense expense = this.expenseRepository.findExpenseByYearAndMonth(year, month);

        if (expense!=null) {
            expenseService.update(year, month,
                    date, location, members, expenses);
            return new ResponseEntity("지출내역을 수정했습니다.", HttpStatus.OK);
        }

        expenseService.create(year, month,
                date, location, members, expenses);
        return new ResponseEntity("지출내역이 생성되었습니다.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateExpense(@RequestBody ExpenseBill expenseBill) {
        String year = expenseBill.getYear();
        String month = expenseBill.getMonth();
        String date = expenseBill.getDate();
        String location = expenseBill.getLocation();
        String members = expenseBill.getMembers();
        List<List<String>> expenses = expenseBill.getExpenses();

        expenseService.update(year, month,
                date, location, members, expenses);
        return new ResponseEntity("지출내역을 수정했습니다.", HttpStatus.OK);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity getExpenseByYearAndMonth(
            @RequestParam(value = "year") String year,
            @RequestParam(value = "month") String month) {
        Expense expense = this.expenseRepository.findExpenseByYearAndMonth(year, month);
        if (expense==null) {
            return new ResponseEntity("요청하신 데이터가 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(expense, HttpStatus.OK);
    }
}
