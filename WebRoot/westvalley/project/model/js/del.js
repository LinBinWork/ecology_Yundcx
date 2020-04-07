
//删除祖项
function onDelproj0(id,params){
    window.ecCom.WeaTools.callApi("/westvalley/project/model/cmd/cmd.jsp",
        "POST",
        {"cmd":"delProject0","billid":id}
    ).then(function(jsonObj) {
        if(jsonObj.ok){
            ModeList.reloadTable();
        }else{
            antd.Modal.error({
                title: '删除失败',
                content: jsonObj.msg,
                onOk: function() {}
            })
        }
    }).catch(function(error) {
        antd.Modal.error({
            title: '系统出错！',
            content: error,
            onOk: function() {}
        })
    });
}

//删除祖项和成本中心
function onDelprojcost(id,params) {
    window.ecCom.WeaTools.callApi("/westvalley/project/model/cmd/cmd.jsp",
        "POST",
        {"cmd": "delProjcost", "billid": id}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            ModeList.reloadTable();
        } else {
            antd.Modal.error({
                title: '删除失败',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '系统出错！',
            content: error,
            onOk: function () {
            }
        })
    });
}
//保存时校验
function onsaveprojcost() {
    jQuery().ready(function(){
    //校验
    window.checkCustomize =()=>{
        var businessTypeID = ModeForm.convertFieldNameToId("businessType");
        var projNoID = ModeForm.convertFieldNameToId("projNo");
        var costNoID = ModeForm.convertFieldNameToId("costNo");

        var businessType = ModeForm.getFieldValue(businessTypeID);
        var projNo = ModeForm.getFieldValue(projNoID);
        var costNo = ModeForm.getFieldValue(costNoID);


        var billid = ModeForm.getCardUrlInfo().billid;

        var flag = true;
        jQuery.ajax({
            type:"POST",
            timeout:1000 * 60 * 20,
            url:"/westvalley/project/model/cmd/cmd.jsp",
            data:{"cmd": "saveProjcost", "billid": billid,"businessType": businessType,"projNo": projNo,"costNo": costNo},
            async:false,
            success:function(data){
                var jsonObj = jQuery.parseJSON(data);
                if(jsonObj.ok){
                    flag = true;
                }else{
                    flag = false;
                    antd.Modal.error({
                        title: '提示',
                        content: jsonObj.msg,
                        onOk: function () {
                        }
                    })
                }
            },
            error:function(e){
                flag = false;
                antd.Modal.error({
                    title: '提示',
                    content: "系统出错",
                    onOk: function () {
                    }
                })
            },
            complete:function(e){

            }
        });
        return flag;
    }
    });
}
/*
    导出营销项目预算额度汇总表
    javascript:exportBudAll();

 */
function exportBudAll(){
    var ids = ModeList.getCheckedID();
    if(ids == ''){
        ids = ModeList.getUnCheckedID();
    }
    window.ecCom.WeaTools.callApi("/westvalley/project/model/cmd/cmd.jsp",
        "POST",
        {"cmd": "exportBudAll", "ids": ids}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            var path = jsonObj.data.path;
            var name = "营销项目预算额度汇总表.xlsx";
            // window.open(path,"_blank");
            //jQuery.devdownload(path, name);
            if(jQuery("#downIFrame")){
                jQuery("#downIFrame").remove();
            }
            jQuery("<iframe style=\"display: none\" name=\"downIFrame\" id=\"downIFrame\" src="+path+"></iframe>").appendTo('body');
            // document.location.href='/westvalley/project/model/cmd/down.jsp'+"?name="+name+"&path="+path;
        } else {
            antd.Modal.error({
                title: '导出失败',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
        }
    }).catch(function (error) {
        antd.Modal.error({
            title: '系统出错！',
            content: error,
            onOk: function () {
            }
        })
    });
}
jQuery.devdownload = function (path,name) {
    var url = "/westvalley/project/model/cmd/down.jsp";
    jQuery('<form action="' + url + '" method="POST">' +  // action请求路径及推送方法
        '<input type="text" name="path" value="' + path + '"/>' + // 文件路径
        '<input type="text" name="name" value="' + name + '"/>' + // 文件路径
        '</form>')
        .appendTo('body').submit().remove();
};

