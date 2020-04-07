package com.westvalley.project.enums;

/**
 * 预算费用类型
 */
public enum ExpTypeEnum {
    /**增加*/
    INCREASE(0,"增加"),
    /**减少*/
    DECREASE(1,"减少"),
    /**不计算*/
    NOCALC(2,"不计算"),
    ;

    private int type;
    private String name;

    private ExpTypeEnum(int type, String name) {
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
