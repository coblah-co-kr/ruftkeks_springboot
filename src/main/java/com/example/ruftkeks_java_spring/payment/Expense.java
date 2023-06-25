package com.example.ruftkeks_java_spring.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;
    private String month;

    private String date;
    private String location;
    private String members;

    @ElementCollection
    private List<List<String>> expenses;

    @Builder
    public Expense(String year, String month,
                   String date, String location, String members,
                   List<List<String>> expenses) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.location = location;
        this.members = members;
        this.expenses = expenses;
    }

    public Expense updateExpenseInfo(
            String year, String month,
            String date, String location, String members,
            List<List<String>> expenses) {
        this.date = date;
        this.location = location;
        this.members = members;
        this.expenses = expenses;
        return this;
    }
}
