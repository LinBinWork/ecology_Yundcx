/***
 * 引用流程：营销项目对外付款申请流程
 * 引用节点：申请节点
 * 主要功能：1.
 */
jQuery(document).ready(function () {
    var sfhtfk = WfForm.convertFieldNameToId("sfhtfk", "main", true);//是否合同付款
    var htwfje = WfForm.convertFieldNameToId("htwfje", "main", true);//合同未付金额
    var bcyfje = WfForm.convertFieldNameToId("bcyfje", "main", true);//本次应付金额
    var htlc = WfForm.convertFieldNameToId("htlc", "main", true);//合同流程
    var sfhtfkV = WfForm.getFieldValue(sfhtfk);//是否合同付款
    var zxszxbh = WfForm.convertFieldNameToId("zxszxbh", "detail_4", true);//子项/孙子项项目编号
    var ftje = WfForm.convertFieldNameToId("ftje", "detail_4");//分摊金额

    if (sfhtfkV == '0') {
        var htlcV = WfForm.getFieldValue(htlc);
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/foreign/Foreign.jsp",
            data: 'cmd=getXMFTMX&htlc=' + htlcV,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var dex = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                    for (var i in dex) {
                        if (i == 'remove') {
                            continue;
                        }
                        WfForm.changeFieldAttr(zxszxbh + "_" + dex[i], 1);
                        WfForm.changeFieldAttr(ftje + "_" + dex[i], 1);
                    }
                    if (data.length > 0) {
                        $("div[name='xmxxft']")[1].style.display = 'none';
                    } else {
                        $("div[name='xmxxft']")[1].style.display = 'block';
                    }
                }
            }
        });
    }


    var htwfje = WfForm.convertFieldNameToId("htwfje", "main");//合同未付金额
    var bcyfje = WfForm.convertFieldNameToId("bcyfje", "main");//本次应付金额
    WfForm.bindFieldChangeEvent(htwfje + "," + bcyfje, function (obj, id, value) {
        updateSFWK();
    });

    var fkzsfbhwk = WfForm.convertFieldNameToId("fkzsfbhwk", "main", true);//付款中是否包含尾款
    var sfxhtja = WfForm.convertFieldNameToId("sfxhtja", "main", true);//是否需合同结案
    var gyspj = WfForm.convertFieldNameToId("gyspj", "main", true);//供应商评价
    var htjaxgfj = WfForm.convertFieldNameToId("htjaxgfj", "main", true);//合同结案相关附件
    WfForm.bindFieldChangeEvent(fkzsfbhwk, function (obj, id, value) {
        var sfxhtjaV = WfForm.getFieldValue(sfxhtja);
        if (value == '0' && sfxhtjaV == '0') {
            WfForm.changeFieldAttr(gyspj, 3);
            WfForm.changeFieldAttr(htjaxgfj, 3);
        } else {
            WfForm.changeFieldAttr(gyspj, 2);
            WfForm.changeFieldAttr(htjaxgfj, 2);
        }
    });
    WfForm.bindFieldChangeEvent(sfxhtja, function (obj, id, value) {
        var fkzsfbhwkV = WfForm.getFieldValue(fkzsfbhwk);
        if (value == '0' && fkzsfbhwkV == '0') {
            WfForm.changeFieldAttr(gyspj, 3);
            WfForm.changeFieldAttr(htjaxgfj, 3);
        } else {
            WfForm.changeFieldAttr(gyspj, 2);
            WfForm.changeFieldAttr(htjaxgfj, 2);
        }
    });
    //提交校验
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //校验。
        var sfhtfkV = WfForm.getFieldValue(sfhtfk);//是否合同付款
        if (sfhtfkV == '0') {
            //有合同付款
            var htwfjeV = WfForm.getFieldValue(htwfje);//合同未付金额
            var bcyfjeV = WfForm.getFieldValue(bcyfje);//本次应付金额
            var fkzsfbhwk = WfForm.convertFieldNameToId("fkzsfbhwk", "main", true);//付款中是否包含尾款
            var fkzsfbhwkV = WfForm.getFieldValue(fkzsfbhwk);
            if (fkzsfbhwkV == "0") {
                if (parseFloat(bcyfjeV) < parseFloat(htwfjeV)) {
                    WfForm.showConfirm("本次付款为最后一笔付款，流程结束后，将关闭合同，无法再针对合同进行付款，是否继续？", function () {
                        if (checkBudData()) {
                            callback();
                        }
                    });
                } else {
                    if (checkBudData()) {
                        callback();
                    }
                }
            } else {
                if (checkBudData()) {
                    callback();
                }
            }
        } else {
            if (checkBudData()) {
                callback();
            }
        }
    });

    WfForm.bindDetailFieldChangeEvent(ftje, function (id, idx, value) {
        flushPrecent();
        getApportionData();
    });

    var fkdx = WfForm.convertFieldNameToId("fkdx", "main", true);//付款对象
    WfForm.bindFieldChangeEvent(fkdx, function (obj, id, value) {
        getPaymentData();    //根据供应商带出预付款记录
    });

    WfForm.bindFieldChangeEvent(sfhtfk, function (obj, id, value) {
        $("div[name='xmxxft']")[1].style.display = 'block';
        isSF = true;
        isFt = false;
        WfForm.delDetailRow("detail_1", "all");//清空分摊明细
        WfForm.delDetailRow("detail_4", "all");//清空分摊明细
        WfForm.changeFieldValue(htlc, {value: ""});
        WfForm.addDetailRow("detail_4");
        updateSFWK();
        var sfhtfkV = WfForm.getFieldValue(sfhtfk);//是否合同付款
        var ftje = WfForm.convertFieldNameToId("ftje", "detail_4");//分摊金额
        var rowids4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
        if (sfhtfkV == '0') {
            for (var i in rowids4) {
                if (i == 'remove') {
                    continue;
                }
                isSF = true;
                isFt = false;
                WfForm.changeFieldAttr(ftje + "_" + rowids4[i], 1);
            }
        } else {
            for (var i in rowids4) {
                if (i == 'remove') {
                    continue;
                }
                isSF = true;
                isFt = false;
                WfForm.changeFieldAttr(ftje + "_" + rowids4[i], 3);
            }
        }
        isFt = true;
    });
    var ftje = WfForm.convertFieldNameToId("ftje", "detail_4");//分摊金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_4");//分摊比例
    WfForm.bindFieldChangeEvent(bcyfje, function (obj, id, value) {
        var sfhtfkV = WfForm.getFieldValue(sfhtfk);//是否合同付款
        var rowids4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
        if (sfhtfkV == '0') {
            //有合同付款
            var bcyfjeV = WfForm.getFieldValue(bcyfje)//本次应付金额
            var rowids = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
            for (var i in rowids) {
                if (i == 'remove') {
                    continue;
                }
                isSF = true;
                isFt = false;
                var ftblV = WfForm.getFieldValue(ftbl + "_" + rowids[i])//分摊比率
                WfForm.changeFieldValue(ftje + "_" + rowids[i], {value: bcyfjeV * ftblV / 100});
            }
            for (var i in rowids4) {
                if (i == 'remove') {
                    continue;
                }
                isSF = true;
                isFt = false;
                WfForm.changeFieldAttr(ftje + "_" + rowids4[i], 1);
            }
        } else {
            isSF = false;
            isFt = true;
            for (var i in rowids4) {
                if (i == 'remove') {
                    continue;
                }
                isSF = true;
                isFt = false;
                WfForm.changeFieldAttr(ftje + "_" + rowids4[i], 3);
            }
            isFt = true;
        }
    });


    var xmjl = WfForm.convertFieldNameToId("xmjl", "detail_4", true);//项目经理
    var sjxmjl = WfForm.convertFieldNameToId("sjxmjl", "detail_4", true);//上级项目经理
    var kyje = WfForm.convertFieldNameToId("kyje", "detail_4", true);//可用金额
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

    WfForm.bindDetailFieldChangeEvent(xmjl, function (id, rowIndex, value) {
        initManager();
    });
    WfForm.bindDetailFieldChangeEvent(sjxmjl, function (id, rowIndex, value) {
        initPIDManager();
    });

    var requestID = WfForm.getBaseInfo().requestid;

    WfForm.bindFieldChangeEvent(htlc, function (obj, id, value) {
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/foreign/Foreign.jsp",
            data: 'cmd=getContract&id=' + value + "&contractRequestID=" + requestID,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var htzje = WfForm.convertFieldNameToId("htzje", "main", true);//合同总金额
                    var htyfje = WfForm.convertFieldNameToId("htyfje", "main", true);//合同已付金额
                    var htwfje = WfForm.convertFieldNameToId("htwfje", "main", true);//合同未付金额
                    WfForm.changeFieldValue(htzje, {value: returnJson.htzje});
                    WfForm.changeFieldValue(htyfje, {value: returnJson.htyfje});
                    WfForm.changeFieldValue(htwfje, {value: returnJson.htwfje});
                }
            }
        });

        var zxszxbh = WfForm.convertFieldNameToId("zxszxbh", "detail_4");//子项/孙子项编号
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/foreign/Foreign.jsp",
            data: 'cmd=getXMFTMX&htlc=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null && returnJson != undefined) {
                    var data = returnJson.data;
                    WfForm.delDetailRow("detail_4", "all");//清空分摊明细
                    WfForm.delDetailRow("detail_5", "all");//清空分摊明细
                    for (var i in data) {
                        if (i == 'remove') {
                            continue;
                        }
                        isSF = true;
                        isFt = false;
                        WfForm.addDetailRow("detail_4");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldAttr(ftje + "_" + dex, 1);
                        WfForm.changeFieldValue(ftbl + "_" + dex, {value: data[i].ftbl});
                        WfForm.changeSingleField(zxszxbh + "_" + dex, {
                            value: data[i].xmbh,
                            specialobj: [{id: data[i].xmbh, name: data[i].projNo}]
                        }, {viewAttr: "1"});
                    }
                    if (data.length > 0) {
                        $("div[name='xmxxft']")[1].style.display = 'none';
                    } else {
                        $("div[name='xmxxft']")[1].style.display = 'block';
                    }
                }
            }
        });


    });

});

