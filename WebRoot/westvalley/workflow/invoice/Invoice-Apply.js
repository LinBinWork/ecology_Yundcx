/**
 * 发票校验
 */
jQuery(document).ready(function () {
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function (callback) {
        //检验发票类型是否已存在。
        if (checkBudData()) {
            callback();
        }
    });

});

function checkBudData() {
    var f = true;
    var fplx = WfForm.convertFieldNameToId("fplx", "main", true);//发票类型
    var fplxV = WfForm.getFieldValue(fplx);
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/invoice/Invoice.jsp",
        data: 'fplx=' + fplxV,
        dataType: "text",
        async: false,
        success: function (data) {
            var returnJson = eval("(" + data + ")");
            if (returnJson.id != null && returnJson.id != undefined && returnJson.id != '') {
                f = false;
            }
        }
    });
    if (!f) {
        antd.Modal.error({
            title: '提示！',
            content: "发票类型：" + fplx + "已存在。",
            onOk: function () {
            }
        });
    }
    return f;
}
