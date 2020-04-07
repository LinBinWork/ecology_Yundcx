var userinfo = new Map();
jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        if(checkBudMoney()){
            callback();
        }
    });

    bindField0();
    bindDetailField1();
    bindDetailField2();
    bindDetailField3();

});
function bindField0(){
    //调整类型
    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    WfForm.bindFieldChangeEvent(dzlx,function(obj,id,value){
        WfForm.delDetailRow("detail_1", "all");
        WfForm.delDetailRow("detail_2", "all");
        WfForm.delDetailRow("detail_3", "all");

        var zxfzrz = WfForm.convertFieldNameToId("zxfzrz");// 子项负责人
        var fxfzrz = WfForm.convertFieldNameToId("fxfzrz");//	父项负责人
        var fxxmjlz = WfForm.convertFieldNameToId("fxxmjlz");//	父项项目经理
        WfForm.changeFieldValue(zxfzrz, {value:"",specialobj:[{id:"",name:""}]});
        WfForm.changeFieldValue(fxfzrz, {value:"",specialobj:[{id:"",name:""}]});
        WfForm.changeFieldValue(fxxmjlz, {value:"",specialobj:[{id:"",name:""}]});

        if(value == "0"){
            WfForm.addDetailRow("detail_1");
        }else        if(value == "1"){
            WfForm.addDetailRow("detail_2");
        }else        if(value == "2"){
            WfForm.addDetailRow("detail_3");
        }
    });
}
function bindDetailField1(){
    var requestID = WfForm.getBaseInfo().requestid;


    //绑定明细1
    var zxszx = WfForm.convertFieldNameToId("zxszx", "detail_1");//子项/孙子项
    var fxbm = WfForm.convertFieldNameToId("fxbm", "detail_1");//父项编码
    var xmysjy = WfForm.convertFieldNameToId("xmysjy", "detail_1");//金额

    var brzxszx = WfForm.convertFieldNameToId("brzxszx", "detail_1");//拨入子项孙子项


    var proj1Manager = WfForm.convertFieldNameToId("proj1Manager", "detail_1");
    var proj1Person = WfForm.convertFieldNameToId("proj1Person", "detail_1");

    var proj2OutManager = WfForm.convertFieldNameToId("proj2OutManager", "detail_1");
    var proj2OutPerson = WfForm.convertFieldNameToId("proj2OutPerson", "detail_1");


    WfForm.bindDetailFieldChangeEvent(zxszx, function (id, idx, value) {
        var p1fieldId = fxbm + "_" + idx;
        var amtfieldId = xmysjy + "_" + idx;

        var p1mfieldId = proj1Manager + "_" + idx;
        var p1pfieldId = proj1Person + "_" + idx;
        var p2mfieldId = proj2OutManager + "_" + idx;
        var p2pfieldId = proj2OutPerson + "_" + idx;


        WfForm.changeFieldValue(brzxszx + "_" + idx, {value: ""});
        if (isNull(value)) {
            WfForm.changeFieldValue(p1fieldId, {value: ""});
            WfForm.changeFieldValue(amtfieldId, {value: "0.00"});

            WfForm.changeFieldValue(p1mfieldId, {value: ""});
            WfForm.changeFieldValue(p1pfieldId, {value: ""});
            WfForm.changeFieldValue(p2mfieldId, {value: ""});
            WfForm.changeFieldValue(p2pfieldId, {value: ""});
        } else {
            getProjInfo(true, value, requestID, p1fieldId, amtfieldId, p1mfieldId, p1pfieldId, p2mfieldId, p2pfieldId);
        }
    });
    var brzxszx = WfForm.convertFieldNameToId("brzxszx", "detail_1");//拨入子项/孙子项
    var brqkyys = WfForm.convertFieldNameToId("brqkyys", "detail_1");//拨入前

    var proj2Manager = WfForm.convertFieldNameToId("proj2Manager", "detail_1");
    var proj2Person = WfForm.convertFieldNameToId("proj2Person", "detail_1");

    WfForm.bindDetailFieldChangeEvent(brzxszx, function (id, idx, value) {
        var p1fieldId = fxbm + "_" + idx;
        var amtfieldId = brqkyys + "_" + idx;


        var p2mfieldId = proj2Manager + "_" + idx;
        var p2pfieldId = proj2Person + "_" + idx;

        if (isNull(value)) {
            WfForm.changeFieldValue(amtfieldId, {value: "0.00"});
            WfForm.changeFieldValue(p2mfieldId, {value: ""});
            WfForm.changeFieldValue(p2pfieldId, {value: ""});
        } else {
            getProjInfo(false, value, requestID, p1fieldId, amtfieldId, "", "", p2mfieldId, p2pfieldId);
        }
    });
}
function bindDetailField2(){
    var requestID = WfForm.getBaseInfo().requestid;
//绑定明细2
    var fx = WfForm.convertFieldNameToId("fx", "detail_2");//父项
    var zx = WfForm.convertFieldNameToId("zx", "detail_2");//子项
    var xmysjy = WfForm.convertFieldNameToId("xmysjy", "detail_2");//父项
    WfForm.bindDetailFieldChangeEvent(fx, function (id, idx, value) {
        var amtfieldId = xmysjy + "_" + idx;
        WfForm.changeFieldValue(zx + "_" + idx, {value: ""});
        if (isNull(value)) {
            WfForm.changeFieldValue(amtfieldId, {value: "0.00"});
        } else {
            getProjBalance(value, requestID, amtfieldId);
        }
    });
    var brqkyje = WfForm.convertFieldNameToId("brqkyje", "detail_2");//子项
    WfForm.bindDetailFieldChangeEvent(zx, function (id, idx, value) {
        var amtfieldId = brqkyje + "_" + idx;
        if (isNull(value)) {
            WfForm.changeFieldValue(amtfieldId, {value: "0.00"});
        } else {
            getProjBalance(value, requestID, amtfieldId);
        }
    });
}
function bindDetailField3() {
    var requestID = WfForm.getBaseInfo().requestid;
    //绑定明细3
    var zx = WfForm.convertFieldNameToId("zx","detail_3");//子项
    var szx = WfForm.convertFieldNameToId("szx","detail_3");//孙子项

    var xmysjy = WfForm.convertFieldNameToId("xmysjy","detail_3");//子项
    WfForm.bindDetailFieldChangeEvent(zx,function(id,idx,value){
        var amtfieldId = xmysjy+"_"+idx;
        WfForm.changeFieldValue(szx+"_"+idx, {value:""});
        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjBalance(value,requestID,amtfieldId);
        }
    });
    var brqkyje = WfForm.convertFieldNameToId("brqkyje","detail_3");//孙子项
    WfForm.bindDetailFieldChangeEvent(szx,function(id,idx,value){
        var amtfieldId = brqkyje+"_"+idx;
        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjBalance(value,requestID,amtfieldId);
        }
    });
}
//刷新人力资源
function initDetailField(flag){

    var proj1ManagerA = [];
    var proj1PersonA = [];
    // var proj2ManagerA = [];
    var proj2PersonA = [];

    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    let dzlxV = WfForm.getFieldValue(dzlx);
    if(dzlxV == "0"){
        var proj1Manager = WfForm.convertFieldNameToId("proj1Manager","detail_1");
        var proj1Person = WfForm.convertFieldNameToId("proj1Person","detail_1");

        var proj2OutManager = WfForm.convertFieldNameToId("proj2OutManager","detail_1");
        var proj2OutPerson = WfForm.convertFieldNameToId("proj2OutPerson","detail_1");
        var proj2Manager = WfForm.convertFieldNameToId("proj2Manager","detail_1");
        var proj2Person = WfForm.convertFieldNameToId("proj2Person","detail_1");

        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        jQuery.each(rowids,function(i,idx){
            //合并人力资源
            if(flag){
                //合并父项
                let proj1ManagerV = WfForm.getFieldValue(proj1Manager+"_"+idx);
                if(!isNull(proj1ManagerV) && proj1ManagerA.indexOf(proj1ManagerV)<0){
                    proj1ManagerA.push(proj1ManagerV);
                }
                let proj1PersonV = WfForm.getFieldValue(proj1Person+"_"+idx);
                if(!isNull(proj1PersonV) && proj1PersonA.indexOf(proj1PersonV)<0){
                    proj1PersonA.push(proj1PersonV);
                }

                //合并子项
                let proj2OutPersonV = WfForm.getFieldValue(proj2OutPerson+"_"+idx);
                let proj2PersonV = WfForm.getFieldValue(proj2Person+"_"+idx);
                if(!isNull(proj2OutPersonV) && proj2PersonA.indexOf(proj2OutPersonV)<0){
                    proj2PersonA.push(proj2OutPersonV);
                }
                if(!isNull(proj2PersonV) && proj2PersonA.indexOf(proj2PersonV)<0){
                    proj2PersonA.push(proj2PersonV);
                }
            }

        });
    }else  if(dzlxV == "1"){
        var proj1Manager = WfForm.convertFieldNameToId("proj1Manager","detail_2");
        var proj1Person = WfForm.convertFieldNameToId("proj1Person","detail_2");

        var proj2Person = WfForm.convertFieldNameToId("proj2Person","detail_2");

        var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
        jQuery.each(rowids,function(i,idx){
                //合并父项
                let proj1ManagerV = WfForm.getFieldValue(proj1Manager+"_"+idx);
                if(!isNull(proj1ManagerV) && proj1ManagerA.indexOf(proj1ManagerV)<0){
                    proj1ManagerA.push(proj1ManagerV);
                    if(isNull(userinfo.get(proj1ManagerV))){
                        userinfo.set(proj1ManagerV,WfForm.getBrowserShowName(proj1Manager+"_"+idx));
                    }
                }
                let proj1PersonV = WfForm.getFieldValue(proj1Person+"_"+idx);
                if(!isNull(proj1PersonV) && proj1PersonA.indexOf(proj1PersonV)<0){
                    proj1PersonA.push(proj1PersonV);
                    if(isNull(userinfo.get(proj1PersonV))){
                        userinfo.set(proj1PersonV,WfForm.getBrowserShowName(proj1Person+"_"+idx));
                    }
                }
                let proj2PersonV = WfForm.getFieldValue(proj2Person+"_"+idx);
                if(!isNull(proj2PersonV) && proj2PersonA.indexOf(proj2PersonV)<0){
                    proj2PersonA.push(proj2PersonV);
                    if(isNull(userinfo.get(proj2PersonV))){
                        userinfo.set(proj2PersonV,WfForm.getBrowserShowName(proj2Person+"_"+idx));
                    }
                }
        });
    }else  if(dzlxV == "2"){
        var proj1Manager = WfForm.convertFieldNameToId("proj1Manager","detail_3");
        var proj1Person = WfForm.convertFieldNameToId("proj1Person","detail_3");

        var proj2Person = WfForm.convertFieldNameToId("proj2Person","detail_3");

        var rowids = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
        jQuery.each(rowids,function(i,idx){
            //合并父项
            let proj1ManagerV = WfForm.getFieldValue(proj1Manager+"_"+idx);
            if(!isNull(proj1ManagerV) && proj1ManagerA.indexOf(proj1ManagerV)<0){
                proj1ManagerA.push(proj1ManagerV);
                if(isNull(userinfo.get(proj1ManagerV))){
                    userinfo.set(proj1ManagerV,WfForm.getBrowserShowName(proj1Manager+"_"+idx));
                }
            }
            let proj1PersonV = WfForm.getFieldValue(proj1Person+"_"+idx);
            if(!isNull(proj1PersonV) && proj1PersonA.indexOf(proj1PersonV)<0){
                proj1PersonA.push(proj1PersonV);
                if(isNull(userinfo.get(proj1PersonV))){
                    userinfo.set(proj1PersonV,WfForm.getBrowserShowName(proj1Person+"_"+idx));
                }
            }
            let proj2PersonV = WfForm.getFieldValue(proj2Person+"_"+idx);
            if(!isNull(proj2PersonV) && proj2PersonA.indexOf(proj2PersonV)<0){
                proj2PersonA.push(proj2PersonV);
                if(isNull(userinfo.get(proj2PersonV))){
                    userinfo.set(proj2PersonV,WfForm.getBrowserShowName(proj2Person+"_"+idx));
                }
            }
        });
    }

    setTimeout(function(){
            var zxfzrz = WfForm.convertFieldNameToId("zxfzrz");// 子项负责人
            var fxfzrz = WfForm.convertFieldNameToId("fxfzrz");//	父项负责人
            var fxxmjlz = WfForm.convertFieldNameToId("fxxmjlz");//	父项项目经理

            var p2ptext = [];
            jQuery.each(proj2PersonA,function(i,id){
                p2ptext.push({id:id,name:userinfo.get(id)});
            });

            var p1mtext = [];
            jQuery.each(proj1ManagerA,function(i,id){
                p1mtext.push({id:id,name:userinfo.get(id)});
            });

            var p1ptext = [];
            jQuery.each(proj1PersonA,function(i,id){
                p1ptext.push({id:id,name:userinfo.get(id)});
            });
            WfForm.changeFieldValue(zxfzrz, {value:proj2PersonA.toString(),specialobj:p2ptext});
            WfForm.changeFieldValue(fxfzrz, {value:proj1PersonA.toString(),specialobj:p1ptext});
            WfForm.changeFieldValue(fxxmjlz, {value:proj1ManagerA.toString(),specialobj:p1mtext});
    },1000)


}

