//@ sourceURL=/OaProject.js
jQuery(document).ready(function () {
    /**
     * 根据项目名带出项目的各种信息
     */
    WfForm.bindFieldChangeEvent(WfForm.convertFieldNameToId("xmmc"), function (obj, id, value) {
        var xmmc = WfForm.getFieldValue(WfForm.convertFieldNameToId("xmmc"));
        var gljklc = WfForm.getFieldValue(WfForm.convertFieldNameToId("gljklc"));
        var url = "/westvalley/workflow/repayment/OaProject.jsp?xmmc=" + xmmc + "&gljklc=" + gljklc;
        jQuery.ajax({
            type: "get",
            url: url,
            dataType: "json",
            async: false,
            success: function (data) {
                WfForm.changeFieldValue(WfForm.convertFieldNameToId("xmmc"), {
                    value: data.ProjID,
                    specialobj: [{id: data.ProjID, name: data.ProjName},]
                });
                WfForm.changeFieldValue(WfForm.convertFieldNameToId("xmbm"), {value: data.ProjNo});
                WfForm.changeFieldValue(WfForm.convertFieldNameToId("bxmdqkyje"), {value: data.projBalance});
                WfForm.changeFieldValue(WfForm.convertFieldNameToId("dqjkye"), {value: data.moneylast})
            }
        });
    });


    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        var isBoolean = 0;
        if (!checkIt()) {
            isBoolean++;
        }
        //提交
        if (isBoolean == 0) {
            callback();
        }
    });


});

/**
 * 判断还款金额不得大于借款余额
 */
function checkIt() {
    var returnvalue = true;
    var bchkje = WfForm.getFieldValue(WfForm.convertFieldNameToId("bchkje"));
    var dqjkye = WfForm.getFieldValue(WfForm.convertFieldNameToId("dqjkye"));
    if (Number(bchkje) > Number(dqjkye)) {
        window.top.Dialog.alert("还款不得大于借款");
        returnvalue = false;
    }
    return returnvalue;
}