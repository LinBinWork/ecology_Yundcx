//@ sourceURL=/Transfer.js
jQuery(document).ready(function () {
    /**
     * 借方分录里自动带出的摘要，金额信息
     */
    var rowids1 = WfForm.getDetailRowCount("detail_1")
    var rowids2 = WfForm.getDetailRowCount("detail_2")
    if (rowids1 == 0 || rowids2 == 0) {
        loadVoucherDetail();
    }

    //添加按钮按钮
    jQuery("#loadVoucherbutton").append(""
        + "<input class='ant-btn ant-btn-ghost' onclick='loadVoucherDetail()' type='button'"
        + " id='loadvoucher_btn' value='生成借贷分录' />"
        + "");
});

function loadVoucherDetail() {
    var dqjkye = WfForm.getFieldValue(WfForm.convertFieldNameToId("dqjkye"));
    var bchkje = WfForm.getFieldValue(WfForm.convertFieldNameToId("bchkje"));
    var shenqr = WfForm.getBrowserShowName(WfForm.convertFieldNameToId("shenqr"));
    var hksy = WfForm.getFieldValue(WfForm.convertFieldNameToId("hksy")).substr(0, 20);
    var jzm = WfForm.convertFieldNameToId("jzm", "detail_1", true);
    var zy = WfForm.convertFieldNameToId("zy", "detail_1", true);
    var jfje = WfForm.convertFieldNameToId("jfje", "detail_1", true);
    var xjl = WfForm.convertFieldNameToId("xjl", "detail_1", true);
    var hjkm = WfForm.convertFieldNameToId("hjkm", "detail_1", true);
    var bt = jQuery("#requestname").val();
    var lrzx1 = WfForm.convertFieldNameToId("lrzx", "detail_1", true);

    var shenqbm = WfForm.convertFieldNameToId("shenqbm", "main", true);
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
                lrzx = returnJson.sapcbzxbm;
                lrzxName = returnJson.sapcbzxmc;
            }
        }
    });

    var gys = "";
    var gysName = "";
    var gh = WfForm.convertFieldNameToId("gonghao", "main", true);//工号
    var ghV = WfForm.getFieldValue(gh);
    var gys2 = WfForm.convertFieldNameToId("gys", "detail_1", true);//供应商
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

    /**
     * 借方分录里自动带出的摘要，金额信息
     */
    WfForm.delDetailRow("detail_1", "all");
    WfForm.addDetailRow("detail_1", {
        [jzm]: {value: "40"},
        [zy]: {value: "还借款：收" + shenqr + " " + bt},
        [hjkm]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"}]},
        [jfje]: {value: bchkje},
        [lrzx1]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},
        [gys2]: {value: gys, specialobj: [{id: gys, name: gysName},]},
        [xjl]: {value: "B4"},
    });

    /**
     * 贷方分录里自动带出的摘要，金额信息
     */
    var jzm2 = WfForm.convertFieldNameToId("jzm", "detail_2", true);
    var zy2 = WfForm.convertFieldNameToId("zy", "detail_2", true);
    var dfje = WfForm.convertFieldNameToId("dfje", "detail_2", true);
    var hjkm2 = WfForm.convertFieldNameToId("hjkm", "detail_2", true);
    var lrzx2 = WfForm.convertFieldNameToId("lrzx", "detail_2", true);
    var ry = WfForm.convertFieldNameToId("ry", "detail_2", true);//供应商
    WfForm.delDetailRow("detail_2", "all");
    WfForm.addDetailRow("detail_2", {
        [jzm2]: {value: "31"},
        [zy2]: {value: "还借款：收" + shenqr + " " + bt},
        [hjkm2]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"}]},
        [dfje]: {value: bchkje},
        [ry]: {value: gys, specialobj: [{id: gys, name: gysName},]},
        [lrzx2]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},
    });
}
