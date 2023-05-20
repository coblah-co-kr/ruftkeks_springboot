package com.example.ruftkeks_java_spring.account;

import com.example.ruftkeks_java_spring.common.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Float longitude;
    private Float latitude;

    @ElementCollection
    private List<String> links;

    @Column(length = 15)
    private String phone;

    private String profileImg;
    private String overviewImg;
    private String address;

    @Column(length = 10)
    private String role;

    @JsonIgnore
    private String password;

    private String lastIp;

    private String refreshToken;

    private boolean active;

    @ElementCollection
    private Set<String> skills = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Education> educations = new ArrayList<>();

    public Account updateBase(
            String email, String address, String phone, List<String> links,
            Float longitude, Float latitude
    ) {
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.links = links;
        this.longitude = longitude;
        this.latitude = latitude;
        return this;
    }

    public Account changePassword(String password) {
        this.password = password;
        return this;
    }

    public Account updateLoginInfo(String ClientIp, String refreshToken) {
        this.lastIp = ClientIp;
        this.refreshToken = refreshToken;
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
