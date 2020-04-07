/***
 * 引用流程：项目其他费用报销流程
 * 引用节点：申请节点
 * 主要功能：1.带出费用归属部门 2.报销类别控制隐藏 3.带出历史借款记录
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
    var bxlb = WfForm.convertFieldNameToId("bxlb", "main", true);//报销类别
    var bxlbV = WfForm.getFieldValue(bxlb);
    concealData(bxlbV);//初始化
    WfForm.bindFieldChangeEvent(bxlb, function (obj, id, value) {
        concealData(value);
        getSumMoney();
    });

    getSumMoney();
    var bxjecny = WfForm.convertFieldNameToId("bxjecny", "detail_1");//报销金额CNY
    WfForm.bindDetailFieldChangeEvent(bxjecny, function (id, idx, value) {
        getSumMoney();
    });
    var ywzdje = WfForm.convertFieldNameToId("ywzdje", "detail_5");//业务招待金额CNY
    WfForm.bindDetailFieldChangeEvent(ywzdje, function (id, idx, value) {
        getSumMoney();
    });

    var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_4", true);//还款/冲销金额


  /*var zxszx = WfForm.convertFieldNameToId("zxszx", "detail_4", true);//子项/孙子项
    var zxszxbh = WfForm.convertFieldNameToId("zxszxbh", "detail_6", true);//子项/孙子项编号
    var ftje = WfForm.convertFieldNameToId("ftje", "detail_6", true);//分摊金额
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var index6 = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
    $("div[name='xmxxft']")[1].style.display = 'block';
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i]);
        if (hkcxjeV == "" || parseFloat(hkcxjeV) == 0) {
            continue;
        }
        for (var j in index6) {
            if (j == 'remove') {
                continue;
            }
            WfForm.changeFieldAttr(zxszxbh + "_" + index6[j], 1); //字段修改为只读
            WfForm.changeFieldAttr(ftje + "_" + index6[j], 1); //字段修改为只读
        }
        $("div[name='xmxxft']")[1].style.display = 'none';
    }
    WfForm.bindDetailFieldChangeEvent(hkcxje, function (id, rowIndex, value) {
        resetXMXXFT()
    });*/


    var zxszxbh = WfForm.convertFieldNameToId("zxszxbh", "detail_6", true);//子项/孙子项项目编号
    var kyje = WfForm.convertFieldNameToId("kyje", "detail_6", true);//可用金额
    //带出子项/孙子项项目信息
    WfForm.bindDetailFieldChangeEvent(zxszxbh, function (id, rowIndex, value) {
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
                        WfForm.changeFieldValue(kyje + "_" + rowIndex, {value: returnJson.money});
                    } else {
                        WfForm.changeFieldValue(kyje + "_" + rowIndex, {value: 0.00});
                    }

                }
            }
        });
        getApportionData();
    });

    var ftje = WfForm.convertFieldNameToId("ftje", "detail_6");//分摊金额
    WfForm.bindDetailFieldChangeEvent(ftje, function (id, idx, value) {
        flushPrecent();
        getApportionData();
    });

    //提交校验
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //校验。
        if (checkBudData()) {
            callback();
        }
    });
});

function resetXMXXFT() {
    var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_4", true);//还款/冲销金额
    var zxszx = WfForm.convertFieldNameToId("zxszx", "detail_4", true);//子项/孙子项
    var zxszxbh = WfForm.convertFieldNameToId("zxszxbh", "detail_6", true);//子项/孙子项编号
    var ftje = WfForm.convertFieldNameToId("ftje", "detail_6", true);//分摊金额
    WfForm.delDetailRow("detail_6", "all");//清空其它报销明细
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    $("div[name='xmxxft']")[1].style.display = 'block';
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i]);
        if (hkcxjeV == "" || parseFloat(hkcxjeV) == 0) {
            continue;
        }
        var zxszxV = WfForm.getFieldValue(zxszx + "_" + index4[i]);
        var zxszxN = WfForm.getBrowserShowName(zxszx + "_" + index4[i]);
        WfForm.addDetailRow("detail_6");
        var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
        dex = dex[dex.length - 1]
        WfForm.changeFieldValue(ftje + "_" + dex, {value: hkcxjeV})
        WfForm.changeFieldValue(zxszxbh + "_" + dex, {
            value: zxszxV,
            specialobj: [{id: zxszxV, name: zxszxN},]
        });
        WfForm.changeFieldAttr(zxszxbh + "_" + dex, 1); //字段修改为只读
        WfForm.changeFieldAttr(ftje + "_" + dex, 1); //字段修改为只读
        $("div[name='xmxxft']")[1].style.display = 'none';
    }
}

