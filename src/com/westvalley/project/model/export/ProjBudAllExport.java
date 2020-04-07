package com.westvalley.project.model.export;

import com.westvalley.project.model.dto.ProjBudAllDto;
import com.westvalley.project.model.dto.ProjBudMonthDto;
import com.westvalley.project.model.dto.ProjBudYearDto;
import com.westvalley.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import weaver.conn.RecordSet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 营销项目预算额度汇总表 导出Excel
 */
public class ProjBudAllExport {

    public static void main(String[] args) {
        ProjBudAllExport export = new ProjBudAllExport();
        List<ProjBudYearDto> dataTest = export.getDataTest();
//        System.out.println(JSONObject.toJSONString(dataTest));
        try {
            FileOutputStream outputStream = new FileOutputStream("D:\\users\\Pictures\\test.xls");
            export.export(dataTest.get(0),outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据选中的ID查询数据
     * @param year 只能是同一年度的ID，不同的话只会导出最新的年度数据
     * @param ids 要导出的ID，英文逗号隔开 ，为空则导出全部
     * @return
     */
    public ProjBudYearDto getData(int year,String ids){
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from wv_v_projExcuCollect where projYear = ? ");
        if(!StringUtil.isEmpty(ids)){
            sb.append(" and proj0id in (").append(ids).append(")");
        }

        ProjBudYearDto projBudYearDto = new ProjBudYearDto();
        projBudYearDto.setYear(year);
        List<ProjBudAllDto> yearList = new ArrayList<>();
//        新零售,海外,品牌中心
        String[]  businessType= {"新零售","海外","品牌中心"};
        RecordSet rs = new RecordSet();
        rs.executeQuery(sb.toString(),year);
        while(rs.next()){
            ProjBudAllDto projBudAllDto = new ProjBudAllDto();
            projBudAllDto.setCompanyNo(rs.getString("companyNo"));
            int businessType1 = rs.getInt("businessType");
            projBudAllDto.setBusinessType(businessType1 >=0 ?businessType[businessType1]:"");
            projBudAllDto.setProj0No(rs.getString("proj0No"));
            projBudAllDto.setProj0Name(rs.getString("proj0Name"));
            projBudAllDto.setProjDeptNo(rs.getString("costNo"));
            projBudAllDto.setProjDeptName(rs.getString("costName"));
            projBudAllDto.setYearBudAmt(StringUtil.toNum(rs.getString("yearAmt")));//取年度立项


            List<ProjBudMonthDto> months = new ArrayList<>();
            //1月至12月
            String[] key = {"monAmt","monExcuAmt","monBalance"};

            for (int i = 1; i < 13; i++) {
                String key0 = key[0] + i;
                String key1 = key[1] + i;
//                String key2 = key[2];

                ProjBudMonthDto monDto = new ProjBudMonthDto();
                monDto.setMonth(i);
                monDto.setBudTotalAmt(StringUtil.toNum(rs.getString(key0)));
                monDto.setBudExcuAmt(StringUtil.toNum(rs.getString(key1)));

                monDto.setCalcAmt(sub(monDto.getBudTotalAmt(),monDto.getBudExcuAmt()));
                monDto.setRemark("");

                months.add(monDto);
            }

            projBudAllDto.setProjBudMonthDtoList(months);


            String monGrandTotalAmt = "0";
            String monGrandExcuAmt = "0";


            for (ProjBudMonthDto monDto : months) {
                monGrandTotalAmt = add(monGrandTotalAmt,monDto.getBudTotalAmt());

                monGrandExcuAmt = add(monGrandExcuAmt,monDto.getBudExcuAmt());
            }

            String calcAmt = sub(monGrandExcuAmt,monGrandTotalAmt);
            String yearAchRate = divideRate(monGrandExcuAmt,projBudAllDto.getYearBudAmt())+"%";

            projBudAllDto.setYearAchRate(yearAchRate);
            projBudAllDto.setMonGrandTotalAmt(monGrandTotalAmt);
            projBudAllDto.setMonGrandExcuAmt(monGrandExcuAmt);
            projBudAllDto.setCalcAmt(calcAmt);

            yearList.add(projBudAllDto);
        }

        projBudYearDto.setProjBudAllDtoList(yearList);

        return projBudYearDto;
    }

    /**测试数据*/
    public List<ProjBudYearDto> getDataTest(){

        List<ProjBudYearDto> projBudYearDtoList = new ArrayList<>();

        //年度
        ProjBudYearDto projBudYearDto = new ProjBudYearDto();
        //月份
        List<ProjBudAllDto> yearList = new ArrayList<>();
        for (int k = 0; k < 10; k++) {
            ProjBudAllDto projBudAllDto = new ProjBudAllDto();
            projBudAllDto.setCompanyNo("1000");
            projBudAllDto.setBusinessType("新零售");
            projBudAllDto.setProj0No("11");
            projBudAllDto.setProj0Name("电台复活节");
            projBudAllDto.setProjDeptNo("0100101");
            projBudAllDto.setProjDeptName("耳机");
            projBudAllDto.setYearBudAmt("2000");//取年度立项


            List<ProjBudMonthDto> months = new ArrayList<>();
            for (int i = 1; i < 13; i++) {

                ProjBudMonthDto monDto = new ProjBudMonthDto();
                monDto.setMonth(i);
                monDto.setBudTotalAmt(String.valueOf(i * 100));
                monDto.setBudExcuAmt(String.valueOf(i * 20));

                monDto.setCalcAmt(sub(monDto.getBudTotalAmt(),monDto.getBudExcuAmt()));
                monDto.setRemark("备注"+i);

                months.add(monDto);
            }

            projBudAllDto.setProjBudMonthDtoList(months);


            String monGrandTotalAmt = "0";
            String monGrandExcuAmt = "0";


            for (ProjBudMonthDto monDto : months) {
                monGrandTotalAmt = add(monGrandTotalAmt,monDto.getBudTotalAmt());

                monGrandExcuAmt = add(monGrandExcuAmt,monDto.getBudExcuAmt());
            }

            String calcAmt = sub(monGrandExcuAmt,monGrandTotalAmt);
            String yearAchRate = divideRate(monGrandExcuAmt,projBudAllDto.getYearBudAmt())+"%";

            projBudAllDto.setYearAchRate(yearAchRate);
            projBudAllDto.setMonGrandTotalAmt(monGrandTotalAmt);
            projBudAllDto.setMonGrandExcuAmt(monGrandExcuAmt);
            projBudAllDto.setCalcAmt(calcAmt);

            yearList.add(projBudAllDto);
        }


        projBudYearDto.setProjBudAllDtoList(yearList);
        projBudYearDto.setYear(2019);

        projBudYearDtoList.add(projBudYearDto);

        return projBudYearDtoList;
    }


    /**
     * 导出年度数据
     * @param projBudYearDto
     * @param os
     */
    public void export(ProjBudYearDto projBudYearDto, OutputStream os) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //颜色
        HSSFCellStyle yellow = workbook.createCellStyle();
        yellow.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
        yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //边框
        yellow.setBorderTop(BorderStyle.THIN);
        yellow.setBorderBottom(BorderStyle.THIN);
        yellow.setBorderLeft(BorderStyle.THIN);
        yellow.setBorderRight(BorderStyle.THIN);
        //字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short)11);
        font.setFontName("宋体");
        yellow.setFont(font);
        //居中
        yellow.setVerticalAlignment(VerticalAlignment.CENTER);
        yellow.setWrapText(true);

        //正常
        HSSFCellStyle white = workbook.createCellStyle();
        //边框
        white.setBorderTop(BorderStyle.THIN);
        white.setBorderBottom(BorderStyle.THIN);
        white.setBorderLeft(BorderStyle.THIN);
        white.setBorderRight(BorderStyle.THIN);
        //字体
        white.setFont(font);
        //居中
        yellow.setVerticalAlignment(VerticalAlignment.CENTER);
        white.setWrapText(true);

        int index = 0;
        int cellColumn = 0;
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow row = null;

        int year = projBudYearDto.getYear();

        HSSFCell cell = null;
        CellRangeAddress region = null;
        int cellLen = 0;
        //标题
        {
            row = sheet.createRow(index++);
            row.setHeight((short)600);
            //第一行
            String[] title1 = {"公司代码","业务类别","项祖名称","部门名称"};
            for (int i = 0; i < title1.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(yellow);
                cell.setCellValue(title1[i]);
            }

            String[] title2 = {"年度预算合计","年度达成率","累计月度预算合计","累计执行合计","累计月度超支(+)节约(-)"};
            String yearTitle2 = year +"年年度预算执行";
            for (int i = 0; i < title2.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(yellow);
                cell.setCellValue(yearTitle2);
            }
            //合并年年度预算执行
            region = new CellRangeAddress(0,  0, cellColumn-title2.length,cellColumn-1);
            sheet.addMergedRegion(region );

            String monTitle3 = "%s年%s月预算执行";
            String[] title3 = {"预算金额","实际金额","超支(+)节约(-)","备注"};
            String[] montn = {"1","2","3","4","5","6","7","8","9","10","11","12"};
            for (int i = 0; i < montn.length ; i++) {
                for (int j = 0; j < title3.length; j++) {
                    cell = row.createCell(cellColumn++);
                    cell.setCellStyle(yellow);
                    cell.setCellValue(String.format(monTitle3,year,montn[i]));
                }
                //合并月预算执行
                region = new CellRangeAddress(0,  0, cellColumn-title3.length,cellColumn-1);
                sheet.addMergedRegion(region );
            }
            cellLen = title1.length + title2.length + montn.length;

            cellColumn = 0;
            //第二行
            row = sheet.createRow(index++);
            row.setHeight((short)1000);
            for (int i = 0; i < title1.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(yellow);
                cell.setCellValue(title1[i]);
            }
            //合并
            for (int i = 0; i < title1.length; i++) {
                region = new CellRangeAddress(0,  1, i,i);
                sheet.addMergedRegion(region );
            }
            for (int i = 0; i < title2.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(yellow);
                cell.setCellValue(title2[i]);
            }
            for (int i = 0; i < montn.length ; i++) {
                for (int j = 0; j < title3.length; j++) {
                    cell = row.createCell(cellColumn++);
                    cell.setCellStyle(yellow);
                    cell.setCellValue(title3[j]);
                }
            }
        }
        cellColumn = 0;
        //填充数据
        List<ProjBudAllDto> projBudAllDtoList = projBudYearDto.getProjBudAllDtoList();
        for (ProjBudAllDto allDto : projBudAllDtoList) {
            row = sheet.createRow(index++);
            row.setHeight((short)480);
            String[] title1 = {allDto.getCompanyNo(),allDto.getBusinessType(),allDto.getProj0Name(),allDto.getProjDeptName()};
            for (int i = 0; i < title1.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(white);
                cell.setCellValue(title1[i]);
            }
            String[] title2 = {allDto.getYearBudAmt(),allDto.getYearAchRate(),allDto.getMonGrandTotalAmt(),allDto.getMonGrandExcuAmt(),allDto.getCalcAmt()};
            for (int i = 0; i < title2.length; i++) {
                cell = row.createCell(cellColumn++);
                cell.setCellStyle(white);
                cell.setCellValue(title2[i]);
            }
            List<ProjBudMonthDto> projBudMonthDtoList = allDto.getProjBudMonthDtoList();
            for (ProjBudMonthDto dto : projBudMonthDtoList) {
                String[] title3 = {dto.getBudTotalAmt(),dto.getBudExcuAmt(),dto.getCalcAmt(),dto.getRemark()};
                for (int j = 0; j < title3.length; j++) {
                    cell = row.createCell(cellColumn++);
                    cell.setCellStyle(white);
                    cell.setCellValue(title3[j]);
                }
            }
            cellColumn = 0;
        }
        //调整列宽
        for (int i = 0; i < cellLen; i++) {
            sheet.setColumnWidth(i, 3300);
        }

        //冻结行标题
        sheet.createFreezePane(9,2,9,2);


        workbook.write(os);
    }


    public String getExportPath(){
        return null;
    }

    public String sub(String a,String b){
        BigDecimal x = new BigDecimal(a);
        BigDecimal y = new BigDecimal(b);
        return x.subtract(y).toString();
    }


    public String divideRate(String a,String b){
        BigDecimal x = new BigDecimal(a);
        BigDecimal y = new BigDecimal(b);
        if(y.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO.toString();
        }
        BigDecimal hundred = new BigDecimal("100");

        return x.multiply(hundred).setScale(2,BigDecimal.ROUND_HALF_UP).divide(y,2,BigDecimal.ROUND_HALF_UP).toString();
    }

    public String add(String a,String b){
        BigDecimal x = new BigDecimal(a);
        BigDecimal y = new BigDecimal(b);
        return x.add(y).toString();
    }






}
