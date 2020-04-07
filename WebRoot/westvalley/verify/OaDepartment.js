/**
 * Oa部门校验
 */
jQuery(document).ready(function () {

    var subm = window.checkCustomize;
    window.checkCustomize = function (obj) {
        var oabmbm = ModeForm.convertFieldNameToId("oabmbm", "main");
        var oabm = ModeForm.getFieldValue(oabmbm);
        var id = "";
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/verify/Verify-OaDepartment.jsp",
            data: 'oabm=' + oabm,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                id = returnJson.id;
            }
        });
        if (id != null && id != '') {
            top.Dialog.alert("OA部门编码：" + oabm + "已存在。");
            return false;
        } else {
            return true;
        }
    }
});
