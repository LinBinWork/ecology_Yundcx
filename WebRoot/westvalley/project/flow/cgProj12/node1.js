var userinfo = new Map();
jQuery(document).ready(function(){

    WfForm.registerCheckEvent(WfForm.OPER_SUBMIT, function(callback){
        //校验申请金额不能大于可用预算
        checkBudMoney(callback);
    });
    bindField0();


    bindDetailField1();
    bindDetailField2();

    initDetail1();




});
function bindDetailField2(){
    var requestID = WfForm.getBaseInfo().requestid;
    var zx = WfForm.convertFieldNameToId("zx","detail_2");//拨出
    var zxysjy = WfForm.convertFieldNameToId("zxysjy","detail_2");//拨出

    var fx = WfForm.convertFieldNameToId("fx","detail_2");//父项
    WfForm.bindDetailFieldChangeEvent(zx,function(id,idx,value){
        //带出余额
        var amtfieldId = zxysjy+"_"+idx;

        WfForm.changeFieldValue(fx+"_"+idx, {value:"",specialobj:[{id:"",name:""}]});

        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjInfo(value,requestID,amtfieldId);
        }
    });

    var brqkyje = WfForm.convertFieldNameToId("brqkyje","detail_2");//父项金额
    WfForm.bindDetailFieldChangeEvent(fx,function(id,idx,value){
        //带出余额
        var amtfieldId = brqkyje+"_"+idx;
        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjInfo(value,requestID,amtfieldId);
        }
    });

    var proj1Man = WfForm.convertFieldNameToId("proj1Man","detail_2");
    var proj1Per = WfForm.convertFieldNameToId("proj1Per","detail_2");
    //绑定人员
    WfForm.bindDetailFieldChangeEvent(proj1Man+","+proj1Per,function(id,idx,value){
        if(!isNull(value)){
            addUserName(id+"_"+idx,value);
        }
        flushManager();
    });


}
function bindDetailField1(){
    var requestID = WfForm.getBaseInfo().requestid;

    var zxfx = WfForm.convertFieldNameToId("zxfx","detail_1");//拨出
    var xmysjy = WfForm.convertFieldNameToId("xmysjy","detail_1");
    WfForm.bindDetailFieldChangeEvent(zxfx,function(id,idx,value){
        //带出余额
        var amtfieldId = xmysjy+"_"+idx;
        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjInfo(value,requestID,amtfieldId);
        }
    });
    var brzxfx = WfForm.convertFieldNameToId("brzxfx","detail_1");//拨入
    var brqkyje = WfForm.convertFieldNameToId("brqkyje","detail_1");
    WfForm.bindDetailFieldChangeEvent(brzxfx,function(id,idx,value){
        //带出余额
        var amtfieldId = brqkyje+"_"+idx;
        if(isNull(value)){
            WfForm.changeFieldValue(amtfieldId, {value:"0.00"});
        }else{
            getProjInfo(value,requestID,amtfieldId);
        }
    });

    var proj1M = WfForm.convertFieldNameToId("proj1M","detail_1");
    var proj1P = WfForm.convertFieldNameToId("proj1P","detail_1");
    var inproj1M = WfForm.convertFieldNameToId("inproj1M","detail_1");
    var inproj1P = WfForm.convertFieldNameToId("inproj1P","detail_1");
    //绑定人员
    WfForm.bindDetailFieldChangeEvent(proj1M+","+proj1P+","+inproj1M+","+inproj1P,function(id,idx,value){
        if(!isNull(value)){
            addUserName(id+"_"+idx,value);
        }
        flushManager();
    });
}