function checkBudData() {
    var f = true;
    var bxjehj = WfForm.convertFieldNameToId("bxjehj", "main", true);//报销金额合计
    var bxjehjV = WfForm.getFieldValue(bxjehj);//报销金额合计
    var bchxje = WfForm.convertFieldNameToId("bchxje", "main", true);//本次核销金额
    var bchxjeV = WfForm.getFieldValue(bchxje);//本次核销金额

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

    if (parseFloat(bchxjeV) > parseFloat(bxjehjV)) {
        //本次还款金额大于报销金额
        f = false;
        antd.Modal.error({
            title: '提示！',
            content: "本次核销金额不能大于报销金额合计。",
            onOk: function () {
            }
        });
        return f;
    }
    var ftzje = WfForm.convertFieldNameToId("ftzje");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);
    if (parseFloat(ftzjeV) != parseFloat(bxjehjV)) {
        //报销总金额和分摊总金额不相等
        f = false;
        antd.Modal.error({
            title: '提示！',
            content: "报销总金额和分摊总金额不相等。",
            onOk: function () {
            }
        });
        return f;
    }
    var bxlb = WfForm.convertFieldNameToId("bxlb", "main", true);//报销类别
    var bxlbV = WfForm.getFieldValue(bxlb);
    if (bxlbV == '0') {
        //招待费
        WfForm.delDetailRow("detail_1", "all");//清空其它报销明细
    } else if (bxlbV == '1') {
        //其他费用报销
        WfForm.delDetailRow("detail_5", "all");//清空招待费明细
    }
    getSumMoney();
    return f;
}

//刷新分摊比例
function flushPrecent() {
    var ftzje = WfForm.convertFieldNameToId("ftzje");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);

    var ftje = WfForm.convertFieldNameToId("ftje", "detail_6");//本次立项金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_6");//分摊比例

    var rowids = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
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

