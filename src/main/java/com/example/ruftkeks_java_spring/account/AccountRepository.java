package com.example.ruftkeks_java_spring.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNickname(String nickname);
}
