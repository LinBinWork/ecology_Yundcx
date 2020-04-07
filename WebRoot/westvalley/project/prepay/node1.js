jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        if(checkBudMoney()){
            checkProjExcustatus(callback);
        }
    });


    //合同类型改变  删除合同信息
    var sfywht = WfForm.convertFieldNameToId("sfywht");
    var htlc = WfForm.convertFieldNameToId("htlc");
    WfForm.bindFieldChangeEvent(sfywht,function(obj,id,value){
        WfForm.changeFieldValue(htlc, {value:"",specialobj:[{id:"",name:""}]});
	});

    var requestID = WfForm.getBaseInfo().requestid;

    //监听合同信息
    var htzje = WfForm.convertFieldNameToId("htzje");//总金额
    var htyfje = WfForm.convertFieldNameToId("htyfje");//已付
    var htwfje = WfForm.convertFieldNameToId("htwfje");//未付
    WfForm.bindFieldChangeEvent(htlc,function(id,idx,value){
        if(isNull(value)){
            WfForm.changeFieldValue(htzje,{value:"0.00"} );
            WfForm.changeFieldValue(htyfje,{value:"0.00"} );
            WfForm.changeFieldValue(htwfje,{value:"0.00"} );
        }else{
            getContracMoney(value,requestID,htzje,htyfje,htwfje);
        }
    });


    var zxszx = WfForm.convertFieldNameToId("zxszx","detail_1");
    var projBalance = WfForm.convertFieldNameToId("projBalance","detail_1");
    var proj1ID = WfForm.convertFieldNameToId("proj1ID","detail_1");
    WfForm.bindDetailFieldChangeEvent(zxszx,function(id,idx,value){
        var fieldId = projBalance+"_"+idx;
        var field2Id = proj1ID+"_"+idx;
        if(isNull(value)){
            WfForm.changeFieldValue(fieldId,{value:"0.00"} );
            WfForm.changeFieldValue(field2Id,{value:""} );
        }else{
            getBudMoney(value,requestID,fieldId,field2Id);
        }
    });

    //汇总父项
    WfForm.bindDetailFieldChangeEvent(proj1ID,function(id,idx,value){
        setTimeout(function(){
            flushProj1s();
        },500)

    });

    //项目经理
    var xmjl = WfForm.convertFieldNameToId("xmjl","detail_1");
    WfForm.bindDetailFieldChangeEvent(xmjl,function(id,idx,value){
        initManager();
    });

    var sjxmjl = WfForm.convertFieldNameToId("sjxmjl","detail_1");
    WfForm.bindDetailFieldChangeEvent(sjxmjl,function(id,idx,value){
        initPIDManager();
    });

});
function flushProj1s(){
//合计父项
    var projIDs = [];
    {
        var projSpans = [];
        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        var proj1ID = WfForm.convertFieldNameToId("proj1ID", "detail_1");
        jQuery.each(rowids, function (i, idx) {
            var proj1IDV = WfForm.getFieldValue(proj1ID + "_" + idx);
            if (projIDs.indexOf(proj1IDV) < 0) {
                projIDs.push(proj1IDV);
                projSpans.push({id: proj1IDV, name: proj1IDV});
            }
        });
        var proj1s = WfForm.convertFieldNameToId("proj1s");//	父项项目汇总
        WfForm.changeFieldValue(proj1s, {value: projIDs.toString(), specialobj: projSpans});
    }

    //因为流程带出的数据保存后不能自动删除  所以需要手动删除
    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"delProj1","projIDs":projIDs.toString()}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            var rownumIDs = [];
            var rowids = WfForm.getDetailAllRowIndexStr("detail_4").split(",");
            var ftbm = WfForm.convertFieldNameToId("ftbm", "detail_4");
            var proj1 = WfForm.convertFieldNameToId("proj1", "detail_4");
            jQuery.each(rowids, function (i, idx) {
                var ftbmV = WfForm.getFieldValue(ftbm + "_" + idx);
                var proj1V = WfForm.getFieldValue(proj1 + "_" + idx);
                var delKey = ftbmV + "@"+proj1V;
                if (jsonObj.data.indexOf(delKey) < 0) {
                    rownumIDs.push(idx);
                }
            });
            if(rownumIDs.length > 0){
                WfForm.delDetailRow("detail_4", rownumIDs.toString());
            }
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '提示',
            content: '更新分摊明细失败',
            onOk: function () {
            }
        })
    });

}

