/***
 * 引用流程：项目其他费用报销流程
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

    var liucbh = WfForm.convertFieldNameToId("liucbh", "main", true);//流程编号
    var liucbhV = WfForm.getBrowserShowName(liucbh);//流程编号
    var bxsy = WfForm.convertFieldNameToId("bxsy", "main", true);//报销事由
    var bxsyV = WfForm.getFieldValue(bxsy).substr(0, 20);//报销事由
    // var zy = liucbhV + "_" + shenqrName + "_费用报销_" + bxsyV;//摘要
    var bt = jQuery("#requestname").val();
    var zy = "费用报销：付" + shenqrName + " " + bt;//摘要
    var shenqbm = WfForm.convertFieldNameToId("shenqbm", "main", true);//申请人部门
    var shenqbmV = WfForm.getFieldValue(shenqbm);

    var fycdbm = WfForm.convertFieldNameToId("fycdbm", "main", true);//费用承担部门
    var fycdbmV = WfForm.getFieldValue(fycdbm);
    var fycdbmN = WfForm.getBrowserShowName(fycdbm);
    var lrzx = fycdbmV;
    var lrzxName = fycdbmN;
    //带出费用承担部门
    /*jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/borrow/Borrow.jsp",
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
    });*/

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


    var bxlb = WfForm.convertFieldNameToId("bxlb", "main", true);//报销类别
    var bxlbV = WfForm.getFieldValue(bxlb);//报销类别
    var jzm2 = WfForm.convertFieldNameToId("jzm", "detail_2", true);//记账码
    var tbzz2 = WfForm.convertFieldNameToId("tbzz", "detail_2", true);//特别总账
    var zy2 = WfForm.convertFieldNameToId("zy", "detail_2", true);//摘要
    var hjkm2 = WfForm.convertFieldNameToId("hjkm", "detail_2", true);//会计科目
    var hsxm2 = WfForm.convertFieldNameToId("hsxm", "detail_2", true);//核算项目
    var jfje2 = WfForm.convertFieldNameToId("jfje", "detail_2", true);//借方金额
    var fycdbm2 = WfForm.convertFieldNameToId("fycdbm", "detail_2", true);//费用承担部门
    var ry2 = WfForm.convertFieldNameToId("ry", "detail_2", true);//人员
    var ywlx2 = WfForm.convertFieldNameToId("ywlx", "detail_2", true);//业务类型


    WfForm.delDetailRow("detail_2", "all");//清空明细
    WfForm.delDetailRow("detail_3", "all");//清空明细`

    var index7 = WfForm.getDetailAllRowIndexStr("detail_7").split(",");
    var ftbm7 = WfForm.convertFieldNameToId("ftbm", "detail_7", true);//分摊部门
    var lxjyje7 = WfForm.convertFieldNameToId("lxskyje", "detail_7", true);//立项结余金额
    var bclxje7 = WfForm.convertFieldNameToId("bclxje", "detail_7", true);//本次立项金额
    var ftbl7 = WfForm.convertFieldNameToId("ftbl", "detail_7", true);//分摊比率
    var ftbmfzr7 = WfForm.convertFieldNameToId("ftbmfzr", "detail_7", true);//分摊部门负责人
    var xm7 = WfForm.convertFieldNameToId("xm", "detail_7", true);//项目
    var bclxje7 = WfForm.convertFieldNameToId("bclxje", "detail_7", true);//项目

    var index6 = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
    var zxszxbh6 = WfForm.convertFieldNameToId("zxszxbh", "detail_6", true);//子项/孙子项编号
    var ftje6 = WfForm.convertFieldNameToId("ftje", "detail_6", true);//分摊金额
    var ftbl6 = WfForm.convertFieldNameToId("ftbl", "detail_6", true);//分摊比率

    var gsbm = WfForm.convertFieldNameToId("gongsdm", "main", true);//公司编码
    var gsbmV = WfForm.getFieldValue(gsbm);//公司编码
    var seSum = 0.00;
    var ftzje = WfForm.convertFieldNameToId("ftzje", "main");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);
    //借方分录

    ftxxList = [];//初始化
    xmxxList = [];
    var index7 = WfForm.getDetailAllRowIndexStr("detail_7").split(",");
    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_7", true);//分摊部门
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_7", true);//分摊比率
    var xm = WfForm.convertFieldNameToId("xm", "detail_7", true);//项目
    for (var i in index7) {
        if (i == 'remove') {
            continue;
        }
        var xmV = WfForm.getFieldValue(xm + "_" + index7[i]);
        var ftblV = WfForm.getFieldValue(ftbl + "_" + index7[i]);
        var ftbmN = WfForm.getBrowserShowName(ftbm + "_" + index7[i]);
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
    if (bxlbV == '1') {
        //其他费用
        var sfzzszyfp = WfForm.convertFieldNameToId("sfzzszyfp", "detail_1", true);//是否增值税专用发票
        var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");//其他报销明细
        var ywyskm = WfForm.convertFieldNameToId("ywyskm", "detail_1", true);//业务（预算）科目
        var bxjecny = WfForm.convertFieldNameToId("bxjecny", "detail_1", true);//报销金额
        var se = WfForm.convertFieldNameToId("se", "detail_1", true);//税额
        var fplx = WfForm.convertFieldNameToId("fplx", "detail_1", true);//发票类型
        var sl = WfForm.convertFieldNameToId("sl", "detail_1", true);//税率

        for (var i in index1) {
            if (i == 'remove' || index1[i] == '') {
                continue;
            }
            var ywyskmV = WfForm.getFieldValue(ywyskm + "_" + index1[i]);//业务（预算）科目
            var ywyskmN = WfForm.getBrowserShowName(ywyskm + "_" + index1[i])//业务类型

            var kjkmbm = "";
            var kjkmmc = "";
            //业务（预算）科目带出会计科目
            jQuery.ajax({
                type: "POST",
                url: "/westvalley/workflow/other/Other.jsp",
                data: 'cmd=getKjkm&id=' + ywyskmV,
                dataType: "text",
                async: false,
                success: function (data) {
                    var returnJson = eval("(" + data + ")");
                    if (returnJson != null && returnJson != undefined) {
                        kjkmbm = returnJson.kjkmbm
                        kjkmmc = returnJson.kjkmmc
                    }
                }
            });
            var bxjecnyV = WfForm.getFieldValue(bxjecny + "_" + index1[i]);//报销金额
            var sfzzszyfpV = WfForm.getFieldValue(sfzzszyfp + "_" + index1[i])//是否增值税专用发票
            var sjjeV = WfForm.getFieldValue(se + "_" + index1[i])//税额
            if (sfzzszyfpV == "0") {
                bxjecnyV = parseFloat(bxjecnyV) - parseFloat(sjjeV)
            }
            var sumMoney10 = 0;
            for (var j in index6) {
                if (j == 'remove') {
                    continue;
                }
                var zxszxbh6V = WfForm.getFieldValue(zxszxbh6 + "_" + index6[j])//子项/孙子项编号
                var zxszxbh6N = WfForm.getBrowserShowName(zxszxbh6 + "_" + index6[j])//子项/孙子项编号
                var count = 1;
                for (var l in ftxxList) {
                    if (zxszxbh6V == ftxxList[l].id) {
                        count = ftxxList[l].count
                    }
                }
                var sumDex = 1;
                var ftbl6V = WfForm.getFieldValue(ftbl6 + "_" + index6[j])//分摊比率
                var ftje6V = WfForm.getFieldValue(ftje6 + "_" + index6[j])
                ftbl6V = (ftje6V / ftzjeV * 100).toFixed(2)
                var sumFtbl = 0;
                var sumMoney = 0;
                var sumMoney6 = ((parseFloat(bxjecnyV) * (parseFloat(ftbl6V)).toFixed(2) / 100)).toFixed(2)
                var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
                dex = dex[dex.length - 1]
                if (dex == index6[j]) {
                    sumMoney6 = parseFloat(bxjecnyV) - parseFloat(sumMoney10)
                } else {
                    sumMoney10 += parseFloat(sumMoney6)
                }
                for (var k in index7) {
                    if (k == 'remove') {
                        continue;
                    }
                    var xm7V = WfForm.getFieldValue(xm7 + "_" + index7[k])//子项/孙子项编号
                    if (xm7V != zxszxbh6V) {
                        continue;
                    }
                    var ftbl7V = WfForm.getFieldValue(ftbl7 + "_" + index7[k])//分摊比率
                    var ftbmN = WfForm.getBrowserShowName(ftbm7 + "_" + index7[k])//分摊部门
                    var xm7V = WfForm.getFieldValue(xm7 + "_" + index7[k])//子项/孙子项编号
                    var ftbl7V = WfForm.getFieldValue(ftbl7 + "_" + index7[k])//分摊比率
                    var bclxje7V = WfForm.getFieldValue(bclxje7 + "_" + index7[k])//本次立项金额
                    ftbl7V = (bclxje7V / ftje6V * 100).toFixed(2)
                    if (count == sumDex) {
                        ftbl7V = 100 - parseFloat(sumFtbl)
                    } else {
                        sumFtbl += parseFloat(ftbl7V);
                    }
                    var je = (sumMoney6 * (parseFloat(ftbl7V) / 100)).toFixed(2);
                    if (count == sumDex) {
                        je = sumMoney6 - parseFloat(sumMoney)
                    } else {
                        sumMoney += parseFloat(je);
                    }
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
                    WfForm.addDetailRow("detail_2", {
                        [jzm2]: {value: "40"},
                        [zy2]: {value: zy},
                        [hsxm2]: {value: zxszxbh6N},
                        [jfje2]: {value: je},
                        [hjkm2]: {value: kjkmbm, specialobj: [{id: kjkmbm, name: kjkmmc}]},
                        [fycdbm2]: {value: bm, specialobj: [{id: bm, name: mc}]},
                        [ry2]: {value: gys, specialobj: [{id: gys, name: gysName},]},
                        [ywlx2]: {value: ywyskmN},
                    });
                    sumDex++;
                }

            }
            if (sfzzszyfpV == "1") continue;
            if (parseFloat(sjjeV) > 0) {
                //是否有增值税专用发票"为"是",则借方明细需要新增显示税额明细；
                var fplxV = WfForm.getBrowserShowName(fplx + "_" + index1[i])//发票类型
                var fplxV2 = WfForm.getFieldValue(fplx + "_" + index1[i])//发票类型
                var slV = WfForm.getBrowserShowName(sl + "_" + index1[i])//税率
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
                var zyV = "报销项目_" + fplxV + "_" + slV;
                WfForm.addDetailRow("detail_2", {
                    [zy2]: {value: zy},//摘要
                    [jzm2]: {value: "40"},//记账码
                    [hjkm2]: {value: sxbm, specialobj: [{id: sxbm, name: sjkm},]},//会计科目
                    [jfje2]: {value: sjjeV},//税额
                });
            }
        }
    } else if (bxlbV == '0') {
        //招待费
        var hjkm = WfForm.convertFieldNameToId("hjkm", "detail_5", true);//会计科目
        var ywzdje = WfForm.convertFieldNameToId("ywzdje", "detail_5", true);//业务招待金额CNY
        var index5 = WfForm.getDetailAllRowIndexStr("detail_5").split(",");//其他报销明细
        for (var i in index5) {
            if (i == 'remove') {
                continue;
            }
            var hjkmV = WfForm.getFieldValue(hjkm + "_" + index5[i]);//会计科目
            var hjkmName = WfForm.getBrowserShowName(hjkm + "_" + index5[i]);//会计科目名称
            var ywzdjeV = WfForm.getFieldValue(ywzdje + "_" + index5[i]);//业务招待金额CNY

            var sumMoney10 = 0;
            //借方分录
            for (var j in index6) {
                if (j == 'remove') {
                    continue;
                }
                var zxszxbh6V = WfForm.getFieldValue(zxszxbh6 + "_" + index6[j])//子项/孙子项编号
                var zxszxbh6N = WfForm.getBrowserShowName(zxszxbh6 + "_" + index6[j])//子项/孙子项编号
                var count = 1;
                for (var i in ftxxList) {
                    if (zxszxbh6V == ftxxList[i].id) {
                        count = ftxxList[i].count
                    }
                }

                var sumDex = 1;
                var ftbl6V = WfForm.getFieldValue(ftbl6 + "_" + index6[j])//分摊比率
                var ftje6V = WfForm.getFieldValue(ftje6 + "_" + index6[j])
                ftbl6V = (ftje6V / ftzjeV * 100).toFixed(2)
                var sumFtbl = 0;
                var sumMoney = 0;
                var sumMoney6 = ((parseFloat(ywzdjeV) * (parseFloat(ftbl6V)).toFixed(2) / 100)).toFixed(2)
                var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
                dex = dex[dex.length - 1]
                if (dex == index6[j]) {
                    sumMoney6 = parseFloat(ywzdjeV) - parseFloat(sumMoney10)
                } else {
                    sumMoney10 += parseFloat(sumMoney6)
                }
                for (var k in index7) {
                    if (k == 'remove') {
                        continue;
                    }
                    var xm7V = WfForm.getFieldValue(xm7 + "_" + index7[k])//子项/孙子项编号
                    if (xm7V != zxszxbh6V) {
                        continue;
                    }
                    var xm7V = WfForm.getFieldValue(xm7 + "_" + index7[k])//子项/孙子项编号
                    var ftbl7V = WfForm.getFieldValue(ftbl7 + "_" + index7[k])//分摊比率
                    var bclxje7V = WfForm.getFieldValue(bclxje7 + "_" + index7[k])//本次立项金额
                    ftbl7V = (bclxje7V / ftje6V * 100).toFixed(2)
                    if (count == sumDex) {
                        ftbl7V = 100 - parseFloat(sumFtbl)
                    } else {
                        sumFtbl += parseFloat(ftbl7V);
                    }
                    var ftbmN = WfForm.getBrowserShowName(ftbm7 + "_" + index7[k])//分摊部门
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
                    var je = (sumMoney6 * (parseFloat(ftbl7V) / 100)).toFixed(2);
                    if (count == sumDex) {
                        je = sumMoney6 - parseFloat(sumMoney)
                    } else {
                        sumMoney += parseFloat(je);
                    }
                    WfForm.addDetailRow("detail_2", {
                        [jzm2]: {value: "40"},
                        [zy2]: {value: zy},
                        [hsxm2]: {value: zxszxbh6N},
                        [jfje2]: {value: je},
                        [hjkm2]: {value: hjkmV, specialobj: [{id: hjkmV, name: hjkmName}]},
                        [fycdbm2]: {value: bm, specialobj: [{id: bm, name: mc}]},
                        [ry2]: {value: gys, specialobj: [{id: gys, name: gysName},]},
                        [ywlx2]: {value: hjkmName},
                    });
                    sumDex++;
                }
            }
        }

    }


    var bchxje = WfForm.convertFieldNameToId("bchxje", "main", true);//本次核销金额
    var bchxjeV = WfForm.getFieldValue(bchxje);

    var jzm3 = WfForm.convertFieldNameToId("jzm", "detail_3", true);//记账码
    var tbzz3 = WfForm.convertFieldNameToId("tbzz", "detail_3", true);//特别总账
    var zy3 = WfForm.convertFieldNameToId("zy", "detail_3", true);//摘要
    var hjkm3 = WfForm.convertFieldNameToId("hjkm", "detail_3", true);//会计科目
    var hsxm3 = WfForm.convertFieldNameToId("hsxm", "detail_3", true);//核算项目
    var dfje3 = WfForm.convertFieldNameToId("dfje", "detail_3", true);//贷方金额
    var xjl3 = WfForm.convertFieldNameToId("xjl", "detail_3", true);//现金流
    var lrzx3 = WfForm.convertFieldNameToId("lrzx", "detail_3", true);//利润中心
    var gys3 = WfForm.convertFieldNameToId("gys", "detail_3", true);//供应商


    var bcsfje = WfForm.convertFieldNameToId("bcsfje", "main", true);//本次实付金额
    var bcsfjeV = WfForm.getFieldValue(bcsfje);


    if (parseFloat(bchxjeV) > 0) {
        //冲借款
        var hkcxje = WfForm.convertFieldNameToId("hkcxje", "detail_4", true);//还款/核销金额
        var jksqh = WfForm.convertFieldNameToId("jksqh", "detail_4", true);//借款单号
        var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
        if (parseFloat(bchxjeV) >= parseFloat(bcsfjeV)) {
            //本次核销金额大于等于本次实付金额
            for (var i in index4) {
                if (i == 'remove') {
                    continue;
                }
                var hkcxjeV = WfForm.getFieldValue(hkcxje + "_" + index4[i])//还款/核销金额
                if (hkcxjeV == '' || hkcxjeV == "0" || parseFloat(hkcxjeV) == 0) {
                    continue;
                }
                var jksqhV = WfForm.getFieldValue(jksqh + "_" + index4[i])//借款单号
                var zyV = liucbhV + "_" + shenqrName + "_" + bxsyV + "_报销冲借款_" + jksqhV;
                WfForm.addDetailRow("detail_3", {
                    [zy3]: {value: zy},//摘要
                    [jzm3]: {value: "31"},//记账码
                    [hjkm3]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"},]},//会计科目
                    [gys3]: {value: gys, specialobj: [{id: gys, name: gysName},]},//供应商
                    [dfje3]: {value: hkcxjeV},//贷方金额
                    [lrzx3]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
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
                var zyV = liucbhV + "_" + shenqrName + "_" + bxsyV + "_报销冲借款_" + jksqhV;
                hkjeSum += parseFloat(hkcxjeV);
                WfForm.addDetailRow("detail_3", {
                    [zy3]: {value: zy},//摘要
                    [jzm3]: {value: "31"},//记账码
                    [hjkm3]: {value: "1221010100", specialobj: [{id: "1221010100", name: "其他应收款-借款"},]},//会计科目
                    [gys3]: {value: gys, specialobj: [{id: gys, name: gysName},]},//供应商
                    [dfje3]: {value: hkcxjeV},//贷方金额
                    [lrzx3]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
                });

            }
        }
        WfForm.addDetailRow("detail_3", {
            [zy3]: {value: zy},//摘要
            [jzm3]: {value: "50"},//记账码
            [hjkm3]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
            [dfje3]: {value: bcsfjeV},//贷方金额
            [xjl3]: {value: "B4"},//现金流
            [lrzx3]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
        });

    } else {
        //无核销借款
        WfForm.addDetailRow("detail_3", {
            [zy3]: {value: zy},//摘要
            [jzm3]: {value: "50"},//记账码
            [hjkm3]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
            [dfje3]: {value: bcsfjeV},//贷方金额
            [xjl3]: {value: "B4"},//现金流
            [lrzx3]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
        });
    }


}
