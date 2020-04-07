/***
 * 引用流程：年度项目预算申请
 * 引用节点：申请节点
 * 主要功能：检验明细表数据是否重复。
 */
jQuery(document).ready(function () {
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //检验明细表数据是否重复。
        if (checkBudData()) {
            callback();
        }
    });

    var zxmc = WfForm.convertFieldNameToId("zxmc", "detail_1", true);//祖项名称
    var bm = WfForm.convertFieldNameToId("bm", "detail_1", true);//费用归属部门名称
    var ywlb = WfForm.convertFieldNameToId("ywlb", "main", true);
    WfForm.bindFieldChangeEvent(ywlb, function (obj, id, value) {
        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        jQuery.each(rowids, function (i, idx) {
            WfForm.changeFieldValue(zxmc + "_" + idx, {
                value: "",
                specialobj: [
                    {id: "", name: ""},
                ]
            });
            WfForm.changeFieldValue(bm + "_" + idx, {
                value: "",
                specialobj: [
                    {id: "", name: ""},
                ]
            });
        });

    });


    WfForm.bindDetailFieldChangeEvent(zxmc, function (id, rowIndex, value) {
        WfForm.changeFieldValue(bm + "_" + rowIndex, {
            value: "",
            specialobj: [
                {id: "", name: ""},
            ]
        });
    });

});

function checkBudData() {
    var f = true;
    var zxbm = WfForm.convertFieldNameToId("zxbm", "detail_1", true);//祖项编码
    var bmbm = WfForm.convertFieldNameToId("bmbm", "detail_1", true);//费用归属部门编码
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    var msg = "";
    var zxbms = [];
    var bmbms = [];
    jQuery.each(rowids, function (i, idx) {
        var sqjeV = WfForm.getFieldValue(zxbm + "_" + idx);
        var ysedV = WfForm.getFieldValue(bmbm + "_" + idx);
        for (var i in rowids) {
            if (idx == rowids[i]) {
                continue;
            }
            var sqjeV2 = WfForm.getFieldValue(zxbm + "_" + rowids[i]);
            var ysedV2 = WfForm.getFieldValue(bmbm + "_" + rowids[i]);
            if (sqjeV == sqjeV2 && ysedV == ysedV2) {
                if (zxbms.indexOf(sqjeV) > -1 && bmbms.indexOf(ysedV) > -1) {

                } else {
                    zxbms.push(sqjeV)
                    bmbms.push(ysedV)
                    f = false;
                }
            }

        }
    });
    if (!f) {
        for (var i = 0; i < zxbms.length; i++) {
            msg += "祖项编码:" + zxbms[i] + ",费用归属部门编码:" + bmbms[i] + ";"
        }
        antd.Modal.error({
            title: '提示！',
            content: msg + "数据重复，无法提交。",
            onOk: function () {
            }
        });
    }
    return f;
}