package com.example.ruftkeks_java_spring.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenewTokenInfo {
    private String refreshToken;
}