function bindField0(){
    //调整类型
    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    WfForm.bindFieldChangeEvent(dzlx,function(obj,id,value){
        WfForm.delDetailRow("detail_1", "all");
        WfForm.delDetailRow("detail_2", "all");

        var fxfzr = WfForm.convertFieldNameToId("fxfzr");//	父项负责人
        var fxxmjl = WfForm.convertFieldNameToId("fxxmjl");//	父项项目经理
        WfForm.changeFieldValue(fxfzr, {value:"",specialobj:[{id:"",name:""}]});
        WfForm.changeFieldValue(fxxmjl, {value:"",specialobj:[{id:"",name:""}]});

        if(value == "0" ||value == "1" ||value == "2" ){
            WfForm.addDetailRow("detail_1");
        }else        if(value == "3"){
            WfForm.addDetailRow("detail_2");
            //隐藏添加、删除、复制
            jQuery("i[name='delbutton1']").parent().hide();
        }
    });
    var dzlxV = WfForm.getFieldValue(WfForm.convertFieldNameToId("dzlx"));
    if(dzlxV == "3"){
        jQuery("i[name='delbutton1']").parent().hide();
        initProj0Detail();
    }

    //父项
    var fx = WfForm.convertFieldNameToId("fx");
    WfForm.bindFieldChangeEvent(fx,function(obj,id,value){
        WfForm.delDetailRow("detail_2", "all");

        var fxfzr = WfForm.convertFieldNameToId("fxfzr");//	父项负责人
        var fxxmjl = WfForm.convertFieldNameToId("fxxmjl");//	父项项目经理
        WfForm.changeFieldValue(fxfzr, {value:"",specialobj:[{id:"",name:""}]});
        WfForm.changeFieldValue(fxxmjl, {value:"",specialobj:[{id:"",name:""}]});

        if(!isNull(value)){
            addProj0Detail(value);
        }
    });
}
//所有的祖项和父项都是只读
function initProj0Detail(){
    var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
    var zx = WfForm.convertFieldNameToId("zx","detail_2");
    var fx = WfForm.convertFieldNameToId("fx","detail_2");

    jQuery.each(rowids,function(i,idx){
        WfForm.changeFieldAttr(zx+"_"+idx, 1);
        WfForm.changeFieldAttr(fx+"_"+idx, 1);
    });
}
//添加祖项
function addProj0Detail(proj1ID){
    var requestID = WfForm.getBaseInfo().requestid;
    window.ecCom.WeaTools.callApi("/westvalley/project/flow/cgProj12/cmd.jsp",
        "POST",
        {"cmd": "getAllProj0","proj1ID":proj1ID,"requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            var proj1Name = jsonObj.data.proj1Name;
            var proj1Balance = jsonObj.data.proj1Balance;

            WfForm.changeFieldValue(WfForm.convertFieldNameToId("brqkyje"), {value:proj1Balance});

            var zx = WfForm.convertFieldNameToId("zx","detail_2");
            var fx = WfForm.convertFieldNameToId("fx","detail_2");
            var bfb = WfForm.convertFieldNameToId("bfb","detail_2");
            jQuery.each(jsonObj.data.proj0List,function(i,item){
                WfForm.addDetailRow("detail_2");
            });
            var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");

            jQuery.each(rowids, function (i, idx) {
                var proj0 = jsonObj.data.proj0List[i];

                WfForm.changeFieldValue(zx + "_" + idx, {value:proj0.proj0ID,specialobj:[{id:proj0.proj0ID,name:proj0.proj0Name}]});
                WfForm.changeFieldValue(bfb + "_" + idx, {value:proj0.proj0Precent});
                WfForm.changeFieldValue(fx + "_" + idx, {value:proj1ID,specialobj:[{id:proj1ID,name:proj1Name}]});
            });


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
        initProj0Detail();
    });

}


function _customAddFun0(addIndexStr){ //明细1新增成功后触发事件，addIndexStr即刚新增的行标示，
    initDetail1();
}

