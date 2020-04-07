/**
 * 引用流程：项目合同审批申请流程
 * 引用节点：申请节点
 * 主要功能：申请单提交时，校验所关联的项目中是否有应完成执行方案申请而未完成的情况，如有，则不可提交，并弹出提示，要求XX项目完成执行方案审批。
 */
jQuery(document).ready(function () {
//提交校验
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //校验。
        var htje = WfForm.convertFieldNameToId("htje", "main", true);
        var htjevalue = WfForm.getFieldValue(htje);//合同金额
        if (parseFloat(htjevalue) == 0) {
            WfForm.showConfirm("本次付款金额为0，是否确认提交？", function () {
                if (checkBudData()) {
                    callback();
                }
            });
        } else {
            if (checkBudData()) {
                callback();
            }
        }

    });

    var xmbh = WfForm.convertFieldNameToId("xmbh", "detail_2", true);//项目编号
    WfForm.bindDetailFieldChangeEvent(xmbh, function (id, rowIndex, value) {
        var kyye = WfForm.convertFieldNameToId("kyye", "detail_2", true);//可用余额
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/contract/Contract.jsp",
            data: 'op=getMoney&&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                // console.log("returnJson === ", returnJson);
                if (returnJson != null) {
                    if (returnJson.money != undefined && returnJson.money != null) {
                        WfForm.changeFieldValue(kyye + "_" + rowIndex, {value: returnJson.money});
                    } else {
                        WfForm.changeFieldValue(kyye + "_" + rowIndex, {value: 0.00});
                    }

                }
            }
        });
    });


    var ftje = WfForm.convertFieldNameToId("ftje", "detail_2");//分摊金额
    WfForm.bindDetailFieldChangeEvent(ftje, function (id, idx, value) {
        flushPrecent();
    });

    var sfgdjeht = WfForm.convertFieldNameToId("sfgdjeht", "main", true);
    var htje = WfForm.convertFieldNameToId("htje", "main", true);//合同金额
    WfForm.bindFieldChangeEvent(sfgdjeht, function (id, idx, value) {
        var sfgdjehtV = WfForm.getFieldValue(sfgdjeht);
        if (sfgdjehtV == '1') {
            //有金额合同付款
            WfForm.delDetailRow("detail_1", "all");//清空明细
            WfForm.addDetailRow("detail_1");
            WfForm.changeFieldValue(htje, {value: ""});
            WfForm.changeFieldAttr(htje, 3);
        } else {
            WfForm.delDetailRow("detail_1", "all");//清空明细
            WfForm.changeFieldValue(htje, {value: 0.00});
            WfForm.changeFieldAttr(htje, 3);
        }
    });

    var htksrq = WfForm.convertFieldNameToId("htksrq", "main");//合同开始日期
    var htjsrq = WfForm.convertFieldNameToId("htjsrq", "main");//合同结束日期
    WfForm.bindFieldChangeEvent(htksrq, function (id, idx, value) {
        WfForm.controlDateRange(htjsrq, value, '2099-12-31');
    });
});


//刷新分摊比例
function flushPrecent() {
    var ftzje = WfForm.convertFieldNameToId("ftzje");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);

    var ftje = WfForm.convertFieldNameToId("ftje", "detail_2");//分摊金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_2");//分摊比例

    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    jQuery.each(rowids, function (i, idx) {
        var ftjeV = WfForm.getFieldValue(ftje + "_" + idx);
        var ftblV = ftjeV / ftzjeV * 100;
        if (isNaN(ftblV)) {
            WfForm.changeFieldValue(ftbl + "_" + idx, {value: 100.00});
        } else {
            WfForm.changeFieldValue(ftbl + "_" + idx, {value: ftblV});
        }
    });
}

function checkBudData() {
    var msg = "";
    var htje = WfForm.convertFieldNameToId("htje", "main", true);
    var htjevalue = WfForm.getFieldValue(htje);//合同金额
    var ftje = WfForm.convertFieldNameToId("ftje", "detail_2", true);
    var sumFtje = 0.00;//分摊金额
    var index2 = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    var xmbh = WfForm.convertFieldNameToId("xmbh", "detail_2", true);
    var xmbhs = '';
    for (var i in index2) {
        if (i == 'remove') {
            continue;
        }
        var value = WfForm.getFieldValue(xmbh + "_" + index2[i])
        var ftjevalue = WfForm.getFieldValue(ftje + "_" + index2[i])
        // console.log("ftjevalue == ", ftjevalue)
        if (ftjevalue != '' && !isNaN(ftjevalue)) {
            sumFtje += parseFloat(ftjevalue);
        }
        if (xmbhs == '') {
            xmbhs = value
        } else {
            xmbhs = xmbhs + "," + value
        }
    }
    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var fkje = WfForm.convertFieldNameToId("fkje", "detail_1", true);
    var sumFkje = 0.00;
    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var fkjevalue = WfForm.getFieldValue(fkje + "_" + index1[i])
        if (fkjevalue != '' && !isNaN(fkjevalue)) {
            sumFkje += parseFloat(fkjevalue);
        }

    }
    if (sumFtje != htjevalue) {
        top.Dialog.alert("项目分摊总金额不等于合同金额，无法提交。");
        return false;
    }
    if (sumFkje != htjevalue) {
        top.Dialog.alert("付款总金额不等于合同金额，无法提交。");
        return false;
    }
    if (xmbhs == '') {
        return true;
    }
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/contract/Verify-Contract.jsp",
        data: 'xmbhs=' + xmbhs,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            // console.log("returnJson === ", returnJson);
            msg = returnJson.msg;
        }
    });
    // console.log("msg === ", msg);
    if (msg != null && msg != '') {
        var bhs = msg.split(",");
        var res = "";
        var xmmc = WfForm.convertFieldNameToId("xmmc", "detail_2", true);
        var xms = [];
        for (var bh in bhs) {
            if (bh == 'remove') {
                continue;
            }
            for (var i in index2) {
                var value = WfForm.getFieldValue(xmbh + "_" + index2[i])
                if (i == 'remove' || value == '') {
                    continue;
                }
                if (value == bhs[bh]) {
                    var xmName = WfForm.getFieldValue(xmmc + "_" + index2[i])//项目名称；
                    if (xms.indexOf(xmName) == -1) {
                        xms.push(xmName)
                    }
                }
            }
        }
        res = "[" + xms + "]项目未完成执行方案审批，请先完成执行方案审批！";
        top.Dialog.alert(res);
        return false;
    } else {
        return true;
    }

}