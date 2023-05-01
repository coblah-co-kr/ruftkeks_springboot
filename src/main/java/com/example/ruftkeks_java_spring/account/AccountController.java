package com.example.ruftkeks_java_spring.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Value("${jwt.token.secret}")
    private String secretKey;

    private long accessTokenExpiredTimeMs = 1000*60*10;
    private long refreshTokenExpiredTimeMs = 1000*60*60;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody SignUp signup) {
        // Should encrypt the password for hide in database;
        String nickname = signup.getNickname();
        String name = signup.getName();
        String password = signup.getPassword();
        if (nickname == null) {
            return new ResponseEntity<>("정보를 입력해 주세요.", HttpStatus.BAD_REQUEST);
        }
        if (this.accountRepository.findByNickname(nickname).isPresent()) {
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
            return new ResponseEntity<>("계정이 존재하지 않거나, 닉네임과 비밀번호가 일치하지 않습니다.",HttpStatus.UNAUTHORIZED);
        }
        else if (account.isActive()) {
            return new ResponseEntity<>(
                    accountService.publishAccessToken(nickname, password),
                    HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>("귀하의 계정을 관리자가 검토중입니다.",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public ResponseEntity userInfo(@RequestHeader HttpHeaders header) {
        /*String token = header.getFirst("Authorization");
        System.out.println(JwtUtil.validateToken(token, secretKey));*/
        return new ResponseEntity<>("sad",HttpStatus.OK);
    }

}
