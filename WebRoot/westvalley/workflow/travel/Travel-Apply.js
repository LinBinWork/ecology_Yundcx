/***
 * 引用流程：差旅费费用报销
 * 引用节点：申请节点
 * 主要功能：1.带出借款历史记录 2.提交校验
 */
jQuery(document).ready(function () {
    var fycdbm = WfForm.convertFieldNameToId("fycdbm", "main", true);//费用承担部门
    var fycdbmV = WfForm.getFieldValue(fycdbm);
    getBorrowData();    //根据申请人带出借款历史明细
    if (fycdbmV == '') {
        var shenqbm = WfForm.convertFieldNameToId("shenqbm", "main", true);//申请人部门
        var shenqbmV = WfForm.getFieldValue(shenqbm);
        //带出费用归属部门
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/cmd.jsp",
            data: 'cmd=getDepartment&bmbm=' + shenqbmV,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    WfForm.changeFieldValue(fycdbm, {
                        value: returnJson.sapcbzxbm,
                        specialobj: [{id: returnJson.sapcbzxbm, name: returnJson.sapcbzxmc},]
                    });
                }
            }
        });
    }

    var xmbh = WfForm.convertFieldNameToId("xmbh", "main", true);//项目编号
    //带出项目信息
    WfForm.bindFieldChangeEvent(xmbh, function (obj, id, value) {
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/cmd.jsp",
            data: 'cmd=getProject&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var sjxmjl = WfForm.convertFieldNameToId("sjxmjl", "main", true);//父项项目经理
                    var xmjl = WfForm.convertFieldNameToId("xmjl", "main", true);//子项项目经理
                    var fx = returnJson.fx
                    var zx = returnJson.zx
                    WfForm.changeFieldValue(sjxmjl, {
                        value: fx.fxxmjl,
                        specialobj: [{id: fx.fxxmjl, name: fx.fxxmName}]
                    });
                    WfForm.changeFieldValue(xmjl, {
                        value: zx.zxxmjl,
                        specialobj: [{id: zx.zxxmjl, name: zx.zxxmName}]
                    });
                }
            }
        });
    });

    //提交校验
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //校验。
        if (checkBudData()) {
            callback();
        }
    });

    var zxkm = WfForm.convertFieldNameToId("zxkm", "detail_1", true);//子项/孙子项
    var bxjermb = WfForm.convertFieldNameToId("bxjermb", "detail_1", true);//报销金额人民币
    WfForm.bindDetailFieldChangeEvent(zxkm + "," + bxjermb, function (id, rowIndex, value) {
        getApportionData();
    });


});

function getBorrowData() {
    //根据申请人带出借款历史明细
    var shenqr = WfForm.convertFieldNameToId("shenqr", "main", true);//申请人
    var shenqrV = WfForm.getFieldValue(shenqr);
    var requestId = WfForm.getBaseInfo().requestid;
    var rowids4 = WfForm.getDetailRowCount("detail_4")
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/cmd.jsp",
        data: 'cmd=getBorrow&userid=' + shenqrV + "&&requestId=" + requestId,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            // console.log("returnJson === ", returnJson);
            if (returnJson != null && returnJson != undefined) {
                var jksqh = WfForm.convertFieldNameToId("jksqh", "detail_4", true);//借款申请号
                var jkje = WfForm.convertFieldNameToId("jkje", "detail_4", true);//借款金额
                var lkfs = WfForm.convertFieldNameToId("lkfs", "detail_4", true);//领款方式
                var rq = WfForm.convertFieldNameToId("jkrq", "detail_4", true);//借款日期
                var jkye = WfForm.convertFieldNameToId("jkye", "detail_4", true);//借款余额
                var jkid = WfForm.convertFieldNameToId("jkid", "detail_4", true);//借款id
                if (rowids4 == 0) {
                    WfForm.delDetailRow("detail_4", "all");
                    for (var i in returnJson) {
                        var data = returnJson[i]
                        WfForm.addDetailRow("detail_4", {
                            [jksqh]: {value: data.requestcode},
                            [jkje]: {value: data.money},
                            // [hkcxje]: {value: parseFloat(data.moneyed) + parseFloat(data.moneying)},
                            [lkfs]: {value: data.payment},
                            [rq]: {value: data.brodate},
                            [jkye]: {value: data.moneylast},
                            [jkid]: {value: data.requestid},
                        });
                    }
                } else {
                    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",")
                    var jkids = [];
                    for (var i in index4) {
                        if (i == 'remove') {
                            continue;
                        }
                        var jkidV = WfForm.getFieldValue(jkid + "_" + index4[i]);
                        if (jkidV != '') {
                            jkids.push(jkidV)
                        }
                        for (var i in returnJson) {
                            var data = returnJson[i]
                            if (jkidV == data.requestid) {
                                //更新借款数据
                                WfForm.changeFieldValue(jksqh + "_" + index4[i], {value: data.requestcode});
                                WfForm.changeFieldValue(jkje + "_" + index4[i], {value: data.money});
                                WfForm.changeFieldValue(lkfs + "_" + index4[i], {value: data.payment});
                                WfForm.changeFieldValue(rq + "_" + index4[i], {value: data.brodate});
                                WfForm.changeFieldValue(jkye + "_" + index4[i], {value: data.moneylast});
                                break;
                            }
                        }
                    }
                    for (var i in returnJson) {
                        var data = returnJson[i]
                        if (jkids.indexOf(data.requestid) > -1) continue;
                        //添加新增的借款数据
                        WfForm.addDetailRow("detail_4", {
                            [jksqh]: {value: data.requestcode},
                            [jkje]: {value: data.money},
                            [lkfs]: {value: data.payment},
                            [rq]: {value: data.brodate},
                            [jkye]: {value: data.moneylast},
                            [jkid]: {value: data.requestid},
                        });
                    }
                }
            }
        }
    });

}

