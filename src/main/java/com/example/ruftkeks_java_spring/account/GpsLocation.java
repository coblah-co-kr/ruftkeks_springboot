package com.example.ruftkeks_java_spring.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GpsLocation {
    private Float lastLatitude;
    private Float lastLongitude;
}
