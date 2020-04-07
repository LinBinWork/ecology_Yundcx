/***
 * 引用流程：个人借款申请单
 * 引用节点：总账主管SAP转账
 * 主要功能：1.生成借贷分录
 */
jQuery(document).ready(function () {
    var rowids2 = WfForm.getDetailRowCount("detail_2")
    var rowids3 = WfForm.getDetailRowCount("detail_3")
    if (rowids2 == 0 || rowids3 == 0) {
        loadVoucherDetail();
    }

    //添加按钮按钮
    jQuery("#loadVoucherbutton").append(""
        + "<input class='ant-btn ant-btn-ghost' onclick='loadVoucherDetail()' type='button'"
        + " id='loadvoucher_btn' value='生成借贷分录' />"
        + "");

});

function loadVoucherDetail() {
    var shenqr = WfForm.convertFieldNameToId("shenqr", "main", true);//申请人名字
    var shenqrName = WfForm.getBrowserShowName(shenqr);//申请人名字
    var jksy = WfForm.convertFieldNameToId("jksy", "main", true);//借款事由
    var jksyV = WfForm.getFieldValue(jksy).substr(0, 20);//借款事由
    var bt = jQuery("#requestname").val();
    var zy = "借款：付" + shenqrName + " " + bt;//摘要
    // var zy = shenqrName + jksyV;//摘要
    var je = WfForm.convertFieldNameToId("bcjkje", "main", true);//本次借款金额
    var jeV = WfForm.getFieldValue(je);

    var shenqbm = WfForm.convertFieldNameToId("shenqbm", "main", true);//申请人部门
    var shenqbmV = WfForm.getFieldValue(shenqbm);

    var lrzx = "";
    var lrzxName = "";
    //带出利润中心
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/cmd.jsp",
        data: 'cmd=getDepartment&bmbm=' + shenqbmV,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson != null && returnJson != undefined) {
                lrzx = returnJson.sapcbzxbm
                lrzxName = returnJson.sapcbzxmc
            }
        }
    });

    var gys = "";
    var gysName = "";
    var gh = WfForm.convertFieldNameToId("gonghao", "main", true);//工号
    var ghV = WfForm.getFieldValue(gh);
    //带出供应商
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/cmd.jsp",
        data: 'cmd=getGys&gh=' + ghV,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson != null && returnJson != undefined) {
                gys = returnJson.PARTNER
                gysName = returnJson.NAME1
            }
        }
    });


    var jzm2 = WfForm.convertFieldNameToId("jzm", "detail_2", true);//记账码
    var tbzz2 = WfForm.convertFieldNameToId("tbzz", "detail_2", true);//特别总账
    var zy2 = WfForm.convertFieldNameToId("zy", "detail_2", true);//摘要
    var hjkm2 = WfForm.convertFieldNameToId("hjkm", "detail_2", true);//会计科目
    var jfje2 = WfForm.convertFieldNameToId("jfje", "detail_2", true);//借方金额
    var lrzx2 = WfForm.convertFieldNameToId("lrzx", "detail_2", true);//利润中心
    var gys2 = WfForm.convertFieldNameToId("gys", "detail_2", true);//供应商
    WfForm.delDetailRow("detail_2", "all");//清空明细


    var bclxje = WfForm.convertFieldNameToId("bclxje", "detail_4", true);//本次立项金额
    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_4", true);//分摊部门
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var gsbm = WfForm.convertFieldNameToId("gsbm", "main", true);//公司编码
    var gsbmV = WfForm.getFieldValue(gsbm);//公司编码
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var bclxjeV = WfForm.getFieldValue(bclxje + "_" + index4[i]);
        if (bclxjeV == 0.00) {
            continue;
        }
        var ftbmN = WfForm.getBrowserShowName(ftbm + "_" + index4[i]);
        var mc = "";
        var bm = "";
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/cmd.jsp",
            data: 'cmd=getCbzx&ftbmN=' + ftbmN + "&gsbm=" + gsbmV,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    mc = returnJson.cbzxmc
                    bm = returnJson.cbzxbm
                }
            }
        });
        //借方分录
        /*WfForm.addDetailRow("detail_2", {
            [jzm2]: {value: "21"},
            [zy2]: {value: zy},
            [jfje2]: {value: bclxjeV},
            [hjkm2]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"},]},
            [lrzx2]: {value: bm, specialobj: [{id: bm, name: mc},]},
            [gys2]: {value: gys, specialobj: [{id: gys, name: gysName},]},
        });*/

        WfForm.addDetailRow("detail_2");
        var dex = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
        dex = dex[dex.length - 1]
        WfForm.changeFieldValue(jzm2 + "_" + dex, {value: "21"});
        WfForm.changeFieldValue(zy2 + "_" + dex, {value: zy});
        WfForm.changeFieldValue(jfje2 + "_" + dex, {value: bclxjeV});
        WfForm.changeFieldValue(hjkm2 + "_" + dex, {
            value: "1221010100",
            specialobj: [{id: "1221010100", name: "其他应收款-借款"},]
        });
        WfForm.changeFieldValue(lrzx2 + "_" + dex, {value: bm, specialobj: [{id: bm, name: mc},]});
        WfForm.changeFieldValue(gys2 + "_" + dex, {value: gys, specialobj: [{id: gys, name: gysName},]});
    }
    var detail3 = WfForm.getDetailRowCount("detail_3")
    var jzm3 = WfForm.convertFieldNameToId("jzm", "detail_3", true);//记账码
    var tbzz3 = WfForm.convertFieldNameToId("tbzz", "detail_3", true);//特别总账
    var zy3 = WfForm.convertFieldNameToId("zy", "detail_3", true);//摘要
    var hjkm3 = WfForm.convertFieldNameToId("hjkm", "detail_3", true);//会计科目
    var jfje3 = WfForm.convertFieldNameToId("dfje", "detail_3", true);//借方金额
    var xjl3 = WfForm.convertFieldNameToId("xjl", "detail_3", true);//现金流
    var lrzx3 = WfForm.convertFieldNameToId("lrzx", "detail_3", true);//利润中心
    WfForm.delDetailRow("detail_3", "all");//清空明细
    //贷方分录
    /*WfForm.addDetailRow("detail_3", {
        [jzm3]: {value: "50"},
        [zy3]: {value: zy},
        [jfje3]: {value: jeV},
        [xjl3]: {value: "B4"},
        [hjkm3]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},
        [lrzx3]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},
    });*/

    WfForm.addDetailRow("detail_3");
    var dex = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
    dex = dex[dex.length - 1]
    WfForm.changeFieldValue(jzm3 + "_" + dex, {value: "50"});
    WfForm.changeFieldValue(zy3 + "_" + dex, {value: zy});
    WfForm.changeFieldValue(jfje3 + "_" + dex, {value: jeV});
    WfForm.changeFieldValue(xjl3 + "_" + dex, {value: "B4"});
    WfForm.changeFieldValue(hjkm3 + "_" + dex, {
        value: "1002010100",
        specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]
    });
    WfForm.changeFieldValue(lrzx3 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});
}