function addUserName(fieldid,id){
    if(!isNull(id)){
        var ids = id.split(",");
        var names = WfForm.getBrowserShowName(fieldid).split(",");

        jQuery.each(ids,function(i,id){
            if(!isNull(id)){
                if(isNull(userinfo.get(id))){
                    userinfo.set(id,names[i]);
                }
            }
        })
    }
}
//刷新父项人员
function flushManager(){

    var proj1ManagerA = [];
    var proj1PersonA = [];

    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    var dzlxV = WfForm.getFieldValue(dzlx);
    if(dzlxV == "0" ||dzlxV == "1" ||dzlxV == "2" ){
        var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
        var proj1M = WfForm.convertFieldNameToId("proj1M","detail_1");
        var proj1P = WfForm.convertFieldNameToId("proj1P","detail_1");
        var inproj1M = WfForm.convertFieldNameToId("inproj1M","detail_1");
        var inproj1P = WfForm.convertFieldNameToId("inproj1P","detail_1");

        var projType = WfForm.convertFieldNameToId("dzlx");
        var projTypeV = WfForm.getFieldValue(projType);
        jQuery.each(rowids,function(i,idx){
            var p1m = WfForm.getFieldValue(proj1M+"_"+idx);
            var p1p = WfForm.getFieldValue(proj1P+"_"+idx);
            var inp1m = WfForm.getFieldValue(inproj1M+"_"+idx);
            var inp1p = WfForm.getFieldValue(inproj1P+"_"+idx);

            if(projTypeV == "2") {
                if (!isNull(p1m) && proj1ManagerA.indexOf(p1m) < 0)
                    proj1ManagerA.push(p1m);
                if (!isNull(inp1m) && proj1ManagerA.indexOf(inp1m) < 0)
                    proj1ManagerA.push(inp1m);

                if (!isNull(p1p) && proj1PersonA.indexOf(p1p) < 0)
                    proj1PersonA.push(p1p);
                if (!isNull(inp1p) && proj1PersonA.indexOf(inp1p) < 0)
                    proj1PersonA.push(inp1p);
            }
        });
    }else  if(dzlxV == "3"){
        var rowids = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
        var proj1M = WfForm.convertFieldNameToId("proj1Man","detail_2");
        var proj1P = WfForm.convertFieldNameToId("proj1Per","detail_2");

        jQuery.each(rowids,function(i,idx){
            var p1m = WfForm.getFieldValue(proj1M+"_"+idx);
            var p1p = WfForm.getFieldValue(proj1P+"_"+idx);

            if (!isNull(p1m) && proj1ManagerA.indexOf(p1m) < 0)
                proj1ManagerA.push(p1m);
            if (!isNull(p1p) && proj1ManagerA.indexOf(p1p) < 0)
                proj1PersonA.push(p1p);

        });
    }


    setTimeout(function(){
        var fxfzr = WfForm.convertFieldNameToId("fxfzr");//	父项负责人
        var fxxmjl = WfForm.convertFieldNameToId("fxxmjl");//	父项项目经理

        var p1ptext = [];
        jQuery.each(proj1PersonA,function(i,id){
            p1ptext.push({id:id,name:userinfo.get(id)});
        });

        var p1mtext = [];
        jQuery.each(proj1ManagerA,function(i,id){
            p1mtext.push({id:id,name:userinfo.get(id)});
        });

        WfForm.changeFieldValue(fxfzr, {value:proj1PersonA.toString(),specialobj:p1ptext});
        WfForm.changeFieldValue(fxxmjl, {value:proj1ManagerA.toString(),specialobj:p1mtext});
    },1000)

}

function initDetail1(){
    var dzlx = WfForm.convertFieldNameToId("dzlx");//调整类型
    var dzlxV = WfForm.getFieldValue(dzlx);


    //年度预算调整只填写拨入
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");
    if(dzlxV == "0"){
        var zxfx = WfForm.convertFieldNameToId("zxfx","detail_1");
        jQuery.each(rowids,function(i,idx){
            WfForm.changeFieldAttr(zxfx+"_"+idx, "1");
        });
    }
}

