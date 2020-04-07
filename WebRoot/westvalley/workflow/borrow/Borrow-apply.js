/***
 * 引用流程：个人借款申请单
 * 引用节点：申请节点
 * 主要功能：1.根据申请人自动带出借款历史明细，2.根据项目名称带出项目信息。
 */
jQuery(document).ready(function () {

    var fxxmjl = WfForm.convertFieldNameToId("fxxmjl", "main", true);//父项项目经理
    var zxxmjl = WfForm.convertFieldNameToId("zxxmjl", "main", true);//子项项目经理
    var xmkyje = WfForm.convertFieldNameToId("xmkyje", "main", true);//项目可用金额
    var xmmc = WfForm.convertFieldNameToId("xmmc", "main", true);//项目名称

    //带出项目信息
    WfForm.bindFieldChangeEvent(xmmc, function (obj, id, value) {
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/borrow/Borrow.jsp",
            data: 'cmd=getProject&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                // console.log("returnJson === ", returnJson);
                if (returnJson != null && returnJson != undefined) {
                    var fx = returnJson.fx
                    var zx = returnJson.zx
                    WfForm.changeFieldValue(xmkyje, {value: returnJson.projBalance});
                }
            }
        });
        getFxData(value);
        //计算分摊金额
        setTimeout(function () {
            getApportionData();
        }, 500);
    });

    var bcjkje = WfForm.convertFieldNameToId("bcjkje", "main", true);//本次借款金额
    //计算分摊金额
    WfForm.bindFieldChangeEvent(bcjkje, function (obj, id, value) {
        getApportionData();
    });

    var shenqrq = WfForm.convertFieldNameToId("shenqrq", "main");//申请日期
    var shenqrqV = WfForm.getFieldValue(shenqrq);
    var yjhkrq = WfForm.convertFieldNameToId("yjhkrq", "main");//预计还款日期
    WfForm.controlDateRange(yjhkrq, shenqrqV, '2099-12-31');


    //根据申请人带出借款历史明细
    var shenqr = WfForm.convertFieldNameToId("shenqr", "main", true);//申请人
    var shenqrV = WfForm.getFieldValue(shenqr);
    var requestId = WfForm.getBaseInfo().requestid;
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/borrow/Borrow.jsp",
        data: 'cmd=getBorrow&userid=' + shenqrV + "&&requestId=" + requestId,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            // console.log("returnJson === ", returnJson);
            if (returnJson != null && returnJson != undefined) {
                WfForm.delDetailRow("detail_1", "all");//清空明细
                var jksqh = WfForm.convertFieldNameToId("jksqh", "detail_1", true);//借款申请号
                var jkje = WfForm.convertFieldNameToId("jkje", "detail_1", true);//借款金额
                var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_1", true);//还款/冲销金额
                var lkfs = WfForm.convertFieldNameToId("lkfs", "detail_1", true);//领款方式
                var rq = WfForm.convertFieldNameToId("rq", "detail_1", true);//日期
                var jkye = WfForm.convertFieldNameToId("jkye", "detail_1", true);//借款余额
                for (var i in returnJson) {
                    var data = returnJson[i];
                    WfForm.addDetailRow("detail_1");
                    var dex = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
                    dex = dex[dex.length - 1]
                    WfForm.changeFieldValue(jksqh + "_" + dex, {value: data.requestcode});
                    WfForm.changeFieldValue(jkje + "_" + dex, {value: data.money});
                    WfForm.changeFieldValue(hkcxje + "_" + dex, {value: parseFloat(data.moneyed) + parseFloat(data.moneying)});
                    WfForm.changeFieldValue(lkfs + "_" + dex, {value: data.payment});
                    WfForm.changeFieldValue(rq + "_" + dex, {value: data.brodate});
                    WfForm.changeFieldValue(jkye + "_" + dex, {value: data.moneylast});
                }
            }
        }
    });


    //提交校验
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //校验。
        if (checkBudData()) {
            callback();
        }
    });

});

function checkBudData() {
    var f = true;
    var bcjkje = WfForm.convertFieldNameToId("bcjkje", "main", true);//本次借款金额
    var xmkyje = WfForm.convertFieldNameToId("xmkyje", "main", true);//项目可用金额
    var bcjkjeV = WfForm.getFieldValue(bcjkje);
    var xmkyjeV = WfForm.getFieldValue(xmkyje);
    if (parseFloat(bcjkjeV) != NaN && parseFloat(xmkyjeV) != NaN) {
        if (parseFloat(bcjkjeV) > parseFloat(xmkyjeV)) {
            f = false;
            antd.Modal.error({
                title: '提示！',
                content: "本次借款金额不能大于项目可用金额。",
                onOk: function () {
                }
            });
        }
    }

    return f;
}

//获取关联父项
function getFxData(value) {
    var glfx = WfForm.convertFieldNameToId("glfx", "main", true);//关联父项
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/cmd.jsp",
        data: 'cmd=getFxData&id=' + value,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson != null && returnJson != undefined) {
                WfForm.changeFieldValue(glfx, {
                    value: returnJson.PID,
                    specialobj: [
                        {id: returnJson.PID, name: "关联父项"}
                    ]
                });
            }
        }
    });
}

//根据比率计算分摊金额
function getApportionData() {
    var bcjkje = WfForm.convertFieldNameToId("bcjkje", "main", true);//本次借款金额
    var bcjkjeV = WfForm.getFieldValue(bcjkje);
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var bclxje = WfForm.convertFieldNameToId("bclxje", "detail_4", true);//本次立项金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_4", true);//分摊比率

    var sumMoney = 0;
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var ftblV = WfForm.getFieldValue(ftbl + "_" + index4[i]);
        if (bcjkjeV == "") {
            WfForm.changeFieldValue(bclxje + "_" + index4[i], {value: 0.00});
        } else {
            var dex = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
            dex = dex[dex.length - 1]
            var bclxjeV = (parseFloat(bcjkjeV) * (parseFloat(ftblV) / 100)).toFixed(2)
            if (index4[i] == dex) {
                WfForm.changeFieldValue(bclxje + "_" + index4[i], {value: parseFloat(bcjkjeV) - parseFloat(sumMoney)});
            } else {
                sumMoney += parseFloat(bclxjeV)
                WfForm.changeFieldValue(bclxje + "_" + index4[i], {value: bclxjeV});
            }
        }
    }
}





