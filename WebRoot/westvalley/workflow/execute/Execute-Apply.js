/**
 * 引用流程：执行方案申请流程
 * 引用节点：申请节点
 * 主要功能：带出关联项目的信息
 */
jQuery(document).ready(function () {

    var glxm = WfForm.convertFieldNameToId("glxm", "main", true);//关联项目
    WfForm.bindFieldChangeEvent(glxm, function (id, rowIndex, value) {
        var xmlxje = WfForm.convertFieldNameToId("xmlxje", "main", true);//项目立项金额
        var glxmN = WfForm.getBrowserShowName(glxm);//项目名称
        glxmN = jQuery(glxmN).html();
        WfForm.changeFieldValue("field-1", {value: glxmN + "执行方案申请"});
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/execute/Execute.jsp",
            data: 'op=getMoney&&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null) {
                    if (returnJson.money != undefined && returnJson.money != null) {
                        WfForm.changeFieldValue(xmlxje, {value: returnJson.money});
                    } else {
                        WfForm.changeFieldValue(xmlxje, {value: 0.00});
                    }

                }
            }
        });

        var xmjl = WfForm.convertFieldNameToId("xmjl", "main", true);//项目经理
        var xmfzr = WfForm.convertFieldNameToId("xmfzr", "main", true);//项目负责人
        jQuery.ajax({
            type: "POST",
            url: "/westvalley/workflow/execute/Execute.jsp",
            data: 'op=getManager&&id=' + value,
            dataType: "text",
            async: false,
            success: function (data) {
                var returnJson = eval("(" + data + ")");
                if (returnJson != null) {
                    WfForm.changeFieldValue(xmjl, {
                        value: returnJson.xmjl,
                        specialobj: [{id: returnJson.xmjl, name: returnJson.jlname}]
                    });
                    WfForm.changeFieldValue(xmfzr, {
                        value: returnJson.zxxmfzr,
                        specialobj: [{id: returnJson.zxxmfzr, name: returnJson.zxxmfzrname}]
                    });
                }
            }
        });
    });

});
