jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        if(checkBudMoney()){
            callback();
        }
    });

    var requestID = WfForm.getBaseInfo().requestid;
    var zxxmmc = WfForm.convertFieldNameToId("zxxmmc");
    var zxsyje = WfForm.convertFieldNameToId("zxsyje");//子项剩余金额
    //子项改变时更新剩余金额
    WfForm.bindFieldChangeEvent(zxxmmc,function(obj,id,value){
        if(isNull(value)){
            WfForm.changeFieldValue(zxsyje, {value:"0.00"});
        }else{
            getBudMoney(value,"getProj2Amt",requestID,zxsyje,"");
        }
	});

    var proj1ID = WfForm.convertFieldNameToId("proj1ID");//父项
    var fxsyje = WfForm.convertFieldNameToId("fxsyje");//父项剩余金额

    //子项改变时更新剩余金额
    WfForm.bindFieldChangeEvent(proj1ID,function(obj,id,value){
        if(isNull(value)){
            WfForm.changeFieldValue(fxsyje, {value:"0.00"});
        }else{
            var proj2V = WfForm.getFieldValue(zxxmmc);
            getBudMoney(value,"getProj1Amt",requestID,fxsyje,proj2V);
        }
    });


    //控制立项时间
    var zxfjrq = WfForm.convertFieldNameToId("zxfjrq");//子项
    //父项改变时更新剩余金额
    WfForm.bindFieldChangeEvent(zxfjrq,function(obj,id,value){
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
    var zxsyje = WfForm.convertFieldNameToId("zxsyje");//子项剩余金额
    WfForm.changeFieldAttr(zxsyje, "1");

    var fxsyje = WfForm.convertFieldNameToId("fxsyje");//父项剩余金额
    WfForm.changeFieldAttr(fxsyje, "1");

}
function checkBudMoney(){
    var f = true;

    var ctrlLevel = WfForm.convertFieldNameToId("ctrlLevel");

    var zxjehj = WfForm.convertFieldNameToId("zxjehj");

    var sqjeV = WfForm.getFieldValue(zxjehj);


    var ctrlLevelV = WfForm.getFieldValue(ctrlLevel);
    if(ctrlLevelV == "0"){//控制级别为父项
        var fxsyje = WfForm.convertFieldNameToId("fxsyje");
        var ysedV = WfForm.getFieldValue(fxsyje);
        if(toNum(sqjeV) > toNum(ysedV)){
            antd.Modal.error({
                title: '校验不通过！',
                content: "孙子项合计金额不能大于父项剩余金额！",
                onOk: function() {}
            });
            f = false;
        }
    }else{
        var zxsyje = WfForm.convertFieldNameToId("zxsyje");
        var ysedV = WfForm.getFieldValue(zxsyje);
        if(toNum(sqjeV) > toNum(ysedV)){
            antd.Modal.error({
                title: '校验不通过！',
                content: "孙子项合计金额不能大于子项剩余金额！",
                onOk: function() {}
            });
            f = false;
        }
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
function getBudMoney(projID,cmd,requestID,fieldId,proj2ID){
    WfForm.changeFieldAttr(fieldId, "1");

    window.ecCom.WeaTools.callApi("/westvalley/project/flow/proj3/cmd.jsp",
        "POST",
        {"cmd": cmd,"projID":projID,"requestID":requestID,"proj2ID":proj2ID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            WfForm.changeFieldValue(fieldId, {value:jsonObj.data});
        } else {
            antd.Modal.error({
                title: '孙子项可用余额获取失败',
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