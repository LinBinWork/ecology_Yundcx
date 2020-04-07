/***
 * 引用流程：营销项目对外付款申请流程
 * 引用节点：总账主管SAP转账
 * 主要功能：1.生成借贷分录
 */
jQuery(document).ready(function () {
    var rowids3 = WfForm.getDetailRowCount("detail_3")
    var rowids6 = WfForm.getDetailRowCount("detail_6")
    if (rowids3 == 0 || rowids6 == 0) {
        loadVoucherDetail();
    }

    //添加按钮按钮
    jQuery("#loadVoucherbutton").append(""
        + "<input class='ant-btn ant-btn-ghost' onclick='loadVoucherDetail()' type='button'"
        + " id='loadvoucher_btn' value='生成借贷分录' />"
        + "");

});


function loadVoucherDetail() {
    var saphjkmbmList = [
        '6600050200', '6600050300', '6600060100', '6600080101', '6600080102', '6600080103', '6600080104', '6600080105', '6600080106'
        , '6600080107', '6600080199', '6600080201', '6600080202', '6600080203', '6600080204', '6600080299', '6600090100'];
    var shenqr = WfForm.convertFieldNameToId("shenqr", "main", true);//申请人名字
    var shenqrName = WfForm.getBrowserShowName(shenqr);//申请人名字
    var liucbh = WfForm.convertFieldNameToId("liucbh", "main", true);//流程编号
    var liucbhV = WfForm.getFieldValue(liucbh);//流程编号
    var fkdx = WfForm.convertFieldNameToId("fkdx", "main", true);//供应商
    var fkdxV = WfForm.getFieldValue(fkdx);//供应商
    var fkdxN = WfForm.getBrowserShowName(fkdx);//供应商

    var kh = WfForm.convertFieldNameToId("kh", "main", true);//客户
    var khV = WfForm.getFieldValue(kh);//客户
    var khN = WfForm.getBrowserShowName(kh);//客户

    var fksy = WfForm.convertFieldNameToId("fksy", "main", true);//付款事由
    var fksyV = WfForm.getFieldValue(fksy).substr(0, 4);//付款事由
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

    var shenqbm = WfForm.convertFieldNameToId("shenqbm", "main", true);//申请人部门
    var shenqbmV = WfForm.getFieldValue(shenqbm);
    var lrzx = "";
    var lrzxName = "";
    //带出费用承担部门
    jQuery.ajax({
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
    });
    WfForm.delDetailRow("detail_3", "all");//清空明细
    WfForm.delDetailRow("detail_6", "all");//清空明细

    var fkfs = WfForm.convertFieldNameToId("fkfs", "main", true);//付款方式
    var fkfsV = WfForm.getFieldValue(fkfs);//付款方式
    var bt = jQuery("#requestname").val();
    var jzm3 = WfForm.convertFieldNameToId("jzm", "detail_3", true);//记账码
    var tbzz3 = WfForm.convertFieldNameToId("tbzz", "detail_3", true);//特别总账
    var zy3 = WfForm.convertFieldNameToId("zy", "detail_3", true);//摘要
    var hjkm3 = WfForm.convertFieldNameToId("hjkm", "detail_3", true);//会计科目
    var hsxm3 = WfForm.convertFieldNameToId("hsxm", "detail_3", true);//核算项目
    var fycdbm3 = WfForm.convertFieldNameToId("fycdbm", "detail_3", true);//费用承担部门
    var lrzx3 = WfForm.convertFieldNameToId("lrzx", "detail_3", true);//利润中心
    var jfjecny3 = WfForm.convertFieldNameToId("jfjecny", "detail_3", true);//借方金额
    var ry3 = WfForm.convertFieldNameToId("ry", "detail_3", true);//人员
    var gys3 = WfForm.convertFieldNameToId("gys", "detail_3", true);//供应商
    var xm3 = WfForm.convertFieldNameToId("xm", "detail_3", true);//项目
    var ywlx3 = WfForm.convertFieldNameToId("ywlx", "detail_3", true);//业务类型



    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var fkrmbje = WfForm.convertFieldNameToId("fkrmbje", "detail_1", true);//付款人民币金额
    var sjfkje = WfForm.convertFieldNameToId("sjfkje", "detail_1", true);//实际付款金额
    var ywyslx = WfForm.convertFieldNameToId("ywyslx", "detail_1", true);//业务（预算）类型
    var fplx = WfForm.convertFieldNameToId("fplx", "detail_1", true);//发票类型
    var se = WfForm.convertFieldNameToId("se", "detail_1", true);//税额
    var sfyzzszyfp = WfForm.convertFieldNameToId("sfyzzszyfp", "detail_1", true);//是否有增值税专用发票
    var sjje = WfForm.convertFieldNameToId("sjje", "detail_1", true);//税金金额
    var fplx = WfForm.convertFieldNameToId("fplx", "detail_1", true);//发票类型

    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var zxszxbh4 = WfForm.convertFieldNameToId("zxszxbh", "detail_4", true);//子项/孙子项编号
    var ftje4 = WfForm.convertFieldNameToId("ftje", "detail_4", true);//分摊金额
    var ftbl4 = WfForm.convertFieldNameToId("ftbl", "detail_4", true);//分摊比率


    var index5 = WfForm.getDetailAllRowIndexStr("detail_5").split(",");
    var ftbm5 = WfForm.convertFieldNameToId("ftbm", "detail_5", true);//分摊部门
    var lxjyje5 = WfForm.convertFieldNameToId("lxskyje", "detail_5", true);//立项结余金额
    var bclxje5 = WfForm.convertFieldNameToId("bclxje", "detail_5", true);//本次立项金额
    var ftbl5 = WfForm.convertFieldNameToId("ftbl", "detail_5", true);//分摊比率
    var ftbmfzr5 = WfForm.convertFieldNameToId("ftbmfzr", "detail_5", true);//分摊部门负责人
    var xm5 = WfForm.convertFieldNameToId("xm", "detail_5", true);//项目

    var gsbm = WfForm.convertFieldNameToId("gsbm", "main", true);//公司编码
    var gsbmV = WfForm.getFieldValue(gsbm);//公司编码

    var jzm6 = WfForm.convertFieldNameToId("jzm", "detail_6", true);//记账码
    var tbzz6 = WfForm.convertFieldNameToId("tbzz", "detail_6", true);//特别总账
    var zy6 = WfForm.convertFieldNameToId("zy", "detail_6", true);//摘要
    var hjkm6 = WfForm.convertFieldNameToId("hjkm", "detail_6", true);//会计科目
    var hsxm6 = WfForm.convertFieldNameToId("hsxm", "detail_6", true);//核算项目
    var lrzx6 = WfForm.convertFieldNameToId("lrzx", "detail_6", true);//利润中心
    var xjl6 = WfForm.convertFieldNameToId("xjl", "detail_6", true);//现金流
    var dfjecny6 = WfForm.convertFieldNameToId("dfjecny", "detail_6", true);//贷方金额
    var gys6 = WfForm.convertFieldNameToId("gys", "detail_6", true);//供应商
    var xm6 = WfForm.convertFieldNameToId("xm", "detail_6", true);//项目
    var ry6 = WfForm.convertFieldNameToId("ry", "detail_6", true);//人员
    var ftzje = WfForm.convertFieldNameToId("ftzje", "main");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);

    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var ywyslxV = WfForm.getFieldValue(ywyslx + "_" + index1[i])//业务类型
        var ywyslxN = WfForm.getBrowserShowName(ywyslx + "_" + index1[i])//业务类型
        var fkrmbjeV = WfForm.getFieldValue(fkrmbje + "_" + index1[i])
        var sjfkjeV = WfForm.getFieldValue(fkrmbje + "_" + index1[i])
        var saphjkmbm = '';
        var saphjkmmc = '';
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/cmd.jsp",
            data: 'cmd=getYwlx&ywlx=' + ywyslxV,
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
        var sfzzszyfpV = WfForm.getFieldValue(sfyzzszyfp + "_" + index1[i])//是否增值税专用发票
        var seV = WfForm.getFieldValue(se + "_" + index1[i])//税额
        if (sfzzszyfpV == "0" && fkfsV != '3') {
            sjfkjeV = parseFloat(sjfkjeV) - parseFloat(seV)
        }
        for (var j in index4) {
            if (j == 'remove') {
                continue;
            }
            var money4 = 0;
            var sumFtbl = 0;
            var zxszxbh4V = WfForm.getFieldValue(zxszxbh4 + "_" + index4[j])//子项/孙子项编号
            var zxszxbh4N = WfForm.getBrowserShowName(zxszxbh4 + "_" + index4[j])//子项/孙子项编号
            var ftbl4V = WfForm.getFieldValue(ftbl4 + "_" + index4[j])//分摊比率
            var ftje4V = WfForm.getFieldValue(ftje4 + "_" + index4[j])//分摊金额
            ftbl4V = (ftje4V / ftzjeV * 100).toFixed(2)
            var sumMoney4 = (parseFloat(sjfkjeV) * (parseFloat(ftbl4V) / 100)).toFixed(2)
            var sumMoney5 = (parseFloat(seV) * (parseFloat(ftbl4V) / 100)).toFixed(2)
            for (var k in index5) {
                if (k == 'remove') {
                    continue;
                }
                var xm5V = WfForm.getFieldValue(xm5 + "_" + index5[k])//子项/孙子项编号
                if (xm5V != zxszxbh4V) {
                    continue;
                }
                var ftbl5V = WfForm.getFieldValue(ftbl5 + "_" + index5[k])//分摊比率
                var bclxje5V = WfForm.getFieldValue(bclxje5 + "_" + index5[k])//本次立项金额
                ftbl5V = (bclxje5V / ftje4V * 100).toFixed(2)

                sumFtbl += parseFloat(ftbl5V)
                var je1 = (sumMoney5 * (parseFloat(ftbl5V) / 100)).toFixed(2)
                var je2 = (sumMoney4 * (parseFloat(ftbl5V) / 100)).toFixed(2)
                if (sumFtbl == 100) {
                    je1 = sumMoney5 - parseFloat(money4)
                    je2 = sumMoney4 - parseFloat(money4)
                } else {
                    if (fkfsV == '3') {
                        money4 += parseFloat(je1);
                    } else {
                        money4 += parseFloat(je2);
                    }
                }
                var ftbmN = WfForm.getBrowserShowName(ftbm5 + "_" + index5[k])//分摊部门
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
                var zy = "付款：付 " + shenqrName + " " + fkdxN + " " + bt;//摘要
                if (fkfsV == '3') {
                    WfForm.addDetailRow("detail_6");
                    var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
                    dex = dex[dex.length - 1]
                    WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
                    WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "50"});//记账码
                    WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                        value: saphjkmbm,
                        specialobj: [{id: saphjkmbm, name: saphjkmmc}]
                    });//会计科目
                    WfForm.changeFieldValue(xm6 + "_" + dex, {value: zxszxbh4N});//项目
                    WfForm.changeFieldValue(ry6 + "_" + dex, {value: gys, specialobj: [{id: gys, name: gysName}]});//人员
                    WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: je1});
                    WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: bm, specialobj: [{id: bm, name: mc}]});//利润中心
                } else {
                    WfForm.addDetailRow("detail_3");
                    var dex = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
                    dex = dex[dex.length - 1]
                    WfForm.changeFieldValue(zy3 + "_" + dex, {value: zy});//摘要
                    WfForm.changeFieldValue(jzm3 + "_" + dex, {value: "40"});//记账码
                    WfForm.changeFieldValue(xm3 + "_" + dex, {value: zxszxbh4N});//项目
                    WfForm.changeFieldValue(hjkm3 + "_" + dex, {
                        value: saphjkmbm,
                        specialobj: [{id: saphjkmbm, name: saphjkmmc}]
                    });//会计科目
                    if (saphjkmbmList.indexOf(saphjkmbm) != -1) {
                        WfForm.changeFieldValue(ry3 + "_" + dex, {value: gys, specialobj: [{id: gys, name: gysName}]});//人员
                    }
                    WfForm.changeFieldValue(lrzx3 + "_" + dex, {value: bm, specialobj: [{id: bm, name: mc}]});//利润中心
                    WfForm.changeFieldValue(jfjecny3 + "_" + dex, {value: je2});//借方金额
                    WfForm.changeFieldValue(ywlx3 + "_" + dex, {value: ywyslxN});//业务类型
                }
            }
        }
        if (fkfsV == '3') {
            var fplxV = WfForm.getFieldValue(fplx + "_" + index1[i])//发票类型
            //根据发票类型带出会计科目
            var sxbm = "";
            var sjkm = "";
            jQuery.ajax({
                type: "POST",
                url: "/westvalley/workflow/cmd.jsp",
                data: 'cmd=getFplx&fplx=' + fplxV,
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
            var zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
            WfForm.addDetailRow("detail_3");
            var dex = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(zy3 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm3 + "_" + dex, {value: "40"});//记账码
            WfForm.changeFieldValue(hjkm3 + "_" + dex, {
                value: sxbm,
                specialobj: [{id: sxbm, name: sjkm}]
            });//会计科目
            WfForm.changeFieldValue(jfjecny3 + "_" + dex, {value: seV});//借方金额
            continue;
        }
        if (sfzzszyfpV == "1") continue;
        if (parseFloat(seV) > 0) {
            var fplxV = WfForm.getFieldValue(fplx + "_" + index1[i])//发票类型
            var fplxV2 = WfForm.getBrowserShowName(fplx + "_" + index1[i])//发票类型
            var zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
            //根据发票类型带出会计科目
            var sxbm = "";
            var sjkm = "";
            jQuery.ajax({
                type: "POST",
                url: "/westvalley/workflow/cmd.jsp",
                data: 'cmd=getFplx&fplx=' + fplxV,
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

            // var zyV = "报销项目_" + fplxV2
            WfForm.addDetailRow("detail_3");
            var dex = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(zy3 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm3 + "_" + dex, {value: "40"});//记账码
            WfForm.changeFieldValue(hjkm3 + "_" + dex, {value: sxbm, specialobj: [{id: sxbm, name: sjkm}]});//会计科目
            WfForm.changeFieldValue(jfjecny3 + "_" + dex, {value: seV});//税额
        }

    }


    var bchxje = WfForm.convertFieldNameToId("bchxje", "main", true);//本次核销金额
    var bchxjeV = WfForm.getFieldValue(bchxje);//本次核销金额
    var bcsfje = WfForm.convertFieldNameToId("bcsfje", "main", true);//实付金额
    var bcsfjeV = WfForm.getFieldValue(bcsfje);//实付金额

    var xm = "";
    if (fkfsV == '4') {
        xm = fkdxV + fkdxN;
    }
    if (parseFloat(bcsfjeV) != 0 && fkfsV != '3') {
        WfForm.addDetailRow("detail_3");
        var dex = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
        dex = dex[dex.length - 1]
        var zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
        WfForm.changeFieldValue(zy3 + "_" + dex, {value: zy});//摘要
        WfForm.changeFieldValue(jzm3 + "_" + dex, {value: "29"});//记账码
        WfForm.changeFieldValue(tbzz3 + "_" + dex, {value: "C"});//特别总账
        if (fkfsV == '4') {
            WfForm.changeFieldValue(hjkm3 + "_" + dex, {
                value: "1122010100",
                specialobj: [{id: "1122010100", name: "应收账款-往来"}]
            });//会计科目
        } else {
            WfForm.changeFieldValue(hjkm3 + "_" + dex, {
                value: "1221020100",
                specialobj: [{id: "1221020100", name: "其他应收款-非借款"}]
            });//会计科目
        }
        /*if (fkfsV == '4') {
            WfForm.changeFieldValue(gys3 + "_" + dex, {value: khV, specialobj: [{id: khV, name: khN}]});//供应商
        } else {
            WfForm.changeFieldValue(gys3 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN}]});//供应商
        }*/
        WfForm.changeFieldValue(gys3 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN}]});//供应商
        WfForm.changeFieldValue(jfjecny3 + "_" + dex, {value: bcsfjeV});//借方金额
    }

    // var zy2 = "冲预付款：" + "付_" + shenqrName + "_" + fkdxN + "_" + fksyV;//冲预付款：付_申请人_供应商-付款事由
    var zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
    if (parseFloat(bchxjeV) > 0) {
        if (parseFloat(bcsfjeV) == 0) {
            //冲预付款
            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy2},//摘要
                [jzm6]: {value: "39"},//记账码
                [tbzz6]: {value: "C"},//特别总帐
                [hjkm6]: {value: "1221020100", specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]},//会计科目
                [gys6]: {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]},//供应商
                [dfjecny6]: {value: parseFloat(bchxjeV) + parseFloat(bcsfjeV)},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/
            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "39"});//记账码
            WfForm.changeFieldValue(tbzz6 + "_" + dex, {value: "C"});//特别总帐
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1221020100",
                specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]
            });//会计科目
            WfForm.changeFieldValue(gys6 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]});//供应商
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: parseFloat(bchxjeV) + parseFloat(bcsfjeV)});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心

        } else if (parseFloat(bcsfjeV) > 0) {
            // 有预付款，但是报销金额>预付余额，差额付款
            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy2},//摘要
                [jzm6]: {value: "39"},//记账码
                [tbzz6]: {value: "C"},//特别总帐
                [hjkm6]: {value: "1221020100", specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]},//会计科目
                [gys6]: {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]},//供应商
                [dfjecny6]: {value: parseFloat(bchxjeV) + parseFloat(bcsfjeV)},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/
            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "39"});//记账码
            WfForm.changeFieldValue(tbzz6 + "_" + dex, {value: "C"});//特别总帐
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1221020100",
                specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]
            });//会计科目
            WfForm.changeFieldValue(gys6 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]});//供应商
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: parseFloat(bchxjeV) + parseFloat(bcsfjeV)});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心

            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy},//摘要
                [jzm6]: {value: "50"},//记账码
                [xjl6]: {value: "B4"},//现金流
                [hjkm6]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
                [dfjecny6]: {value: bcsfjeV},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/

            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "50"});//记账码
            WfForm.changeFieldValue(xjl6 + "_" + dex, {value: "B4"});//现金流
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1002010100",
                specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]
            });//会计科目
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: bcsfjeV});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心
        }
    } else {
        if (fkfsV == '4') {
            // 广告/产品置换
            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy},//摘要
                [jzm6]: {value: "11"},//记账码
                [tbzz6]: {value: ""},//特别总账
                [hjkm6]: {value: khV, specialobj: [{id: khV, name: khN},]},//会计科目
                [gys6]: {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]},//供应商
                [dfjecny6]: {value: bcsfjeV},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/
            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            var zy = "付款：付" + shenqrName + " " + khN + " " + bt;//摘要
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "11"});//记账码
            WfForm.changeFieldValue(tbzz6 + "_" + dex, {value: ""});//特别总账
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {value: khV, specialobj: [{id: khV, name: khN},]});//会计科目
            WfForm.changeFieldValue(gys6 + "_" + dex, {value: khV, specialobj: [{id: khV, name: khN}]});//供应商
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: bcsfjeV});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心

            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy},//摘要
                [jzm6]: {value: "39"},//记账码
                [tbzz6]: {value: "C"},//特别总账
                [hjkm6]: {value: "1221020100", specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]},//会计科目
                [gys6]: {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]},//供应商
                [dfjecny6]: {value: bcsfjeV},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
                [xm6]: {value: xm},//项目
            });*/

            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            var zy = "付款：付" + shenqrName + fkdxN + bt;//摘要
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "39"});//记账码
            WfForm.changeFieldValue(tbzz6 + "_" + dex, {value: "C"});//特别总账
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1122010100",
                specialobj: [{id: "1122010100", name: "应收账款-往来"},]
            });//会计科目
            WfForm.changeFieldValue(gys6 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN}]});//供应商
            // WfForm.changeFieldValue(gys6 + "_" + dex, {value: khV, specialobj: [{id: khV, name: khN}]});//供应商
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: bcsfjeV});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心
            WfForm.changeFieldValue(xm6 + "_" + dex, {value: xm});//项目

        } else if (fkfsV == '3') {
            //平台扣款

        } else {
            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy},//摘要
                [jzm6]: {value: "39"},//记账码
                [tbzz6]: {value: "C"},//特别总账
                [hjkm6]: {value: "1221020100", specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]},//会计科目
                [gys6]: {value: khV, specialobj: [{id: khV, name: khN},]},//供应商
                [dfjecny6]: {value: bcsfjeV},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/
            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "39"});//记账码
            WfForm.changeFieldValue(tbzz6 + "_" + dex, {value: "C"});//特别总账
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1221020100",
                specialobj: [{id: "1221020100", name: "其他应收款-非借款"},]
            });//会计科目
            var zy = '';
            if (khV == '') {
                WfForm.changeFieldValue(gys6 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]});//供应商
                zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
            } else {
                WfForm.changeFieldValue(gys6 + "_" + dex, {value: khV, specialobj: [{id: khV, name: khN},]});//供应商
                zy = "付款：付" + shenqrName + " " + khN + " " + bt;//摘要
            }
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: bcsfjeV});//金额
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心

            //无预付款直接付款
            /*WfForm.addDetailRow("detail_6", {
                [zy6]: {value: zy},//摘要
                [jzm6]: {value: "50"},//记账码
                [xjl6]: {value: "B4"},//现金流
                [hjkm6]: {value: "1002010100", specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]},//会计科目
                [dfjecny6]: {value: bcsfjeV},//金额
                [lrzx6]: {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]},//利润中心
            });*/

            WfForm.addDetailRow("detail_6");
            var dex = WfForm.getDetailAllRowIndexStr("detail_6").split(",");
            dex = dex[dex.length - 1]
            var zy = "付款：付" + shenqrName + " " + fkdxN + " " + bt;//摘要
            WfForm.changeFieldValue(zy6 + "_" + dex, {value: zy});//摘要
            WfForm.changeFieldValue(jzm6 + "_" + dex, {value: "50"});//记账码
            WfForm.changeFieldValue(xjl6 + "_" + dex, {value: "B4"});//现金流
            WfForm.changeFieldValue(hjkm6 + "_" + dex, {
                value: "1002010100",
                specialobj: [{id: "1002010100", name: "中国银行深圳侨城支行4933"},]
            });//会计科目
            WfForm.changeFieldValue(dfjecny6 + "_" + dex, {value: bcsfjeV});//金额
            WfForm.changeFieldValue(gys6 + "_" + dex, {value: fkdxV, specialobj: [{id: fkdxV, name: fkdxN},]});//供应商
            WfForm.changeFieldValue(lrzx6 + "_" + dex, {value: lrzx, specialobj: [{id: lrzx, name: lrzxName},]});//利润中心


        }


    }
}
