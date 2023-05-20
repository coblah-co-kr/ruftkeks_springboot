package com.example.ruftkeks_java_spring.mypage;

import com.example.ruftkeks_java_spring.account.Account;
import com.example.ruftkeks_java_spring.account.AccountRepository;
import com.example.ruftkeks_java_spring.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/my-page")
public class MyPageController {

    @Autowired
    private AccountRepository accountRepository;

    private final AccountService accountService;

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateMyBaseInfo(@RequestHeader HttpHeaders headers, @RequestBody MyInfo myInfo) {
        String token = headers.getFirst("Authorization");
        Optional<Account> account = this.accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account account1 = account.get();
        account1.updateBase(
                myInfo.getEmail(), myInfo.getAddress(),
                myInfo.getPhone(), myInfo.getLinks(),
                myInfo.getLongitude(), myInfo.getLatitude()
        );
        this.accountRepository.save(account1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 프사는 put을 쓸건데, 프론트에서 백엔드로 파일을 보내야할듯.
}
