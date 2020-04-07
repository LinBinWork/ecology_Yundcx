package com.westvalley.project.enums;

/**
 * 控制级别
 */
public enum ProjtypeEnum {
    ORG(0,"祖项"),
    PARENT(1,"父项"),
    SON(2,"子项"),
    GRANDSON(3,"孙子项"),
    ;
    private int type;
    private String name;
    private ProjtypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
