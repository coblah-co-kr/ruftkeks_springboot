package com.example.ruftkeks_java_spring.mypage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyInfo {
    private String address;
    private String email;
    private String phone;
    private List<String> links;
    private Float latitude;
    private Float longitude;
}
