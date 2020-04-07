package com.westvalley.project.model.validate;

import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.file.ExcelParseForJXL;
import weaver.formmode.interfaces.ImportPreInterfaceForJXLAction;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 祖项导入验证
 */
public class Proj0Validate implements ImportPreInterfaceForJXLAction {


    /**
     * 在导入数据前校验excel数据
     *
     * @param param 模块表单参数
     * @param user 当前人对象
     * @param excelParse excel对象
     * @return
     */
    @Override
    public String checkImportData(Map<String, Object> param, User user, ExcelParseForJXL excelParse) {

        // 获取模块ID
//        Integer modeId = Util.getIntValue(param.get("modeid").toString());
//        Integer formId = Util.getIntValue(param.get("formid").toString());

        // 获取当前登录人员ID
        Integer userId = user.getUID();

        // 获取上传的excel总行数
        int rowSum = excelParse.getRowSum("1");

        StringBuilder sb = new StringBuilder();

        List<String> orgCode = new ArrayList<>();
        //从第二行开始
        for (int i = 2; i <= rowSum; i++) {
            // 获取上传Excel 的某个单元格非时间日期值
            // 第一个参数说明： 是第几个工作簿 sheet1 表示第一个，后面以此类推
            // 第二个参数说明： 是第几行
            // 第三个参数说明： 是第几列
            String strValue = excelParse.getValue("1",i, 4);
//            log.d("excelParse",i,strValue);
            if(StringUtil.isEmpty(strValue)){
                sb.append("第").append(i).append("行，祖项编码不能为空<br>\n");
                continue;
            }
            if(orgCode.contains(strValue)){
                sb.append("第").append(i).append("行，祖项编码:").append(strValue).append(" 不能重复<br>\n");
                continue;
            }
            orgCode.add(strValue);
        }
        if(sb.toString().length() == 0){
            //校验数据库是否重复
            RecordSet rs = new RecordSet();
            rs.executeQuery("select orgCode from uf_orgproject");
            while(rs.next()){
                String temp = rs.getString(1);
                if(orgCode.contains(temp)){
                    sb.append("祖项编码:").append(temp).append(" 已存在<br>\n");
                }
            }
        }

        return sb.toString();
    }
}
