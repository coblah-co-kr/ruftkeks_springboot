package com.example.ruftkeks_java_spring.account;

import com.example.ruftkeks_java_spring.common.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String nickname;

    @Column(length = 5)
    private String name;

    @Column(length = 20)
    private String email;

    @Column(length = 50)
    private String link;

    @Column(length = 15)
    private String phone;

    private String picture;
    private String address;

    @Column(length = 10)
    private String role;

    private String password;

    private boolean active;

    @ElementCollection
    private Set<String> skills = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Education> educations = new ArrayList<>();

    public Account update(
            String email, String link,
            String picture, String address,
            String phone
    ) {
        this.email = email;
        this.link = link;
        this.picture = picture;
        this.address = address;
        this.phone = phone;
        return this;
    }

    public Account changePassword(String password) {
        this.password = password;
        return this;
    }

    @Builder
    public Account(String nickname, String password, String name) {
        this.nickname = nickname;
        this.name = name;
        this.role = RoleEnum.FELLOW.position;
        this.active = false;
    }

    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void removeSkill(String skill) {
        skills.remove(skill);
    }
}