function checkBudData() {
    var f = true;
    var bxjehj = WfForm.convertFieldNameToId("bxjehj", "main", true);//报销金额合计
    var dqjkye = WfForm.convertFieldNameToId("dqjkye", "main", true);//当前借款余额
    var bchkje = WfForm.convertFieldNameToId("bchkje", "main", true);//本次还款金额
    var bxjehjV = WfForm.getFieldValue(bxjehj);
    var dqjkyeV = WfForm.getFieldValue(dqjkye);
    var bchkjeV = WfForm.getFieldValue(bchkje);

    var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_4", true);//还款/冲销金额
    var jkye = WfForm.convertFieldNameToId("jkye", "detail_4", true);//借款余额
    var jksqh = WfForm.convertFieldNameToId("jksqh", "detail_4", true);//借款申请号
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i])//还款/冲销金额
        var jkyeV = WfForm.getFieldValue(jkye + "_" + index4[i])//借款余额
        if (hkcxjeV != '' && parseFloat(hkcxjeV) > parseFloat(jkyeV)) {
            f = false;
            var jksqhV = WfForm.getFieldValue(jksqh + "_" + index4[i])//借款申请号
            antd.Modal.error({
                title: '提示！',
                content: "借款申请号:" + jksqhV + "中的还款/冲销金额不能大于借款余额。",
                onOk: function () {
                }
            });
            return f;
        }
    }
    if (parseFloat(bchkjeV) > parseFloat(bxjehjV)) {
        //本次还款金额大于报销金额
        f = false;
        antd.Modal.error({
            title: '提示！',
            content: "本次还款金额不能大于报销金额合计。",
            onOk: function () {
            }
        });
        return f;
    }
    if (parseFloat(bchkje) > parseFloat(dqjkyeV)) {
        f = false;
        antd.Modal.error({
            title: '提示！',
            content: "本次还款金额不能大于当前借款余额。",
            onOk: function () {
            }
        });
        return f;
    }

    return f;
}

var ftmxList = [];//分摊明细信息
var zxkmList = [];//子项孙子项
var mxxmList = [];

