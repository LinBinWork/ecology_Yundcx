jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        if(checkBudMoney()){
            callback();
        }
    });

    var requestID = WfForm.getBaseInfo().requestid;
    var glfx = WfForm.convertFieldNameToId("glfx");
    var xmbh = WfForm.convertFieldNameToId("xmbh");
    var fxsyje = WfForm.convertFieldNameToId("fxsyje");//父项剩余金额
    //父项改变时更新剩余金额
    WfForm.bindFieldChangeEvent(glfx,function(obj,id,value){
        if(isNull(value)){
            WfForm.changeFieldValue(fxsyje, {value:"0.00"});
        }else{
            getBudMoney(value,requestID,fxsyje);
        }
	});
    WfForm.bindFieldChangeEvent(xmbh,function(obj,id,value){
        if(isNull(value)){
            WfForm.changeFieldValue("field-1",{value:""});
        }else{
            var title =  WfForm.getFieldValue(xmbh)+"-"
                + jQuery(WfForm.getBrowserShowName(glfx)).html()+"-分解子项";
            WfForm.changeFieldValue("field-1",{value:title});
        }
    });


    //控制立项时间
    var fxlxrq = WfForm.convertFieldNameToId("fxlxrq");//父项
    //父项改变时更新剩余金额
    WfForm.bindFieldChangeEvent(fxlxrq,function(obj,id,value){
        if(isNull(value)){
        }else{
            WfForm.controlDateRange(WfForm.convertFieldNameToId("fjrq"), value)
        }
    });



    //初始化字段
    setTimeout(function(){
        initMainField();
    },700);


});

function initMainField(){
    var fxsyje = WfForm.convertFieldNameToId("fxsyje");//父项剩余金额
    WfForm.changeFieldAttr(fxsyje, "1");
}
function checkBudMoney(){
    var f = true;

    var zxjehj = WfForm.convertFieldNameToId("zxjehj");
    var fxsyje = WfForm.convertFieldNameToId("fxsyje");
    var sqjeV = WfForm.getFieldValue(zxjehj);
    var ysedV = WfForm.getFieldValue(fxsyje);
    if(toNum(sqjeV) > toNum(ysedV)){
        antd.Modal.error({
            title: '校验不通过！',
            content: "子项合计金额不能大于父项剩余金额！",
            onOk: function() {}
        });
        f = false;
    }
    return f;
}
function toNum(v){
    if (isNull(v)) {
        return 0;
    }
    return parseFloat(String(v).replace(/,/g, ""));
};
function isNull(v){
    if (typeof(v) == "undefined" || v == "" || v == null) {
        return true;
    }
    return false;
};

/**
 * 获取可用余额
 */
function getBudMoney(projID,requestID,fieldId){
    WfForm.changeFieldAttr(fieldId, "1");

    window.ecCom.WeaTools.callApi("/westvalley/project/flow/proj2/cmd.jsp",
        "POST",
        {"cmd": "getProj1Amt","projID":projID,"requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            WfForm.changeFieldValue(fieldId, {value:jsonObj.data});
        } else {
            antd.Modal.error({
                title: '子项可用余额获取失败',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
            WfForm.changeFieldValue(fieldId, {value:"0.00"});
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '系统出错！',
            content: '请联系系统管理员！',
            onOk: function () {
            }
        })
        WfForm.changeFieldValue(fieldId, {value:"0.00"});
    });
}
//# sourceURL=node1.js