package com.example.ruftkeks_java_spring.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
    private final long expireTimeMs = 1000 * 60 * 60 * 24; // 1d

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
        Account account = this.accountRepository.findByNickname(nickname);

        if (account==null) {
            return null;
        }
        if (passwordEncoder.matches(password, account.getPassword())) {
            return account;
        }
        else {
            return null;
        }
    }
}