//获取分摊明细
function getApportionData() {
    WfForm.delDetailRow("detail_5", "all");//清空分摊明细
    ftmxList = [];//初始化
    zxkmList = [];
    mxxmList = [];
    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",")
    var zxkm = WfForm.convertFieldNameToId("zxkm", "detail_1", true);//子项/孙子项
    var bxjermb = WfForm.convertFieldNameToId("bxjermb", "detail_1", true);//报销金额人民币
    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_5", true);//分摊部门
    var lxjyje = WfForm.convertFieldNameToId("lxjyje", "detail_5", true);//立项结余金额
    var bclxje = WfForm.convertFieldNameToId("bclxje", "detail_5", true);//本次立项金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_5", true);//分摊比率
    var ftbmfzr = WfForm.convertFieldNameToId("ftbmfzr", "detail_5", true);//分摊部门负责人
    var xm = WfForm.convertFieldNameToId("xm", "detail_5", true);//项目
    //先计算项目对应的总金额
    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var zxkmV = WfForm.getFieldValue(zxkm + "_" + index1[i]);
        if (zxkmV == '') {
            continue;
        }
        var bxjermbV = WfForm.getFieldValue(bxjermb + "_" + index1[i]);
        if (ftmxList.length == 0) {
            ftmxList.push({
                id: zxkmV,
                sumMoney: bxjermbV,
                leg: 0,
                sumLeg: 1,
                jeSum: 0.00,
            })
            zxkmList.push(zxkmV)
        } else {
            var index = zxkmList.indexOf(zxkmV)
            if (index != -1) {
                //存在则更新金额
                ftmxList[index].sumMoney = parseFloat(ftmxList[index].sumMoney) + parseFloat(bxjermbV)
            } else {
                ftmxList.push({
                    id: zxkmV,
                    sumMoney: bxjermbV,
                    leg: 0,
                    sumLeg: 1,
                    jeSum: 0.00,
                })
                zxkmList.push(zxkmV)
            }
        }
    }
    //带出分摊明细
    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var zxkmV = WfForm.getFieldValue(zxkm + "_" + index1[i]);
        if (zxkmV == '') {
            continue;
        }
        var zxkmN = WfForm.getBrowserShowName(zxkm + "_" + index1[i]);
        if (mxxmList.length == 0) {
            mxxmList.push(zxkmV)
        } else {
            var index = mxxmList.indexOf(zxkmV)
            if (index != -1) {
                continue;
            } else {
                mxxmList.push(zxkmV)
            }
        }
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/cmd.jsp",
            data: 'cmd=getApportionData&id=' + zxkmV,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    for (var i in returnJson) {
                        var data = returnJson[i]
                        WfForm.addDetailRow("detail_5", {
                            [ftbm]: {value: data.ftbm, specialobj: [{id: data.ftbm, name: data.ltext},]},//分摊部门
                            [ftbl]: {value: data.ftbl},
                            [ftbmfzr]: {value: data.ftbmfzr, specialobj: [{id: data.ftbmfzr, name: data.lastname},]},//分摊部门负责人
                            [xm]: {value: zxkmV, specialobj: [{id: zxkmV, name: zxkmN},]},//项目
                        });
                        var index = zxkmList.indexOf(zxkmV)
                        if (index != -1) {
                            //存在则更新金额
                            ftmxList[index].leg = parseFloat(ftmxList[index].leg) + 1
                        }
                    }
                }
            }
        });
    }
    var index5 = WfForm.getDetailAllRowIndexStr("detail_5").split(",")
    //计算分摊金额
    for (var i in index5) {
        if (i == 'remove') {
            continue;
        }
        var xmV = WfForm.getFieldValue(xm + "_" + index5[i]);
        var ftblV = WfForm.getFieldValue(ftbl + "_" + index5[i]);
        var index = zxkmList.indexOf(xmV)
        var sumMoney = ftmxList[index].sumMoney
        var bclxjeV = (parseFloat(sumMoney) * (parseFloat(ftblV) / 100)).toFixed(2)
        if (ftmxList[index].leg == ftmxList[index].sumLeg) {
            WfForm.changeFieldValue(bclxje + "_" + index5[i], {value: sumMoney - parseFloat(formatDecimal(ftmxList[index].jeSum, 2))});
        } else {
            ftmxList[index].sumLeg = ftmxList[index].sumLeg + 1
            ftmxList[index].jeSum = (parseFloat(ftmxList[index].jeSum) + parseFloat(bclxjeV)).toFixed(2)
            WfForm.changeFieldValue(bclxje + "_" + index5[i], {value: bclxjeV});
        }
    }
}

function formatDecimal(num, decimal) {
    num = num.toString()
    let index = num.indexOf('.')
    if (index !== -1) {
        num = num.substring(0, decimal + index + 1)
    } else {
        num = num.substring(0)
    }
    return parseFloat(num).toFixed(decimal)
}