//根据报销类型控制隐藏明细
function concealData(value) {
    var ywyskm = WfForm.convertFieldNameToId("ywyskm", "detail_1", true);//业务（预算）科目
    var ybje = WfForm.convertFieldNameToId("ybje", "detail_1", true);//原币金额
    var ybbz = WfForm.convertFieldNameToId("ybbz", "detail_1", true);//原币币种
    var hl = WfForm.convertFieldNameToId("hl", "detail_1", true);//汇率
    var bxjecny = WfForm.convertFieldNameToId("bxjecny", "detail_1", true);//报销金额CNY
    var fplx = WfForm.convertFieldNameToId("fplx", "detail_1", true);//发票类型
    var sfzzszyfp = WfForm.convertFieldNameToId("sfzzszyfp", "detail_1", true);//是否增值税专用发票
    var rowids1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");

    var zddwjzdry = WfForm.convertFieldNameToId("zddwjzdry", "detail_5", true);//招待单位及招待人员
    var fkzw = WfForm.convertFieldNameToId("fkzw", "detail_5", true);//访客职位
    var cmzdzjptry = WfForm.convertFieldNameToId("cmzdzjptry", "detail_5", true);//出面招待者及陪同人员
    var zdlb = WfForm.convertFieldNameToId("zdlb", "detail_5", true);//招待类别
    var ybje5 = WfForm.convertFieldNameToId("ybje", "detail_5", true);//原币金额
    var ybbb = WfForm.convertFieldNameToId("ybbb", "detail_5", true);//原币币别
    var hl5 = WfForm.convertFieldNameToId("hl", "detail_5", true);//汇率
    var hjkm5 = WfForm.convertFieldNameToId("hjkm", "detail_5", true);//会计科目
    var ywzdje = WfForm.convertFieldNameToId("ywzdje", "detail_5", true);//业务招待金额CNY
    var rowids5 = WfForm.getDetailAllRowIndexStr("detail_5").split(",");
    //改变字段的状态，1：只读，2：可编辑，3：必填，4：隐藏字段标签及内
    if (value == '0') {
        for (var i in rowids1) {
            if (i == 'remove') {
                continue;
            }
            WfForm.changeFieldAttr(ywyskm + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(ybje + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(ybbz + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(hl + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(bxjecny + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(fplx + "_" + rowids1[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(sfzzszyfp + "_" + rowids1[i], 1); //字段修改为只读
        }
        for (var i in rowids5) {
            if (i == 'remove') {
                continue;
            }
            WfForm.changeFieldAttr(zddwjzdry + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(fkzw + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(cmzdzjptry + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(zdlb + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(ybje5 + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(ybbb + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(hl5 + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(ywzdje + "_" + rowids5[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(hjkm5 + "_" + rowids5[i], 3); //字段修改为必填
        }
    } else if (value == "1") {
        for (var i in rowids1) {
            if (i == 'remove') {
                continue;
            }
            WfForm.changeFieldAttr(ywyskm + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(ybje + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(ybbz + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(hl + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(bxjecny + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(fplx + "_" + rowids1[i], 3); //字段修改为必填
            WfForm.changeFieldAttr(sfzzszyfp + "_" + rowids1[i], 3); //字段修改为必填
        }
        for (var i in rowids5) {
            if (i == 'remove') {
                continue;
            }
            WfForm.changeFieldAttr(zddwjzdry + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(fkzw + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(cmzdzjptry + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(zdlb + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(ybje5 + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(ybbb + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(hl5 + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(ywzdje + "_" + rowids5[i], 1); //字段修改为只读
            WfForm.changeFieldAttr(hjkm5 + "_" + rowids5[i], 1); //字段修改为只读

        }
    }
}

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
                var zxszx = WfForm.convertFieldNameToId("zxszx", "detail_4", true);//子项/孙子项
                if (rowids4 == 0) {
                    WfForm.delDetailRow("detail_4", "all");
                    for (var i in returnJson) {
                        var data = returnJson[i]
                        WfForm.addDetailRow("detail_4");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldValue(jksqh + "_" + dex, {value: data.requestcode});
                        WfForm.changeFieldValue(jkje + "_" + dex, {value: data.money});
                        WfForm.changeFieldValue(lkfs + "_" + dex, {value: data.payment});
                        WfForm.changeFieldValue(rq + "_" + dex, {value: data.brodate});
                        WfForm.changeFieldValue(jkye + "_" + dex, {value: data.moneylast});
                        WfForm.changeFieldValue(jkid + "_" + dex, {value: data.requestid});
                        WfForm.changeFieldValue(zxszx + "_" + dex, {value: data.projid});
                    }
                } else {
                    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",")
                    var jkids = [];
                    for (var i in index4) {
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
                                WfForm.changeFieldValue(zxszx + "_" + dex, {value: data.projid});
                                break;
                            }
                        }
                    }
                    for (var i in returnJson) {
                        var data = returnJson[i]
                        if (jkids.indexOf(data.requestid) > -1) continue;
                        //添加新增的借款数据
                        WfForm.addDetailRow("detail_4");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldValue(jksqh + "_" + dex, {value: data.requestcode});
                        WfForm.changeFieldValue(jkje + "_" + dex, {value: data.money});
                        WfForm.changeFieldValue(lkfs + "_" + dex, {value: data.payment});
                        WfForm.changeFieldValue(rq + "_" + dex, {value: data.brodate});
                        WfForm.changeFieldValue(jkye + "_" + dex, {value: data.moneylast});
                        WfForm.changeFieldValue(jkid + "_" + dex, {value: data.requestid});
                        WfForm.changeFieldValue(zxszx + "_" + dex, {value: data.projid});
                    }
                }
            }
        }
    });

}

//计算报销金额合计
function getSumMoney() {
    var bxlb = WfForm.convertFieldNameToId("bxlb", "main", true);//报销类别
    var bxlbV = WfForm.getFieldValue(bxlb);
    var sumMoney = 0.00;
    if (bxlbV == '0') {
        //招待费
        var rowids1 = WfForm.getDetailAllRowIndexStr("detail_5").split(",");
        var ywzdje = WfForm.convertFieldNameToId("ywzdje", "detail_5");//业务招待金额CNY
        for (var i in rowids1) {
            if (i == 'remove') {
                continue;
            }
            var ywzdjeV = WfForm.getFieldValue(ywzdje + "_" + rowids1[i]);
            sumMoney += parseFloat(ywzdjeV);
        }

    } else if (bxlbV == '1') {
        //其他费用报销
        var rowids1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        var bxjecny = WfForm.convertFieldNameToId("bxjecny", "detail_1");//报销金额CNY
        for (var i in rowids1) {
            if (i == 'remove') {
                continue;
            }
            var bxjecnyV = WfForm.getFieldValue(bxjecny + "_" + rowids1[i]);
            sumMoney += parseFloat(bxjecnyV);
        }
    }
    var bxjehj = WfForm.convertFieldNameToId("bxjehj", "main", true);//报销金额合计
    WfForm.changeFieldValue(bxjehj, {value: sumMoney});
}


var ftmxList = [];//分摊明细信息
var zxkmList = [];//子项孙子项
var mxxmList = [];

//获取分摊明细
function getApportionData() {
    WfForm.delDetailRow("detail_7", "all");//清空分摊明细
    ftmxList = [];//初始化
    zxkmList = [];
    mxxmList = [];
    var index1 = WfForm.getDetailAllRowIndexStr("detail_6").split(",")
    var zxkm = WfForm.convertFieldNameToId("zxszxbh", "detail_6", true);//子项/孙子项
    var bxjermb = WfForm.convertFieldNameToId("ftje", "detail_6", true);//报销金额人民币

    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_7", true);//分摊部门
    var lxjyje = WfForm.convertFieldNameToId("lxskyje", "detail_7", true);//立项结余金额
    var bclxje = WfForm.convertFieldNameToId("bclxje", "detail_7", true);//本次立项金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_7", true);//分摊比率
    var ftbmfzr = WfForm.convertFieldNameToId("ftbmfzr", "detail_7", true);//分摊部门负责人
    var xm = WfForm.convertFieldNameToId("xm", "detail_7", true);//项目
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
        if (bxjermbV == '') {
            bxjermbV = 0.00
        }
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
                        WfForm.addDetailRow("detail_7");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_7").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldValue(ftbm + "_" + dex, {
                            value: data.ftbm,
                            specialobj: [{id: data.ftbm, name: data.ltext},]
                        });//分摊部门
                        WfForm.changeFieldValue(ftbl + "_" + dex, {value: data.ftbl});
                        WfForm.changeFieldValue(ftbmfzr + "_" + dex, {
                            value: data.ftbmfzr,
                            specialobj: [{id: data.ftbmfzr, name: data.lastname},]
                        });
                        WfForm.changeFieldValue(xm + "_" + dex, {
                            value: zxkmV,
                            specialobj: [{id: zxkmV, name: zxkmN},]
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
    var index5 = WfForm.getDetailAllRowIndexStr("detail_7").split(",")
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
