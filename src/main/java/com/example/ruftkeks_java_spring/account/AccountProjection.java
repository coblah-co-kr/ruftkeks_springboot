package com.example.ruftkeks_java_spring.account;

import java.util.List;

public interface AccountProjection {
    String getNickname();
    String getName();
    String getEmail();
    Float getLongitude();
    Float getLatitude();
    Float getLastLatitude();
    Float getLastLongitude();
    List<String> getLinks();
    String getPhone();
    String getProfileImg();
    String getOverviewImg();
    String getAddress();
    String getRole();
    String getLastIp();
    boolean getActive();
}
