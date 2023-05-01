package com.example.ruftkeks_java_spring.account;

import com.example.ruftkeks_java_spring.common.JwtTokenInfo;
import com.example.ruftkeks_java_spring.common.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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

    public Account login(String nickname, String password) {
        Optional<Account> account = this.accountRepository.findByNickname(nickname);

        if (!account.isPresent()) {
            return null;
        }
        if (passwordEncoder.matches(password, account.get().getPassword())) {
            // 인증
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
}
