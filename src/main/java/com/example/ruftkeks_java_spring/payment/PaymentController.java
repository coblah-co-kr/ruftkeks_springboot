package com.example.ruftkeks_java_spring.payment;

import com.example.ruftkeks_java_spring.account.AccountRepository;
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
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private final PaymentService paymentService;

    @RequestMapping(value = "/make", method = RequestMethod.POST)
    public ResponseEntity makeBill(@RequestBody Bill bill) {
        String year = bill.getYear();
        List<List<String>> payments = bill.getPayments();

        Payment payment = this.paymentRepository.findAllByYear(year);

        if (payment!=null) {
            paymentService.update(year, payments);
            return new ResponseEntity("회비내역을 수정했습니다.", HttpStatus.OK);
        }

        paymentService.create(year, payments);
        return new ResponseEntity("회비내역이 생성되었습니다.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateBill(@RequestBody Bill bill) {
        String year = bill.getYear();
        List<List<String>> payments = bill.getPayments();

        paymentService.update(year, payments);
        return new ResponseEntity("회비내역을 수정했습니다.", HttpStatus.OK);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity getBillAllByYear(@RequestParam(value = "year") String year) {
        Payment payment = this.paymentRepository.findAllByYear(year);
        if (payment==null) {
            return new ResponseEntity("요청하신 데이터가 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(payment, HttpStatus.OK);
    }
}
