/**
 * 引用流程：项目关闭审批流程
 * 引用节点：创建节点
 * 主要功能：根据项目名称带出项目立项金额，项目已发生金额，项目剩余金额
 */
jQuery(document).ready(function () {
    var xmmc = WfForm.convertFieldNameToId("xmmc", "main", true);
    var xmlxje = WfForm.convertFieldNameToId("xmlxje", "main", true);//项目立项金额
    var xmyfsje = WfForm.convertFieldNameToId("xmyfsje", "main", true);//项目已发生金额
    var xmsyje = WfForm.convertFieldNameToId("xmsyje", "main", true);//项目剩余金额


    var zxszxbh1 = WfForm.convertFieldNameToId("zxszxbh", "detail_1", true);//子项/孙子项编号
    var zxszxmc1 = WfForm.convertFieldNameToId("zxszxmc", "detail_1", true);//子项/孙子项名称
    var lxje1 = WfForm.convertFieldNameToId("lxje", "detail_1", true);//立项金额
    var yfsje1 = WfForm.convertFieldNameToId("yfsje", "detail_1", true);//已发生金额
    var sfje1 = WfForm.convertFieldNameToId("sfje", "detail_1", true);//释放金额
    var sfjfzxcl1 = WfForm.convertFieldNameToId("sfjfzxcl", "detail_1", true);//是否交付中心处理

    WfForm.bindDetailFieldChangeEvent(yfsje1, function (id, idx, value) {
        var lxje1V = WfForm.getFieldValue(lxje1 + "_" + idx);
        WfForm.changeFieldValue(sfje1 + "_" + idx, {value: parseFloat(lxje1V) - parseFloat(value)});//释放金额
    });

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        var sumMoney = parseFloat(WfForm.getFieldValue(xmsyje));
        var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        for (var i in index1) {
            if (i == 'remove') {
                continue;
            }
            var sfje1V = WfForm.getFieldValue(sfje1 + "_" + index1[i]);
            sumMoney += parseFloat(sfje1V);
        }
        if (checkBudData()) {
            WfForm.showConfirm("当前项目剩余总金额" + sumMoney + "，项目关闭后，剩余金额将释放，后续该项目将无法使用此预算费用，确认项目关闭吗？", function () {
                callback();
            });
        }
    });

    WfForm.changeFieldAttr(xmlxje, 1);
    WfForm.changeFieldAttr(xmyfsje, 1);
    WfForm.changeFieldAttr(xmsyje, 1);
    var xmmcV = WfForm.getFieldValue(xmmc);
    if (xmmcV != '') {
        var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        for (var i in index1) {
            if (i == 'remove') {
                continue;
            }
            var sfjfzxcl1V = WfForm.getFieldValue(sfjfzxcl1 + "_" + index1[i]);
            var yfsje1V = WfForm.getFieldValue(yfsje1 + "_" + index1[i]);
            if (sfjfzxcl1V == 0) {
                WfForm.changeFieldAttr(yfsje1 + "_" + index1[i], 3);
            } else {
                WfForm.changeFieldAttr(yfsje1 + "_" + index1[i], 1);
            }
        }
    }


    WfForm.bindFieldChangeEvent(xmmc, function (obj, id, value) {
        WfForm.changeFieldAttr(xmlxje, 2);
        WfForm.changeFieldAttr(xmyfsje, 2);
        WfForm.changeFieldAttr(xmsyje, 2);
        if (value == '') {
            WfForm.changeFieldValue(xmlxje, {value: 0.00}, {viewAttr: "1"});
            WfForm.changeFieldValue(xmyfsje, {value: 0.00}, {viewAttr: "1"});
            WfForm.changeFieldValue(xmsyje, {value: 0.00}, {viewAttr: "1"});
            WfForm.delDetailRow("detail_1", "all");//清空明细
            return false;
        }
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/close/Close.jsp",
            data: 'cmd=getProjectData&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var data = returnJson.data
                    WfForm.changeSingleField(xmlxje, {value: data.projAmt}, {viewAttr: "1"});
                    WfForm.changeSingleField(xmyfsje, {value: data.projUsedAmt}, {viewAttr: "1"});
                    WfForm.changeSingleField(xmsyje, {value: data.projBalance}, {viewAttr: "1"});
                }
            }
        });

        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/close/Close.jsp",
            data: 'cmd=getProjectList&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var data = returnJson.data
                    WfForm.delDetailRow("detail_1", "all");//清空明细
                    for (var i in data) {
                        if (i == 'remove') {
                            continue;
                        }
                        WfForm.addDetailRow("detail_1");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldValue(zxszxbh1 + "_" + dex, {
                            value: data[i].id,
                            specialobj: [{id: data[i].id, name: data[i].projNo}]
                        });//子项/孙子项编号
                        WfForm.changeFieldValue(zxszxmc1 + "_" + dex, {value: data[i].projName});//子项/孙子项名称
                        WfForm.changeFieldValue(lxje1 + "_" + dex, {value: data[i].projAmt});//立项金额
                        if (data[i].ctrlLevel == "PARENT") {
                            //父端
                            WfForm.changeFieldValue(sfje1 + "_" + dex, {value: 0.00});//释放金额
                        } else {
                            //末端
                            WfForm.changeFieldValue(sfje1 + "_" + dex, {value: data[i].projBalance});//释放金额
                        }
                        WfForm.changeFieldValue(sfjfzxcl1 + "_" + dex, {value: data[i].deliveryCenter});//是否交付中心处理
                        if (data[i].deliveryCenter == 0) {
                            WfForm.changeSingleField(yfsje1 + "_" + dex, {value: data[i].projUsedAmt}, {viewAttr: "3"});//已发生金额
                        } else {
                            WfForm.changeSingleField(yfsje1 + "_" + dex, {value: data[i].projUsedAmt}, {viewAttr: "1"});//已发生金额
                        }
                    }
                }
            }
        });

    });
})
;

function checkBudData() {
    var f = true;
    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var sfje1 = WfForm.convertFieldNameToId("sfje", "detail_1", true);//释放金额
    var zxszxbh1 = WfForm.convertFieldNameToId("zxszxbh", "detail_1", true);//子项/孙子项编号
    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var sfje1V = WfForm.getFieldValue(sfje1 + "_" + index1[i])//释放金额
        if (parseFloat(sfje1V) < 0) {
            f = false
            var zxszxbh1N = WfForm.getBrowserShowName(zxszxbh1 + "_" + index1[i])//项目编号
            antd.Modal.error({
                title: '提示！',
                content: "项目编号:" + zxszxbh1N + "中的释放金额不能小于0。",
                onOk: function () {
                }
            });
            return f;
        }
    }
    return f;
}
