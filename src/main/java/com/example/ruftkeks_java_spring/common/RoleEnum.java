package com.example.ruftkeks_java_spring.common;

public enum RoleEnum {
    FELLOW(0, "Fellow"),
    LEADER(1, "Leader"),
    MANAGER(2,"Manager"),
    EXECUTIVE(3, "Executive"),
    ADVISOR(4,"Advisor"),
    CIO(5, "CIO"),
    COO(5,"COO"),
    CFO(5,"CFO"),
    CTO(5,"CTO"),
    CEO(6, "CEO");

    public int level;
    public String position;

    RoleEnum(int level, String position) {
        this.level = level;
        this.position = position;
    }
}
