package com.example.ruftkeks_java_spring.common;

public enum RoleEnum {
    FELLOW(0, "Fellow"),
    LEADER(1, "Leader"),
    MANAGER(2,"Manager"),
    EXECUTIVE(3, "Executive"),
    ADVISOR(4,"Advisor"),
    CIO(5, "Chief Information Officer"),
    COO(5,"Chief Operating Officer"),
    CFO(5,"Chief Financial Officer"),
    CEO(6,"Chief Technology Officer");

    public int level;
    public String position;

    RoleEnum(int level, String position) {
        this.level = level;
        this.position = position;
    }
}
