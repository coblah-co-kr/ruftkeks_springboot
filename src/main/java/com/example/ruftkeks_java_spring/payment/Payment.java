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
public class Payment {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String year;

    @ElementCollection
    private List<List<String>> payments;

    @Builder
    public Payment(String year, List<List<String>> payments) {
        this.year = year;
        this.payments = payments;
    }

    public Payment updatePaymentInfo(List<List<String>> payments) {
        this.payments = payments;
        return this;
    }
}
