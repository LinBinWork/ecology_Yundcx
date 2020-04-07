package com.westvalley.project.util;

import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.enums.ProjtypeEnum;

public class EnumsUtil {

//    /**
//     * 获取预算类型
//     * @param type
//     * @return
//     */
//    public static BudTypeEnum getBudTypeEnum(int type){
//        if(BudTypeEnum.USED.getType() == type){
//            return BudTypeEnum.USED;
//        }
//        if(BudTypeEnum.FREZEE.getType() == type){
//            return BudTypeEnum.FREZEE;
//        }
//        if(BudTypeEnum.RELEASE.getType() == type){
//            return BudTypeEnum.RELEASE;
//        }
//        if(BudTypeEnum.TRANSFER.getType() == type){
//            return BudTypeEnum.TRANSFER;
//        }
//        if(BudTypeEnum.SPLIT_FREZEE.getType() == type){
//            return BudTypeEnum.SPLIT_FREZEE;
//        }
//        if(BudTypeEnum.SPLIT_FREZEE.getType() == type){
//            return BudTypeEnum.SPLIT_FREZEE;
//        }
//        throw new IllegalArgumentException("预算类型不正确");
//    }

    /**
     * 获取控制级别
     * @param level
     * @return
     */
    public static CtrlLevelEnum getCtrlLevelEnum(int level){
        if(CtrlLevelEnum.PARENT.getLevel() == level){
            return CtrlLevelEnum.PARENT;
        }
        if(CtrlLevelEnum.LAST.getLevel() == level){
            return CtrlLevelEnum.LAST;
        }
        throw new IllegalArgumentException("控制级别不正确");
    }


    /**
     * 获取项目类型
     * @param projtype
     * @return
     */
    public static ProjtypeEnum getProjtypeEnum(int projtype){
        if(ProjtypeEnum.ORG.getType() == projtype){
            return ProjtypeEnum.ORG;
        }
        if(ProjtypeEnum.PARENT.getType() == projtype){
            return ProjtypeEnum.PARENT;
        }
        if(ProjtypeEnum.SON.getType() == projtype){
            return ProjtypeEnum.SON;
        }
        if(ProjtypeEnum.GRANDSON.getType() == projtype){
            return ProjtypeEnum.GRANDSON;
        }
        throw new IllegalArgumentException("项目类型不正确");
    }

}