//校验项目是否已完成执行方案审批
function checkProjExcustatus(callback){
    var projIDs = [];
    var zxszx = WfForm.convertFieldNameToId("zxszx","detail_1");
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    jQuery.each(rowids,function(i,idx){
        var zxszxV = WfForm.getFieldValue(zxszx+"_"+idx);
        if(projIDs.indexOf(zxszxV) < 0){
            projIDs.push(zxszxV);
        }
    });
    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"checkExcustatus","projIDs":projIDs.toString()}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            callback();
        } else {
            antd.Modal.error({
                title: '提示',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '提示',
            content: '校验失败',
            onOk: function () {
            }
        })
    });


}
function initManager(){
    var p1mtext = [];
    var projManagerA = [];
    var xmjl = WfForm.convertFieldNameToId("xmjl","detail_1");
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    jQuery.each(rowids,function(i,idx){
        var xmjlV = WfForm.getFieldValue(xmjl+"_"+idx);
        if(projManagerA.indexOf(xmjlV) <= 0){
            p1mtext.push({id:xmjlV,name:WfForm.getBrowserShowName(xmjl+"_"+idx)});
            projManagerA.push(xmjlV);
        }
    });

    if(projManagerA.length > 0){
        WfForm.changeFieldValue(WfForm.convertFieldNameToId("xmjl"),
            {value:projManagerA.toString(),specialobj:p1mtext});
    }else{
        WfForm.changeFieldValue(WfForm.convertFieldNameToId("xmjl"),
            {value:"",specialobj:[{id:"",name:""}]});
    }

}
function initPIDManager(){
    var p1mtext = [];
    var projManagerA = [];
    var xmjl = WfForm.convertFieldNameToId("sjxmjl","detail_1");
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    jQuery.each(rowids,function(i,idx){
        var xmjlV = WfForm.getFieldValue(xmjl+"_"+idx);
        if(projManagerA.indexOf(xmjlV) <= 0) {
            p1mtext.push({id: xmjlV, name: WfForm.getBrowserShowName(xmjl + "_" + idx)});
            projManagerA.push(xmjlV);
        }
    });

    if(projManagerA.length > 0){
        WfForm.changeFieldValue(WfForm.convertFieldNameToId("sjxmjl"),
            {value:projManagerA.toString(),specialobj:p1mtext});
    }else{
        WfForm.changeFieldValue("",
            {value:"",specialobj:[{id:"",name:""}]});
    }
}

function checkBudMoney(){
    var f = true;
    //有无合同 0有
    var sfywht = WfForm.convertFieldNameToId("sfywht");
    var sfywhtV = WfForm.getFieldValue(sfywht);
    if("0" == sfywhtV){
        //有合同校验 本次预付款 <= 合同剩余金额
        var yfkje = WfForm.convertFieldNameToId("yfkje");
        var yfkjeV = WfForm.getFieldValue(yfkje);

        var htwfje = WfForm.convertFieldNameToId("htwfje");
        var htwfjeV = WfForm.getFieldValue(htwfje);

        if(toNum(yfkjeV) > toNum(htwfjeV)){
            antd.Modal.error({
                title: '提示',
                content: "本次预付款金额不能大于合同未付金额！",
                onOk: function() {}
            });
            f = false;
        }
    }else{
        // 无合同 ，分别校验  项目预算
        var fkje = WfForm.convertFieldNameToId("fkje","detail_1");
        var projBalance = WfForm.convertFieldNameToId("projBalance","detail_1");

        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        jQuery.each(rowids,function(i,idx){
            var fkjeV = WfForm.getFieldValue(fkje+"_"+idx);
            var projBalanceV = WfForm.getFieldValue(projBalance+"_"+idx);
            if(toNum(fkjeV) > toNum(projBalanceV)){
                antd.Modal.error({
                    title: '提示',
                    content: "第"+(i+1)+"行，本次付款金额不能项目可用金额！可用预算："+projBalanceV,
                    onOk: function() {}
                });
                f = false;
                return f;
            }
        });
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
 * 获取合同未付金额
 */
function getContracMoney(contractRequestID,requestID,total,already,fieldId){
    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"getContracMoney","requestID":requestID,"contractRequestID":contractRequestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            WfForm.changeFieldValue(total,{value:jsonObj.data.contractAmt} );
            WfForm.changeFieldValue(already,{value:jsonObj.data.contractUsedAmt} );
            WfForm.changeFieldValue(fieldId,{value:jsonObj.data.contractBalance} )
        } else {
            // antd.Modal.error({
            //     title: '合同未付金额获取失败',
            //     content: jsonObj.msg,
            //     onOk: function () {
            //     }
            // })
            WfForm.changeFieldValue(total,{value:"0.00"} );
            WfForm.changeFieldValue(already,{value:"0.00"} );
            WfForm.changeFieldValue(fieldId,{value:"0.00"} );
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '系统出错！',
            content: '请联系系统管理员！',
            onOk: function () {
            }
        })
        WfForm.changeFieldValue(total,{value:"0.00"} );
        WfForm.changeFieldValue(already,{value:"0.00"} );
        WfForm.changeFieldValue(fieldId,{value:"0.00"} )
    });
}

/**
 * 获取可用余额
 */
function getBudMoney(projID,requestID,fieldId,field2Id){

    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"getProjAmt","projID":projID,"requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            WfForm.changeFieldValue(fieldId, {value:jsonObj.data.balance});
            WfForm.changeFieldValue(field2Id, {value:jsonObj.data.proj1ID});
        } else {
            antd.Modal.error({
                title: '项目可用预算获取失败',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
            WfForm.changeFieldValue(fieldId, {value:"0.00"});
            WfForm.changeFieldValue(field2Id, {value:""});
        }
    }).catch(function (error) {
        WfForm.changeFieldValue(fieldId, {value:"0.00"});
        WfForm.changeFieldValue(field2Id, {value:""});
    })
}
//# sourceURL=node1.js