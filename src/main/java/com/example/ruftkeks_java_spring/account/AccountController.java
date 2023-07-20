package com.example.ruftkeks_java_spring.account;

import com.example.ruftkeks_java_spring.common.JwtTokenInfo;
import com.example.ruftkeks_java_spring.common.RenewTokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
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
import java.util.List;
import java.util.Optional;

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
        if (this.accountRepository.findByNickname(nickname).isPresent()) {
            return new ResponseEntity<>("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }
        accountService.create(nickname, name, password);

        return new ResponseEntity<>("회원가입이 신청되었습니다. 관리자의 승인을 기다려주세요.",HttpStatus.CREATED);
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity signIn(@RequestBody SignIn signin, HttpServletRequest request) {
        String nickname = signin.getNickname();
        String password = signin.getPassword();

        Account account = accountService.login(nickname, password);
        if (account==null) {
            return new ResponseEntity<>("계정이 존재하지 않거나, 닉네임과 비밀번호가 일치하지 않습니다.",HttpStatus.UNAUTHORIZED);
        }
        else if (account.isActive()) {
            JwtTokenInfo tokenInfo = accountService.publishAccessToken(nickname, password);
            account.updateLoginInfo(
                    accountService.getClientIp(request),
                    tokenInfo.getRefreshToken()
            );
            this.accountRepository.save(account);
            return new ResponseEntity<>(
                    tokenInfo,
                    HttpStatus.OK
            );
        }
        else {
            return new ResponseEntity<>("귀하의 계정을 관리자가 검토중입니다.",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/token/validate", method = RequestMethod.GET)
    public ResponseEntity validateToken(@RequestHeader HttpHeaders header) {
        String accessToken = header.getFirst("Authorization");
        return new ResponseEntity<>(accountService.tokenValidationResult(accessToken), HttpStatus.OK);
    }

    @RequestMapping(value = "/token/renew", method = RequestMethod.POST)
    public ResponseEntity renewToken(
            @RequestHeader HttpHeaders header, @RequestBody RenewTokenInfo tokenInfo,
            HttpServletRequest request
    ) {
        String accessToken = header.getFirst("Authorization").substring(7);
        JwtTokenInfo renewToken = accountService.tokenRenewResult(accessToken, tokenInfo.getRefreshToken(), request);
        if (renewToken.getAccessToken() != accessToken) {
            return new ResponseEntity<>(renewToken, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(renewToken, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity userInfo(@RequestHeader HttpHeaders header) {
        String token = header.getFirst("Authorization");
        Optional<Account> account = accountService.getMyInfo(token);
        if (account.isPresent()) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/make/temp_pw", method = RequestMethod.PUT)
    public ResponseEntity makePw(@RequestHeader HttpHeaders header,
                                   @RequestBody String password) {
        String token = header.getFirst("Authorization");
        Optional<Account> account = accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(accountService.encodePassword(password), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/change_pw", method = RequestMethod.PUT)
    public ResponseEntity changePw(@RequestHeader HttpHeaders header,
                                   @RequestBody ChangePw changePw) {
        String password = changePw.getPassword();
        String newPassword = changePw.getNewPassword();
        String token = header.getFirst("Authorization");
        Optional<Account> account = accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String nickname = account.get().getNickname();
        Account account1 = accountService.updatePassword(nickname, password, newPassword);
        if (account1==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(account1, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update/gps", method = RequestMethod.PUT)
    public ResponseEntity updateGps(@RequestHeader HttpHeaders header,
                                    @RequestBody GpsLocation gpsLocation) {
        Float lastLatitude = gpsLocation.getLastLatitude();
        Float lastLongitude = gpsLocation.getLastLongitude();
        String token = header.getFirst("Authorization");
        Optional<Account> account = accountService.getMyInfo(token);
        if (account.isPresent()) {
            Account account1 = account.get();
            account1.updateLastGPS(lastLatitude, lastLongitude);
            this.accountRepository.save(account1);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity allUserInfo(@RequestHeader HttpHeaders header) {
        String token = header.getFirst("Authorization");
        return new ResponseEntity<>(accountService.getAllInfo(token), HttpStatus.OK);
    }
}
