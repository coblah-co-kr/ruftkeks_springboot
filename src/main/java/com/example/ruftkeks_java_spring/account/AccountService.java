package com.example.ruftkeks_java_spring.account;

import com.example.ruftkeks_java_spring.common.JwtTokenInfo;
import com.example.ruftkeks_java_spring.common.JwtTokenProvider;
import com.example.ruftkeks_java_spring.common.TokenEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    public Account create(String nickname, String name, String password) {
        Account account = Account.builder()
                .name(name)
                .nickname(nickname)
                .build();
        account.changePassword(passwordEncoder.encode(password));
        this.accountRepository.save(account);
        return account;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Account updatePassword(
            String nickname,
            String presentPassword,
            String wantedPassword
    ) {
        Optional<Account> account = this.accountRepository.findByNickname(nickname);

        if (!account.isPresent()) {
            return null;
        }
        if (passwordEncoder.matches(presentPassword, account.get().getPassword())) {
            Account account1 = account.get();
            account1.changePassword(passwordEncoder.encode(wantedPassword));
            this.accountRepository.save(account1);
            return account1;
        }
        else {
            return null;
        }
    }

    public Account login(String nickname, String password) {
        Optional<Account> account = this.accountRepository.findByNickname(nickname);

        if (!account.isPresent()) {
            return null;
        }
        if (passwordEncoder.matches(password, account.get().getPassword())) {
            return account.get();
        }
        else {
            return null;
        }
    }

    @Transactional
    public JwtTokenInfo publishAccessToken(String nickname, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtTokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    public String tokenValidationResult(String accessToken) {
        return jwtTokenProvider.validateTokenDetail(accessToken.substring(7));
    }

    public JwtTokenInfo tokenRenewResult(
            String accessToken, String refreshToken, HttpServletRequest request
    ) {
        String validatedAccessToken = jwtTokenProvider.validateTokenDetail(accessToken);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return JwtTokenInfo.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Optional<Account> account = this.accountRepository.findByNickname(authentication.getName());
        if (!account.isPresent()) {
            return JwtTokenInfo.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        if (!account.get().getLastIp().equals(getClientIp(request))) {
            return JwtTokenInfo.builder()
                    .grantType("Bearer")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        if (validatedAccessToken.startsWith(TokenEnum.OK.text)) {
            if (validatedAccessToken.split(TokenEnum.OK.text).length==0) {
                return JwtTokenInfo.builder()
                        .grantType("Bearer")
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
            if (Integer.parseInt(validatedAccessToken.split(TokenEnum.OK.text)[1]) < 10) {
                // 유저 이메일, 연락처로 안내메세지 발송
                return jwtTokenProvider.generateToken(authentication);
            }
        } else if (validatedAccessToken.startsWith(TokenEnum.EXPIRED.text)) {
            return jwtTokenProvider.generateToken(authentication);
        }
        return JwtTokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = null;
        boolean isIpInHeader = false;

        List<String> headerList = new ArrayList<>();
        headerList.add("X-Forwarded-For");
        headerList.add("HTTP_CLIENT_IP");
        headerList.add("HTTP_X_FORWARDED_FOR");
        headerList.add("HTTP_X_FORWARDED");
        headerList.add("HTTP_FORWARDED_FOR");
        headerList.add("HTTP_FORWARDED");
        headerList.add("Proxy-Client-IP");
        headerList.add("WL-Proxy-Client-IP");
        headerList.add("HTTP_VIA");
        headerList.add("IPV6_ADR");

        for (String header: headerList) {
            clientIp = request.getHeader(header);
            if (StringUtils.hasText(clientIp) && !clientIp.equals("unknown")) {
                isIpInHeader = true;
                break;
            }
        }

        if (!isIpInHeader) {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    public Optional<Account> getMyInfo(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken.substring(7));
        return this.accountRepository.findByNickname(authentication.getName());
    }

    public List<AccountProjection> getAllInfo(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken.substring(7));
        return this.accountRepository.findAllBy();
    }
}