//修改是否尾款
function updateSFWK() {
    var sfhtfk = WfForm.convertFieldNameToId("sfhtfk", "main", true);//是否合同付款
    var sfhtfkV = WfForm.getFieldValue(sfhtfk);
    var bcyfje = WfForm.convertFieldNameToId("bcyfje", "main", true);//本次应付金额
    var bcyfjeV = WfForm.getFieldValue(bcyfje);

    var htwfje = WfForm.convertFieldNameToId("htwfje", "main", true);//合同未付金额
    var htwfjeV = WfForm.getFieldValue(htwfje);

    var fkzsfbhwk = WfForm.convertFieldNameToId("fkzsfbhwk", "main", true);//是否最后一笔付款
    if (sfhtfkV == '0') {
        if (parseFloat(bcyfjeV) >= parseFloat(htwfjeV)) {
            WfForm.changeFieldValue(fkzsfbhwk, {value: "0"});
            WfForm.changeFieldAttr(fkzsfbhwk, 1); //字段修改为只读
        } else {
            WfForm.changeFieldValue(fkzsfbhwk, {value: "1"});
            WfForm.changeFieldAttr(fkzsfbhwk, 3); //字段修改为只读
        }
    } else {
        WfForm.changeFieldAttr(fkzsfbhwk, 3); //字段修改为只读
    }
}

