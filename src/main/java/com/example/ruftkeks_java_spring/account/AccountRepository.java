package com.example.ruftkeks_java_spring.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    //Account findByNickname(String nickname);
    Optional<Account> findByNickname(String nickname);
}