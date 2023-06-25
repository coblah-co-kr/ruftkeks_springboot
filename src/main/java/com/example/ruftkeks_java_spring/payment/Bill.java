package com.example.ruftkeks_java_spring.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Bill {
    private String year;
    private List<List<String>> payments;
}