function _customDelFun4() {
    flushPrecent();
}

function getPaymentData() {
    var fkdx = WfForm.convertFieldNameToId("fkdx", "main", true);//付款对象
    var fkdxV = WfForm.getFieldValue(fkdx);
    var requestId = WfForm.getBaseInfo().requestid;
    var rowids4 = WfForm.getDetailRowCount("detail_2")
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/payment/Payment.jsp",
        data: 'cmd=getPayData&gysid=' + fkdxV + "&&requestId=" + requestId,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson != null && returnJson != undefined) {
                WfForm.delDetailRow("detail_2", "all");
                var yfdh = WfForm.convertFieldNameToId("yfdh", "detail_2", true);//预付单号
                var yfrq = WfForm.convertFieldNameToId("yfrq", "detail_2", true);//预付日期
                var yfje = WfForm.convertFieldNameToId("yfje", "detail_2", true);//预付金额
                var yfkje = WfForm.convertFieldNameToId("yfkje", "detail_2", true);//已付款金额
                var ztje = WfForm.convertFieldNameToId("ztje", "detail_2", true);//在途金额
                var syje = WfForm.convertFieldNameToId("syje", "detail_2", true);//剩余金额
                var yfid = WfForm.convertFieldNameToId("yfid", "detail_2", true);//预付id
                var bz = WfForm.convertFieldNameToId("bz", "detail_2", true);//币种
                var xmid = WfForm.convertFieldNameToId("xmid", "detail_2", true);//项目ID
                var mxid = WfForm.convertFieldNameToId("mxid", "detail_2", true);//明细ID
                for (var i in returnJson) {
                    var data = returnJson[i]
                    WfForm.addDetailRow("detail_2");
                    var dex = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
                    dex = dex[dex.length - 1]
                    WfForm.changeFieldValue(yfdh + "_" + dex, {value: data.requestcode});
                    WfForm.changeFieldValue(yfrq + "_" + dex, {value: data.paydate});
                    WfForm.changeFieldValue(yfje + "_" + dex, {value: data.money});
                    WfForm.changeFieldValue(yfkje + "_" + dex, {value: data.moneyed});
                    WfForm.changeFieldValue(ztje + "_" + dex, {value: data.moneying});
                    WfForm.changeFieldValue(syje + "_" + dex, {value: data.moneylast});
                    WfForm.changeFieldValue(yfid + "_" + dex, {value: data.requestid});
                    WfForm.changeFieldValue(xmid + "_" + dex, {value: data.xmid});
                    WfForm.changeFieldValue(bz + "_" + dex, {value: data.bz});
                    WfForm.changeFieldValue(mxid + "_" + dex, {value: data.detailid});
                }
            }
        }
    });

}

