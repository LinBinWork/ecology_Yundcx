package com.westvalley.project.model.dto;

import java.util.List;

/**
 * 营销项目预算额度汇总表  年度数据
 */
public class ProjBudYearDto {

    /**年度*/
    private int year;
    /**年度预算数据*/
    private List<ProjBudAllDto> projBudAllDtoList;

    /**
     * 获取 年度
     *
     * @return year 年度
     */
    public int getYear() {
        return this.year;
    }

    /**
     * 设置 年度
     *
     * @param year 年度
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * 获取 年度预算数据
     *
     * @return projBudAllDtoList 年度预算数据
     */
    public List<ProjBudAllDto> getProjBudAllDtoList() {
        return this.projBudAllDtoList;
    }

    /**
     * 设置 年度预算数据
     *
     * @param projBudAllDtoList 年度预算数据
     */
    public void setProjBudAllDtoList(List<ProjBudAllDto> projBudAllDtoList) {
        this.projBudAllDtoList = projBudAllDtoList;
    }
}
