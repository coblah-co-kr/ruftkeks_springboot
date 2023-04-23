package com.example.ruftkeks_java_spring.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private EducationRepository educationRepository;

    private final AccountService accountService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody SignUp signup) {
        // Should encrypt the password for hide in database;
        String nickname = signup.getNickname();
        String name = signup.getName();
        String password = signup.getPassword();
        if (nickname == null) {
            return new ResponseEntity<>("정보를 입력해 주세요.", HttpStatus.BAD_REQUEST);
        }
        Account account = this.accountRepository.findByNickname(nickname);
        if (account != null) {
            return new ResponseEntity<>("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }
        accountService.create(nickname, name, password);
        return new ResponseEntity<>("회원가입이 신청되었습니다. 관리자의 승인을 기다려주세요.",HttpStatus.CREATED);
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestBody SignIn signin) {
        String nickname = signin.getNickname();
        String password = signin.getPassword();

        Account account = accountService.login(nickname, password);
        if (account==null) {
            return new ResponseEntity<>("닉네임과 비밀번호가 일치하지 않습니다.",HttpStatus.UNAUTHORIZED);
        }
        else if (account.isActive()) {
            return new ResponseEntity(account, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("귀하의 계정을 관리자가 검토중입니다.",HttpStatus.FORBIDDEN);
        }
    }

}
