jQuery(document).ready(function(){
    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        if(checkBudMoney() && checkDeptRepit()){
            callback();
        }
    });
    var glzx = WfForm.convertFieldNameToId("glzx");
    var xmmc = WfForm.convertFieldNameToId("xmmc");
    //祖项改变时删除明细
    WfForm.bindFieldChangeEvent(glzx,function(obj,id,value){
        WfForm.delDetailRow("detail_2", "all");
        WfForm.addDetailRow("detail_2",{});

        if(isNull(value)){
            WfForm.changeFieldValue("field-1", {value:""});
        }else{
            //“项目编号”+父项名称+'立项申请'
            var title = value +"-"+ WfForm.getFieldValue(xmmc)+"-立项申请";
            WfForm.changeFieldValue("field-1",{value:title});
        }
    });
    WfForm.bindFieldChangeEvent(xmmc,function(obj,id,value){
        var title = WfForm.getFieldValue(glzx)  +"-"+ value+"-立项申请";
        WfForm.changeFieldValue("field-1",{value:title});
    });


    //初始化字段
    setTimeout(function(){
        initDetail2Field();
    },700);

    //监听明细2行添加事件
    WfForm.registerAction(WfForm.ACTION_ADDROW+"2", function(idx){
        initDetail2Field();
    });

    var requestID = WfForm.getBaseInfo().requestid;
    var lxkyje = WfForm.convertFieldNameToId("lxkyje","detail_2");//立项可用金额
    var ftbm = WfForm.convertFieldNameToId("ftbm","detail_2");//分摊部门
    WfForm.bindDetailFieldChangeEvent(ftbm,function(id,idx,value){
        var ftbmV = WfForm.getFieldValue(ftbm+"_"+idx);
        var fieldId = lxkyje+"_"+idx;
        if(isNull(ftbmV) || !checkDeptRepit()){
            WfForm.changeFieldValue(fieldId, {value:"0.00"});
        }else{
            getBudMoney(ftbmV,requestID,fieldId);
        }
    });
    var bclxje = WfForm.convertFieldNameToId("bclxje","detail_2");//本次立项金额
    WfForm.bindDetailFieldChangeEvent(bclxje,function(id,idx,value){
        flushPrecent();
    });

});

//刷新分摊比例
function flushPrecent(){
    var lxzje = WfForm.convertFieldNameToId("lxzje");//立项总金额
    var lxzjeV = WfForm.getFieldValue(lxzje);

    var bclxje = WfForm.convertFieldNameToId("bclxje","detail_2");//本次立项金额
    var ftbl = WfForm.convertFieldNameToId("ftbl","detail_2");//分摊比例

    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    jQuery.each(rowids,function(i,idx){
        var bclxjeV = WfForm.getFieldValue(bclxje+"_"+idx);
        window.ecCom.WeaTools.math().then(function(math){
            var ftblV = math.round(math.multiply(math.divide(bclxjeV,lxzjeV),100),2);
            WfForm.changeFieldValue(ftbl+"_"+idx, {value:ftblV});
        })
    });



}
//校验是否分摊部门是否重复
function checkDeptRepit() {
    var f = true;
    var ftbm = WfForm.convertFieldNameToId("ftbm","detail_2");//分摊部门
    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    var bms = [];
    jQuery.each(rowids,function(i,idx){
        var ftbmV = WfForm.getFieldValue(ftbm+"_"+idx);
        if(bms.indexOf(ftbmV) >= 0){
            antd.Modal.error({
                title: '校验不通过！',
                content: "分摊部门不能重复",
                onOk: function() {}
            });
            f = false;
            WfForm.changeFieldValue(ftbm+"_"+idx,
                {
                    value: "",
                    valueSpan: "",
                    specialobj: {
                        id: "",
                        name: "",
                    }
                });
            return f;
        }else{
            if(!isNull(ftbmV)){
                bms.push(ftbmV);
            }
        }
    });
    return f;
}

function initDetail2Field(){
    var lxkyje = WfForm.convertFieldNameToId("lxkyje","detail_2");//立项可用金额
    var ftbl = WfForm.convertFieldNameToId("ftbl","detail_2");//分摊
    //立项可用金额设置为只读
    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    jQuery.each(rowids,function(i,idx){
        WfForm.changeFieldAttr(lxkyje+"_"+idx, "1");
        WfForm.changeFieldAttr(ftbl+"_"+idx, "1");
    });

}
function checkBudMoney(){
    var f = true;
    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    var bclxje = WfForm.convertFieldNameToId("bclxje","detail_2");
    var lxkyje = WfForm.convertFieldNameToId("lxkyje","detail_2");
    jQuery.each(rowids,function(i,idx){
        var sqjeV = WfForm.getFieldValue(bclxje+"_"+idx);
        var ysedV = WfForm.getFieldValue(lxkyje+"_"+idx);
        if(toNum(sqjeV) > toNum(ysedV)){
            antd.Modal.error({
                title: '提示！',
                content: "本次立项总金额大于立项可用金额，无法立项，如需立项，请先联系运营部完成预算调整。",
                onOk: function() {}
            });
            f = false;
            return false;
        }
    });


    var lxzje = WfForm.convertFieldNameToId("lxzje");
    var gjsxjehj = WfForm.convertFieldNameToId("gjsxjehj");

    var lxzjeV = WfForm.getFieldValue(lxzje);//立项总金额
    var gjsxjehjV = WfForm.getFieldValue(gjsxjehj);//关键事项金额合计
    if(toNum(gjsxjehjV) > toNum(lxzjeV)){
        antd.Modal.error({
            title: '校验不通过！',
            content: "关键事项金额合计不能大于立项总金额",
            onOk: function() {}
        });
        f = false;
        return false;
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

    window.ecCom.WeaTools.callApi("/westvalley/project/flow/proj1/cmd.jsp",
        "POST",
        {"cmd":"getProj0Amt","projID":projID,"requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            WfForm.changeFieldValue(fieldId, {value:jsonObj.data});
        } else {
            antd.Modal.error({
                title: '父项可用余额获取失败',
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