function initManager() {
    var values = [];
    var data = [];
    var rowids = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var xmjl = WfForm.convertFieldNameToId("xmjl", "detail_4", true);//项目经理
    for (var i in rowids) {
        if (i == 'remove') {
            continue;
        }
        var xmjlV = WfForm.getFieldValue(xmjl + "_" + rowids[i]);//项目经理
        var xmjlName = WfForm.getBrowserShowName(xmjl + "_" + rowids[i]);//项目经理名称
        values.push(xmjlV)
        data.push({
            id: xmjlV,
            name: xmjlName,
        })
    }
    var zxxmjl = WfForm.convertFieldNameToId("zxxmjl", "main", true);//子项项目经理
    if (values.length > 0) {
        WfForm.changeFieldValue(zxxmjl,
            {value: values.toString(), specialobj: data});
    } else {
        WfForm.changeFieldValue(zxxmjl,
            {value: "", specialobj: ""});
    }
}

function initPIDManager() {
    var values = [];
    var data = [];
    var rowids = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var sjxmjl = WfForm.convertFieldNameToId("sjxmjl", "detail_4", true);//上级项目经理
    for (var i in rowids) {
        if (i == 'remove') {
            continue;
        }
        var xmjlV = WfForm.getFieldValue(sjxmjl + "_" + rowids[i]);//上级项目经理
        var xmjlName = WfForm.getBrowserShowName(sjxmjl + "_" + rowids[i]);//上级项目经理名称
        values.push(xmjlV)
        data.push({
            id: xmjlV,
            name: xmjlName,
        })
    }
    var fxxmjl = WfForm.convertFieldNameToId("fxxmjl", "main", true);//父项项目经理
    if (values.length > 0) {
        WfForm.changeFieldValue(fxxmjl,
            {value: values.toString(), specialobj: data});
    } else {
        WfForm.changeFieldValue(fxxmjl,
            {value: "", specialobj: ""});
    }
}

var isSF = true;
var isFt = true;

