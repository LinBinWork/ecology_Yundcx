/***
 * 引用流程：月度预算申请
 * 引用节点：申请节点
 * 主要功能：
 */
jQuery(document).ready(function () {

    var ywlb = WfForm.convertFieldNameToId("ywlb", "main", true);
    var zxmc = WfForm.convertFieldNameToId("zxmc", "detail_1", true);//祖项名称
    var bm = WfForm.convertFieldNameToId("fygzbm", "detail_1", true);//费用归属部门名称
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