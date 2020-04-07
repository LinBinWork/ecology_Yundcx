jQuery(document).ready(function(){
	setTimeout(function(){
        addDevBtn();
        autoFlush();
	},700);

});
function addDevBtn(){
    jQuery("#auto").append("<input class=\"ant-btn ant-btn-ghost\" onclick=\"autoClick();\" type=\"button\" value=\"刷新分录\"/>");
}

/**
 * 加载刷新分录
 */
function autoFlush(){
    var requestID = WfForm.getBaseInfo().requestid;
    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"autoFlush","requestID":requestID}
    ).then(function (jsonObj) {
        if (jsonObj.ok) {
            //如果data==1就不刷新
            if(isNull(jsonObj.data) || jsonObj.data != "1"){
                WfForm.reloadPage();
            }
        } else {
            antd.Modal.error({
                title: '刷新失败',
                content: jsonObj.msg,
                onOk: function () {
                }
            })
        }
    }).catch(function (error) {
        try{
            console.log(error);
        }catch(e){}
    })
}
function autoClick(){
    var requestID = WfForm.getBaseInfo().requestid;
    window.ecCom.WeaTools.callApi("/westvalley/project/prepay/cmd.jsp",
        "POST",
        {"cmd":"autoClick","requestID":requestID}
    ).then(function (jsonObj) {
        WfForm.doRightBtnEvent("BTN_WFSAVE");
    }).catch(function (error) {
    })
}
function isNull(v){
    if (typeof(v) == "undefined" || v == "" || v == null) {
        return true;
    }
    return false;
};
//# sourceURL=auto.js
