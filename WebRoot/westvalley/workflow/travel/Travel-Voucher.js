/***
 * 引用流程：差旅费费用报销
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

var ftxxList = [];//分摊信息
var xmxxList = [];//项目信息
function loadVoucherDetail() {
    var shenqr = WfForm.convertFieldNameToId("shenqr", "main", true);//申请人名字
    var shenqrName = WfForm.getBrowserShowName(shenqr);//申请人名字

    var fycdbm = WfForm.convertFieldNameToId("fycdbm", "main", true);//费用承担部门
    var fycdbmV = WfForm.getFieldValue(fycdbm);
    var fycdbmName = WfForm.getBrowserShowName(fycdbm);
    var cbzx = "";
    var cbzxName = "";
    //根据费用承担部门带出成本中心
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/borrow/Borrow.jsp",
        data: 'cmd=getDepartment&bmbm=' + fycdbmV,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson != null && returnJson != undefined) {
                cbzx = returnJson.sapcbzxbm
                cbzxName = returnJson.sapcbzxmc
            }
        }
    });

    var gys = "";
    var gysName = "";
    var gh = WfForm.convertFieldNameToId("gonghao", "main", true);//工号
    var ghV = WfForm.getFieldValue(gh);
    //带出人员
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
    var bt = jQuery("#requestname").val();
    var jzm2 = WfForm.convertFieldNameToId("jzm", "detail_2", true);//记账码
    var tbzz2 = WfForm.convertFieldNameToId("tbzz", "detail_2", true);//特别总账
    var zy2 = WfForm.convertFieldNameToId("zy", "detail_2", true);//摘要
    var hjkm2 = WfForm.convertFieldNameToId("hjkm", "detail_2", true);//会计科目
    var hsxm2 = WfForm.convertFieldNameToId("hsxm", "detail_2", true);//核算项目
    var jfje2 = WfForm.convertFieldNameToId("jfje", "detail_2", true);//借方金额
    var cbzx2 = WfForm.convertFieldNameToId("cbzx", "detail_2", true);//成本中心
    var ry2 = WfForm.convertFieldNameToId("ry", "detail_2", true);//人员
    var bz2 = WfForm.convertFieldNameToId("bz", "detail_2", true);//备注
    var ywlx2 = WfForm.convertFieldNameToId("ywlx", "detail_2", true);//业务类型
    WfForm.delDetailRow("detail_2", "all");//清空明细
    WfForm.delDetailRow("detail_3", "all");//清空明细
    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var cfdd = WfForm.convertFieldNameToId("cfdd", "detail_1", true);//出发地点
    var dddd = WfForm.convertFieldNameToId("dddd", "detail_1", true);//到达地点
    var ywlx = WfForm.convertFieldNameToId("ywlx", "detail_1", true);//业务类型
    var bxjermb = WfForm.convertFieldNameToId("bxjermb", "detail_1", true);//报销金额人民币
    var sjje = WfForm.convertFieldNameToId("sjje", "detail_1", true);//税金金额
    var fplx = WfForm.convertFieldNameToId("fplx", "detail_1", true);//发票类型
    var sl = WfForm.convertFieldNameToId("sl", "detail_1", true);//税率
    var xmbm = WfForm.convertFieldNameToId("xmbm", "detail_1", true);//项目编码
    var zxkm = WfForm.convertFieldNameToId("zxkm", "detail_1", true);//子项/孙子项
    var ywlx1 = WfForm.convertFieldNameToId("ywlx", "detail_1", true);//业务类型

    var sfyzzszyfp = WfForm.convertFieldNameToId("sfyzzszyfp", "detail_1", true);//是否增值税专用发票

    var gsbm = WfForm.convertFieldNameToId("gsbm", "main", true);//公司编码
    var gsbmV = WfForm.getFieldValue(gsbm);//公司编码

    ftxxList = [];//初始化
    xmxxList = [];
    var index5 = WfForm.getDetailAllRowIndexStr("detail_5").split(",");
    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_5", true);//分摊部门
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_5", true);//分摊比率
    var xm = WfForm.convertFieldNameToId("xm", "detail_5", true);//项目
    for (var i in index5) {
        if (i == 'remove') {
            continue;
        }
        var xmV = WfForm.getFieldValue(xm + "_" + index5[i]);
        var ftblV = WfForm.getFieldValue(ftbl + "_" + index5[i]);
        var ftbmN = WfForm.getBrowserShowName(ftbm + "_" + index5[i]);
        if (xmxxList.length == 0) {
            xmxxList.push(xmV)
            ftxxList.push({
                id: xmV,
                data: [{ftbm: ftbmN, ftbl: ftblV}],
                count: 1,
            })
        } else {
            var index = xmxxList.indexOf(xmV)
            if (index != -1) {
                //存在则更新金额
                var data = ftxxList[index].data
                data.push({ftbm: ftbmN, ftbl: ftblV})
                ftxxList[index].data = data;
                ftxxList[index].count = parseFloat(ftxxList[index].count) + 1;
            } else {
                xmxxList.push(xmV)
                ftxxList.push({
                    id: xmV,
                    data: [{ftbm: ftbmN, ftbl: ftblV}],
                    count: 1,
                })
            }
        }
    }

    for (var j in ftxxList) {
        if (j == 'remove') {
            continue;
        }
        for (var i in index1) {
            if (i == 'remove') {
                continue;
            }
            var zxkmV = WfForm.getFieldValue(zxkm + "_" + index1[i])//子项/孙子项
            if (ftxxList[j].id != zxkmV) {
                continue;
            }
            var cfddV = WfForm.getFieldValue(cfdd + "_" + index1[i])//出发地
            var ddddV = WfForm.getFieldValue(dddd + "_" + index1[i])//到达地
            var ywlxV1 = WfForm.getFieldValue(ywlx + "_" + index1[i])//业务类型
            var ywlxV2 = WfForm.getBrowserShowName(ywlx + "_" + index1[i])//业务类型
            var ywlx1N = WfForm.getBrowserShowName(ywlx1 + "_" + index1[i])//业务类型
            var bxjermbV = WfForm.getFieldValue(bxjermb + "_" + index1[i])//报销人民币金额
            var xmbmV = WfForm.getFieldValue(xmbm + "_" + index1[i])//项目编码
            var saphjkmbm = '';
            var saphjkmmc = '';
            jQuery.ajax({
                type: "POST",
                url: "/westvalley/workflow/cmd.jsp",
                data: 'cmd=getYwlx&ywlx=' + ywlxV1,
                dataType: "text",
                async: false,
                success: function (data) {
                    var returnJson = eval("(" + data + ")");
                    if (returnJson != null && returnJson != undefined) {
                        saphjkmbm = returnJson.saphjkmbm
                        saphjkmmc = returnJson.saphjkmmc
                    }
                }
            });
            var count = ftxxList[j].count;
            var sumFtbl = 0;
            var sumMoney = 0;
            var sumXM = 1;
            var sfyzzszyfpV = WfForm.getFieldValue(sfyzzszyfp + "_" + index1[i])//是否增值税专用发票
            var sjjeV = WfForm.getFieldValue(sjje + "_" + index1[i])//税额
            if (sfyzzszyfpV == "0") {
                bxjermbV = parseFloat(bxjermbV) - parseFloat(sjjeV)
            }
            var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
            // var zy = "费用报销：付_" + shenqrName + "_" + cfddV + "-" + ddddV + " " + ywlxV2;//摘要
            var data = ftxxList[j].data
            for (var k in data) {
                if (k == 'remove') {
                    continue;
                }
                var ftbmN = data[k].ftbm
                var ftbl = data[k].ftbl
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
                var money = (bxjermbV * (parseFloat(ftbl) / 100)).toFixed(2)
                sumFtbl += parseFloat(ftbl)
                if (sumXM == count) {
                    money = parseFloat(bxjermbV) - parseFloat(sumMoney);
                } else {
                    sumMoney += parseFloat(money);
                }

                WfForm.addDetailRow("detail_2", {
                    [zy2]: {value: zy},//摘要
                    [jzm2]: {value: "40"},//记账码
                    [hjkm2]: {value: saphjkmbm, specialobj: [{id: saphjkmbm, name: saphjkmmc},]},//会计科目
                    [hsxm2]: {value: xmbmV},//核算项目
                    [jfje2]: {value: money},//借方金额
                    [cbzx2]: {value: bm, specialobj: [{id: bm, name: mc},]},//成本中心
                    [ry2]: {value: gys, specialobj: [{id: gys, name: gysName},]},//人员
                    [ywlx2]: {value: ywlx1N},//业务类型
                });
                sumXM++;
            }

            if (sfyzzszyfpV == "1") continue;
            if (parseFloat(sjjeV) > 0) {
                //有税额
                var sjjeV = WfForm.getFieldValue(sjje + "_" + index1[i])//报销人民币金额
                var fplxV = WfForm.getBrowserShowName(fplx + "_" + index1[i])//发票类型
                var fplxV2 = WfForm.getFieldValue(fplx + "_" + index1[i])//发票类型
                var slV = WfForm.getBrowserShowName(sl + "_" + index1[i])//税率
                var ywlxV1 = WfForm.getFieldValue(ywlx + "_" + index1[i])//业务类型
                var ywlxV2 = WfForm.getBrowserShowName(ywlx + "_" + index1[i])//业务类型

                //根据发票类型带出会计科目
                var sxbm = "";
                var sjkm = "";
                jQuery.ajax({
                    type: "POST",
                    url: "/westvalley/workflow/cmd.jsp",
                    data: 'cmd=getFplx&fplx=' + fplxV2,
                    dataType: "text",
                    async: false,
                    success: function (data) {
                        var returnJson = eval("(" + data + ")");
                        if (returnJson != null && returnJson != undefined) {
                            sxbm = returnJson.sxbm
                            sjkm = returnJson.sjkm
                        }
                    }
                });

                // var zyV = "报销项目_" + fplxV + "_" + slV;
                var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
                WfForm.addDetailRow("detail_2", {
                    [zy2]: {value: zy},//摘要
                    [jzm2]: {value: "40"},//记账码
                    [hsxm2]: {value: xmbmV},//核算项目
                    [hjkm2]: {value: sxbm, specialobj: [{id: sxbm, name: sjkm},]},//会计科目
                    [jfje2]: {value: sjjeV},//税额
                });

            }

        }
    }

    var bchkje = WfForm.convertFieldNameToId("bchkje", "main", true);//本次还款金额
    var bchkjeV = WfForm.getFieldValue(bchkje);
    var sjzfje = WfForm.convertFieldNameToId("sjzfje", "main", true);//实际支付金额
    var sjzfjeV = WfForm.getFieldValue(sjzfje);

    var liucbh = WfForm.convertFieldNameToId("liucbh", "main", true);//流程编号
    var liucbhV = WfForm.getFieldValue(liucbh);
    var bxsy = WfForm.convertFieldNameToId("bxsy", "main", true);//报销事由
    var bxsyV = WfForm.getFieldValue(bxsy).substr(0, 20);

    var bxjehj = WfForm.convertFieldNameToId("bxjehj", "main", true);//报销金额合计
    var bxjehjV = WfForm.getFieldValue(bxjehj);

    var jzm3 = WfForm.convertFieldNameToId("jzm", "detail_3", true);//记账码
    var tbzz3 = WfForm.convertFieldNameToId("tbzz", "detail_3", true);//特别总账
    var zy3 = WfForm.convertFieldNameToId("zy", "detail_3", true);//摘要
    var hjkm3 = WfForm.convertFieldNameToId("hjkm", "detail_3", true);//会计科目
    var hsxm3 = WfForm.convertFieldNameToId("hsxm", "detail_3", true);//核算项目
    var dfje3 = WfForm.convertFieldNameToId("dfje", "detail_3", true);//贷方金额
    var gys3 = WfForm.convertFieldNameToId("gys", "detail_3", true);//成本中心
    var xjl3 = WfForm.convertFieldNameToId("xjl", "detail_3", true);//现金流
    var lrzx3 = WfForm.convertFieldNameToId("lrzx", "detail_3", true);//利润中心
    var ry3 = WfForm.convertFieldNameToId("ry", "detail_3", true);//人员
    if (parseFloat(bchkjeV) > 0) {
        //冲借款
        var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_4", true);//还款/核销金额
        var jksqh = WfForm.convertFieldNameToId("jksqh", "detail_4", true);//借款单号
        var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
        if (parseFloat(bchkjeV) >= parseFloat(bxjehjV)) {
            //本次还款金额大于等于报销金额
            for (var i in index4) {
                if (i == 'remove') {
                    continue;
                }
                var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i])//还款/核销金额
                if (hkcxjeV == '' || hkcxjeV == "0" || parseFloat(hkcxjeV) == 0) {
                    continue;
                }
                var jksqhV = WfForm.getFieldValue(jksqh + "_" + index4[i])//借款单号
                var zy = liucbhV + "_" + shenqrName + "_" + bxsyV + "_" + jksqhV;

                WfForm.addDetailRow("detail_3", {
                    [zy3]: {value: zy},//摘要gys
                    [jzm3]: {value: "31"},//记账码
                    [hjkm3]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"},]},//会计科目
                    [ry3]: {value: gys, specialobj: [{id: gys, name: gysName},]},//人员
                    [dfje3]: {value: hkcxjeV},//贷方金额
                });
            }

        } else {
            var hkjeSum = 0.00;
            for (var i in index4) {
                if (i == 'remove') {
                    continue;
                }
                var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i])//还款/核销金额
                if (hkcxjeV == '' || hkcxjeV == "0" || parseFloat(hkcxjeV) == 0) {
                    continue;
                }
                var jksqhV = WfForm.getFieldValue(jksqh + "_" + index4[i])//借款单号
                // var zy = liucbhV + "_" + shenqrName + "_" + bxsyV + "_" + jksqhV;
                var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
                hkjeSum += parseFloat(hkcxjeV);
                WfForm.addDetailRow("detail_3", {
                    [zy3]: {value: zy},//摘要gys
                    [jzm3]: {value: "31"},//记账码
                    [hjkm3]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"},]},//会计科目
                    [gys3]: {value: gys, specialobj: [{id: gys, name: gysName},]},//供应商
                    [dfje3]: {value: parseFloat(hkcxjeV)},//贷方金额
                    [ry3]: {value: gys, specialobj: [{id: gys, name: gysName},]},//人员
                });
            }
            // var zyV = liucbhV + "_" + shenqrName + "_" + bxsyV;
            var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
            WfForm.addDetailRow("detail_3", {
                [zy3]: {value: zy},//摘要
                [jzm3]: {value: "50"},//记账码
                [hjkm3]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
                [dfje3]: {value: sjzfjeV},//贷方金额
                [xjl3]: {value: "B4"},//现金流
            });
        }
    } else {
        //无借款直接报销
        // var zyV = liucbhV + "_" + shenqrName + "_" + bxsyV;
        var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
        WfForm.addDetailRow("detail_3", {
            [zy3]: {value: zy},//摘要
            [jzm3]: {value: "50"},//记账码
            [hjkm3]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
            [dfje3]: {value: sjzfjeV},//贷方金额
            [xjl3]: {value: "B4"},//现金流
        });
    }
}