//刷新分摊比例
function flushPrecent() {
    if (isSF && !isFt) {
        isFt = true
        return;
    }
    var ftzje = WfForm.convertFieldNameToId("ftzje");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);

    var ftje = WfForm.convertFieldNameToId("ftje", "detail_4");//分摊金额
    var ftbl = WfForm.convertFieldNameToId("ftbl", "detail_4");//分摊比例

    var rowids = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    jQuery.each(rowids, function (i, idx) {
        var ftjeV = WfForm.getFieldValue(ftje + "_" + idx);
        var ftblV = (ftjeV / ftzjeV * 100).toFixed(3);
        if (isNaN(ftblV)) {
            WfForm.changeFieldValue(ftbl + "_" + idx, {value: 100.00});
        } else {
            WfForm.changeFieldValue(ftbl + "_" + idx, {value: ftblV});
        }
    });
    isFt = true
}

function checkBudData() {
    var f = true;
    var ftzje = WfForm.convertFieldNameToId("ftzje");//分摊总金额
    var ftzjeV = WfForm.getFieldValue(ftzje);
    var bcyfje = WfForm.convertFieldNameToId("bcyfje");//本次应付金额
    var bcyfjeV = WfForm.getFieldValue(bcyfje);
    var sfhtfk = WfForm.convertFieldNameToId("sfhtfk", "main", true);//是否合同付款
    var sfhtfkV = WfForm.getFieldValue(sfhtfk);//是否合同付款


    var fkfs = WfForm.convertFieldNameToId("fkfs", "main", true);//付款方式
    var fkfsV = WfForm.getFieldValue(fkfs);//付款方式
    if (sfhtfkV == '0') {
        //有合同付款：本次实付金额不得大于合同未付金额。
        var bcsfje = WfForm.convertFieldNameToId("bcsfje", "main", true);//本次实付金额
        var htwfje = WfForm.convertFieldNameToId("htwfje", "main", true);//合同未付金额
        var bcsfjeV = WfForm.getFieldValue(bcsfje);
        var htwfjeV = WfForm.getFieldValue(htwfje);
        if (parseFloat(bcsfjeV) > parseFloat(htwfjeV)) {
            f = false;
            antd.Modal.error({
                title: '提示！',
                content: "本次实付金额不得大于合同未付金额。",
                onOk: function () {
                }
            });
            return f;
        }

        if (fkfsV == "4") {
            var htlc = WfForm.convertFieldNameToId("htlc", "main", true);//合同流程
            var htlcV = WfForm.getFieldValue(htlc);//合同流程
            jQuery.ajax({
                type: "POST",
                url: "/westvalley/workflow/foreign/Foreign.jsp",
                data: 'cmd=checkoutContract&htlc=' + htlcV,
                dataType: "text",
                async: false,
                success: function (data) {
                    var returnJson = eval("(" + data + ")");
                    if (returnJson != null && returnJson != undefined) {
                        if (returnJson.htlx != '0') {
                            f = false;
                            antd.Modal.error({
                                title: '提示！',
                                content: "当前付款方式为广告/产品置换，该合同不是置换类合同，无法提交。",
                                onOk: function () {
                                }
                            });
                            return f;
                        }
                    }
                }
            });
        }
    } else if (sfhtfkV == '1') {
        //无合同付款，付款金额分摊至各项目的后，其金额不得超过当前项目控制层级所在项目的可用项目预算金额。

    }
    if (ftzjeV != bcyfjeV) {
        f = false
        antd.Modal.error({
            title: '提示！',
            content: "分摊总金额不等于本次应付金额，无法提交。",
            onOk: function () {
            }
        });
        return f;
    }
    var yfdh = WfForm.convertFieldNameToId("yfdh", "detail_2", true);//预付单号
    var hxje = WfForm.convertFieldNameToId("hxje", "detail_2", true);//核销金额
    var syje = WfForm.convertFieldNameToId("syje", "detail_2", true);//剩余金额
    var bz2 = WfForm.convertFieldNameToId("bz", "detail_2", true);//币种
    var index2 = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    var index1 = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var bz1 = WfForm.convertFieldNameToId("bz", "detail_1", true);//币种

    for (var i in index2) {
        if (i == 'remove') {
            continue;
        }
        var hxjeV = WfForm.getFieldValue(hxje + "_" + index2[i])
        var syjeV = WfForm.getFieldValue(syje + "_" + index2[i])
        if (parseFloat(hxjeV) > parseFloat(syjeV)) {
            var yfdhV = WfForm.getFieldValue(yfdh + "_" + index2[i])
            f = false;
            antd.Modal.error({
                title: '提示！',
                content: "预付单号:" + yfdhV + ",核销金额：" + hxjeV + "不能大于未核销金额:" + syjeV + "。",
                onOk: function () {
                }
            });
            return f
        }
        if (parseFloat(hxjeV) == 0) continue;
        var bz2V = WfForm.getFieldValue(bz2 + "_" + index2[i])
        if (bz2V == '') continue;
        for (var j in index1) {
            if (j == 'remove') {
                continue;
            }
            var bz1V = WfForm.getFieldValue(bz1 + "_" + index1[j])//币种
            if (bz1V == "") continue;
            if (bz2V != bz1V) {
                f = false;
                antd.Modal.error({
                    title: '提示！',
                    content: "币种不一致，无法进行核销。",
                    onOk: function () {
                    }
                });
                return f
            }
        }
    }
    for (var i in index1) {
        if (i == 'remove') {
            continue;
        }
        var bz1V = WfForm.getFieldValue(bz1 + "_" + index1[i])//币种
        if (bz1V == "") continue;
        for (var j in index1) {
            if (j == 'remove') {
                continue;
            }
            var bz2V = WfForm.getFieldValue(bz1 + "_" + index1[j])
            if (bz2V != bz1V) {
                f = false;
                antd.Modal.error({
                    title: '提示！',
                    content: "付款明细中不允许存在不同的币种！",
                    onOk: function () {
                    }
                });
                return f
            }
        }
    }

    var msg = '';
    var index4 = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
    var xmbhs = '';
    var xmbh = WfForm.convertFieldNameToId("zxszxbh", "detail_4", true);
    for (var i in index4) {
        if (i == 'remove') {
            continue;
        }
        var value = WfForm.getFieldValue(xmbh + "_" + index4[i])
        if (xmbhs == '') {
            xmbhs = value
        } else {
            xmbhs = xmbhs + "," + value
        }
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
    if (msg != null && msg != '') {
        var bhs = msg.split(",");
        var res = "";
        var xmmc = WfForm.convertFieldNameToId("zxszxxmmc", "detail_4", true);
        var xms = [];
        for (var bh in bhs) {
            if (bh == 'remove') {
                continue;
            }
            for (var i in index4) {
                var value = WfForm.getFieldValue(xmbh + "_" + index4[i])
                if (i == 'remove' || value == '') {
                    continue;
                }
                if (value == bhs[bh]) {
                    var xmName = WfForm.getFieldValue(xmmc + "_" + index4[i])//项目名称；
                    if (xms.indexOf(xmName) == -1) {
                        xms.push(xmName)
                    }
                }
            }
        }
        res = "[" + xms + "]项目未完成执行方案审批，请先完成执行方案审批！";
        antd.Modal.error({
            title: '提示！',
            content: res,
            onOk: function () {
            }
        });
        f = false;
        return f;
    }

    var fkzsfbhwk = WfForm.convertFieldNameToId("fkzsfbhwk", "main", true);//是否最后一笔付款
    WfForm.changeFieldAttr(fkzsfbhwk, 3); //字段修改为只读
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
    var index1 = WfForm.getDetailAllRowIndexStr("detail_4").split(",")
    var zxkm = WfForm.convertFieldNameToId("zxszxbh", "detail_4", true);//子项/孙子项
    var bxjermb = WfForm.convertFieldNameToId("ftje", "detail_4", true);//分摊金额

    var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_5", true);//分摊部门
    var lxjyje = WfForm.convertFieldNameToId("lxskyje", "detail_5", true);//立项结余金额
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
                        WfForm.addDetailRow("detail_5");
                        var dex = WfForm.getDetailAllRowIndexStr("detail_5").split(",");
                        dex = dex[dex.length - 1]
                        WfForm.changeFieldValue(ftbm + "_" + dex, {
                            value: data.ftbm,
                            specialobj: [{id: data.ftbm, name: data.ltext},]
                        });
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











