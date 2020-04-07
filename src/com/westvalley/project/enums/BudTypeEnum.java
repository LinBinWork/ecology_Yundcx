package com.westvalley.project.enums;

/**
 * 预算状态
 */
public enum BudTypeEnum {
    /**已使用*/
    USED(0,"已使用"),
    /**冻结*/
    FREZEE(1,"冻结"),
    /**释放*/
    RELEASE(2,"释放"),
    /**调拨*/
    TRANSFER(3,"调拨-拨出"),
    /**拆解 冻结*/
    SPLIT_FREZEE(4,"拆解-冻结"),
    /**拆解 结束*/
    SPLIT_USED(5,"拆解-归档"),
    /**追加*/
    APPEND(6,"追加"),
    /**预付款/付款 合同付款 申请提交使用*/
    CONTRACT_FREZEE(7,"合同金额-冻结"),
    /**预付款/付款 合同付款 归档使用*/
    CONTRACT_USED(8,"合同金额-使用"),
    /**冲销 使用*/
    BORROW_REVERSAL(9,"冲销"),
    /**合同申请  等同与冻结*/
    APPLY(1,"合同申请-在途"),
    /**合同申请 归档待付款 等同与冻结*/
    ARCHIVE(1,"合同申请-归档冻结"),
    /**合同申请 归档直接付款 等同与使用*/
    PAY(0,"合同申请-直接付款"),
    ;


    private int type;
    private String name;

    private BudTypeEnum(int type, String name) {
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
