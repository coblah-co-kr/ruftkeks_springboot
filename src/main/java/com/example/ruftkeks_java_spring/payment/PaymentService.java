package com.example.ruftkeks_java_spring.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment create(String year, List<List<String>> payments) {
        Payment payment = Payment.builder()
                .year(year)
                .payments(payments)
                .build();
        this.paymentRepository.save(payment);
        return payment;
    }

    public Payment update(String year, List<List<String>> payments) {
        Payment payment = this.paymentRepository.findAllByYear(year);
        payment.updatePaymentInfo(payments);
        this.paymentRepository.save(payment);
        return payment;
    }
}