function checkBudMoney(){
    var f = true;
    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    let dzlxV = WfForm.getFieldValue(dzlx);
    if(dzlxV == "0"){
        //调拨
        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        var xmysjy = WfForm.convertFieldNameToId("xmysjy","detail_1");
        var brysje = WfForm.convertFieldNameToId("brysje","detail_1");
        jQuery.each(rowids,function(i,idx){
            var sqjeV = WfForm.getFieldValue(brysje+"_"+idx);
            var ysedV = WfForm.getFieldValue(xmysjy+"_"+idx);
            if(toNum(sqjeV) > toNum(ysedV)){
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "本次拨入预算金额不能大于项目预算结余",
                    onOk: function() {}
                });
                f = false;
                return f;
            }
        });
    }else if(dzlxV == "1"){
        //子项追加
        var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
        var xmysjy = WfForm.convertFieldNameToId("xmysjy","detail_2");
        var brysje = WfForm.convertFieldNameToId("brysje","detail_2");
        jQuery.each(rowids,function(i,idx){
            var sqjeV = WfForm.getFieldValue(brysje+"_"+idx);
            var ysedV = WfForm.getFieldValue(xmysjy+"_"+idx);
            if(toNum(sqjeV) > toNum(ysedV)){
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "本次追加预算金额不能大于项目预算结余",
                    onOk: function() {}
                });
                f = false;
                return f;
            }
        });
    }else if(dzlxV == "2"){
        //子项追加
        var rowids = WfForm.getDetailAllRowIndexStr("detail_3").split(",");
        var xmysjy = WfForm.convertFieldNameToId("xmysjy","detail_3");
        var brysje = WfForm.convertFieldNameToId("brysje","detail_3");
        jQuery.each(rowids,function(i,idx){
            var sqjeV = WfForm.getFieldValue(brysje+"_"+idx);
            var ysedV = WfForm.getFieldValue(xmysjy+"_"+idx);
            if(toNum(sqjeV) > toNum(ysedV)){
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "本次追加预算金额不能大于项目预算结余",
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
 * 获取可用余额
 */
function getProjInfo(flag,projID,requestID,p1fieldId,amtfieldId,p1mfieldId,p1pfieldId,p2mfieldId,p2pfieldId){
    var tp = "";
    var pNo = "";
    var balance = "0.00";
    var proj1Manager = "";
    var proj1Person = "";
    var proj2Manager = "";
    var proj2Person = "";


    window.ecCom.WeaTools.callApi("/westvalley/project/flow/cgProj23/cmd.jsp",
        "POST",
        {"cmd": "getProjInfo","flag":flag,"projID":projID,"requestID":requestID}
    ).then(function (jsonObj) {

        if (jsonObj.ok) {
            pNo = jsonObj.data.pNo;
            balance = jsonObj.data.balance;

            proj1Manager = jsonObj.data.proj1Manager;
            proj1Person = jsonObj.data.proj1Person;
            proj2Manager = jsonObj.data.proj2Manager;
            proj2Person = jsonObj.data.proj2Person;
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
            title: '系统出错！',
            content: '请联系系统管理员！',
            onOk: function () {
            }
        })
    }).finally(function(){
        if(flag){
            WfForm.changeFieldValue(p1fieldId, {value:pNo});
            WfForm.changeFieldValue(p1mfieldId, {value:proj1Manager});
            WfForm.changeFieldValue(p1pfieldId, {value:proj1Person});
        }
        WfForm.changeFieldValue(amtfieldId, {value:balance});

        WfForm.changeFieldValue(p2mfieldId, {value:proj2Manager});
        WfForm.changeFieldValue(p2pfieldId, {value:proj2Person});

        addUserName(proj1Manager);
        addUserName(proj1Person);
        addUserName(proj2Manager);
        addUserName(proj2Person);


        initDetailField(true);
    });
}

function getProjBalance(projID,requestID,amtfieldId){
    var balance = "0.00";


    window.ecCom.WeaTools.callApi("/westvalley/project/flow/cgProj23/cmd.jsp",
        "POST",
        {"cmd": "getProjBalance","projID":projID,"requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            balance = jsonObj.data.balance;

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
            title: '系统出错！',
            content: '请联系系统管理员！',
            onOk: function () {
            }
        })
    }).finally(function(){
        WfForm.changeFieldValue(amtfieldId, {value:balance});
        initDetailField(true);
    });
}

function addUserName(id){
    if(!isNull(id) && isNull(userinfo.get(id))){

        window.ecCom.WeaTools.callApi("/api/hrm/simpleinfo/getHrmSimpleInfo","GET",{"userid": id}
        ).then(function (jsonObj) {
            try{
                var n = jsonObj.simpleInfo.lastname;
                if(!isNull(n)){
                    if(isNull(userinfo.get(id))){
                        userinfo.set(id,n);
                    }
                }
            }catch(e){
            }
        }).catch(function (error) {
        })
    }

}
//# sourceURL=node1.js