function checkBudMoney(callback){
    var f = true;
    var rowids = WfForm.getDetailAllRowIndexStr("detail_1").split(",");

    var dzlx = WfForm.getFieldValue(WfForm.convertFieldNameToId("dzlx"));
    if(dzlx == "0" ||dzlx == "1" ||dzlx == "2" ) {
        var dzhje = WfForm.convertFieldNameToId("dzhje", "detail_1");
        var xmysjy = WfForm.convertFieldNameToId("xmysjy", "detail_1");
        var brysje = WfForm.convertFieldNameToId("brysje", "detail_1");
        jQuery.each(rowids, function (i, idx) {
            var dzhjeV = WfForm.getFieldValue(dzhje + "_" + idx);
            if (toNum(dzhjeV) < 0) {
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "第" + (i + 1) + "行，调整后金额不能小于0",
                    onOk: function () {
                    }
                });
                f = false;
                return f;
            }
            if (dzlx != "0") {
                var ysedV = WfForm.getFieldValue(xmysjy + "_" + idx);
                var sqjeV = WfForm.getFieldValue(brysje + "_" + idx);
                if (toNum(sqjeV) > toNum(ysedV)) {
                    antd.Modal.error({
                        title: '校验不通过！',
                        content: "本次拨入预算金额不能大于项目预算结余",
                        onOk: function () {
                        }
                    });
                    f = false;
                    return f;
                }
            }
        });
    }else {
        var rowids2 = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
        var brtoysje = WfForm.convertFieldNameToId("brtoysje", "detail_2");
        var zxysjy = WfForm.convertFieldNameToId("zxysjy", "detail_2");
        jQuery.each(rowids2, function (i, idx) {
            var brtoysjeV = WfForm.getFieldValue(brtoysje + "_" + idx);
            if (toNum(brtoysjeV) <= 0) {
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "第" + (i + 1) + "行，追加金额必须大于0",
                    onOk: function () {
                    }
                });
                f = false;
                return f;
            }
            var ysedV = WfForm.getFieldValue(zxysjy + "_" + idx);
            if (toNum(brtoysjeV) > toNum(ysedV)) {
                antd.Modal.error({
                    title: '校验不通过！',
                    content: "本次追加预算金额不能大于项目预算结余",
                    onOk: function () {
                    }
                });
                f = false;
                return f;
            }
        });

    }
    if(f ){
        if(dzlx == "3" ){
            var rowids2 = WfForm.getDetailAllRowIndexStr("detail_2").split(",");
            var fx = WfForm.convertFieldNameToId("fx", "detail_2");
            var brtoysje = WfForm.convertFieldNameToId("brtoysje", "detail_2");
            var bfb = WfForm.convertFieldNameToId("bfb", "detail_2");

            var fxzjjeV = WfForm.getFieldValue(WfForm.convertFieldNameToId("fxzjje"));
            jQuery.each(rowids2, function (i, idx) {
                var brtoysjeV = WfForm.getFieldValue(brtoysje + "_" + idx);
                var bfbV = WfForm.getFieldValue(bfb + "_" + idx);
                var bfb1 = toFloat(toNum(brtoysjeV) / toNum(fxzjjeV) * 100);
                if( bfb1 - bfbV <= -1 || bfb1 - bfbV >= 1  ){//绝对值小于1就行，无法精确判断
                    f = false;
                    antd.Modal.error({
                        title: '提示',
                        content: "父项追加金额后，分摊比例必须保持不变，原比例："+bfbV+",追加后比例："+bfb1,
                        onOk: function () {
                        }
                    })
                    return f;
                }
            });
        }
    }
    if(f){
        callback();
    }
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
function toFloat(v){
    v=String(v).replace(/,/g, "");
    if (isNull(v)) {
        return "0.00";
    }
    return Math.round(v * Math.pow(10, 2)) / Math.pow(10, 2);

}

/**
 * 获取可用余额
 */
function getProjInfo(projID,requestID,amtfieldId){
    var balance = "0.00";

    window.ecCom.WeaTools.callApi("/westvalley/project/flow/cgProj12/cmd.jsp",
        "POST",
        {"cmd": "getProjInfo","projID":projID,"requestID":requestID}
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
    });
}
//# sourceURL=node1.js