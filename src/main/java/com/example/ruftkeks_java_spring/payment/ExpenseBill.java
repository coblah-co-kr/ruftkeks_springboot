package com.example.ruftkeks_java_spring.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseBill {
    private String year;
    private String month;
    private String date;
    private String location;
    private String members;
    private List<List<String>> expenses;
}
