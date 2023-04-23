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
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 20)
    private String institution;

    @Column(length = 20)
    private String major;

    @Column(columnDefinition = "TEXT")
    private String achievement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public Education(
            LocalDate startDate, LocalDate endDate,
            String institution, String major,
            String achievement
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.institution = institution;
        this.major = major;
        this.achievement = achievement;
    }

    public void addAccount(Account account) {
        if (this.account != null) {
            this.account.getEducations().remove(this);
        }
        this.account = account;
        account.getEducations().add(this);
    }
}
