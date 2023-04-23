package com.example.ruftkeks_java_spring.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 20)
    private String corpName;

    @Column(length = 20)
    private String department;

    @Column(length = 10)
    private String position;

    @Column(columnDefinition = "TEXT")
    private String achievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public Career(
            LocalDate startDate, LocalDate endDate,
            String corpName, String department,
            String position, String achievement
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.corpName = corpName;
        this.department = department;
        this.position = position;
        this.achievement = achievement;
    }

    public void addAccount(Account account) {
        if (this.account != null) {
            this.account.getEducations().remove(this);
        }
        this.account = account;
        account.getCareers().add(this);
    }
}
