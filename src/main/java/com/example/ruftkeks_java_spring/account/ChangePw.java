package com.example.ruftkeks_java_spring.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePw {
    private String password;
    private String newPassword;
}
