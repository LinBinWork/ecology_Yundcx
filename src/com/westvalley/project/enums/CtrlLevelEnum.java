package com.westvalley.project.enums;

/**
 * 控制级别
 */
public enum CtrlLevelEnum {
    PARENT(0,"父项"),LAST(1,"末端");


    private int level;
    private String name;

    private CtrlLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
