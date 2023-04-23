package com.example.ruftkeks_java_spring;

import com.example.ruftkeks_java_spring.account.Account;
import com.example.ruftkeks_java_spring.account.AccountRepository;
import com.example.ruftkeks_java_spring.account.Education;
import com.example.ruftkeks_java_spring.account.EducationRepository;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
class RuftkeksJavaSpringApplicationTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private EducationRepository educationRepository;

	/*

	@Test
	void testJpaDelete() {
		Account account = this.accountRepository.findByNickname("Grant");
		this.accountRepository.delete(account);
	}

	 */
}

	/*
	@Test
	void testJpaCreate() {
		Account a1 = new Account("Grant", "김용균");
		this.accountRepository.save(a1);
	}

	@Test
	void testJpaUpdate() {
		Account account = this.accountRepository.findByNickname("Grant");
		account.update("grant@coblah.co.kr", "https://github.com/modec28", null, "경기도 하남시 미사강변중앙로31번길 15", "010-4141-3783");
		this.accountRepository.save(account);
	}

	@Test
	void testJpaAddEdu() {
		Account account = this.accountRepository.findByNickname("Grant");
		Education education = Education.builder()
				.startDate(LocalDate.of(2013,03,01))
				.endDate(LocalDate.of(2019,02,01))
				.institution("한국항공대학교")
				.major("항공전자정보공학부")
				.achievement(null)
				.build();
		education.addAccount(account);
		this.educationRepository.save(education);
	}
}
	 */


