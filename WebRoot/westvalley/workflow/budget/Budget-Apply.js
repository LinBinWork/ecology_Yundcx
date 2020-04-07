/***
 * 引用流程：年度项目预算申请
 * 引用节点：申请节点
 * 主要功能：
 */
var t0 = {};
var t1 = {};
jQuery(document).ready(function () {
    /***获取流程表单字段field**/
    jQuery.ajax({
        type: "POST",
        url: "/westvalley/workflow/getfieldid.jsp",
        data: 'wid=' + jQuery("input[name=workflowid]").val(),
        dataType: "json",
        async: false,
        success: function (data) {
            t0 = data.mid;
            t1 = data.did1;
        }
    });

    /*操作类型
    WfForm.OPER_SAVE 保存
    WfForm.OPER_SUBMIT 提交/批准/提交需反馈/不需反馈等
    WfForm.OPER_REJECT 退回
    WfForm.OPER_REMARK 批注提交
    WfForm.OPER_INTERVENE 干预
    WfForm.OPER_FORWARD 转发
    WfForm.OPER_TAKEBACK 强制收回
    WfForm.OPER_DELETE 删除*/

    /*WfForm.registerCheckEvent(WfForm.OPER_SAVE, function(callback){
        jQuery("#field27495").val("保存自动赋值");
        callback(); //继续提交需调用callback，不调用代表阻断
    });*/

    var fileId = WfForm.convertFieldNameToId("gonghao","main",true);// 1:字段名称 2:表单标示，主表(main)/具体明细表(detail_1),默认为main 3：返回值是否需要 field 字符串前缀，默认为true
    // console.log(fileId);
    var fieldvalue = WfForm.getFieldValue(t0.gonghao);//获取单个的值 字段标示，格式 field${字段ID}_${明细行号}
    // console.log("fieldvalue === ",fieldvalue);
    WfForm.changeSingleField("field110", {value:"修改的值"}, {viewAttr:"1"});//修改值同时置为只读

    WfForm.changeFieldValue("field123", {value:"1.234"}); //修改文本框、多行文本、选择框等
    WfForm.changeFieldValue("field11_2", {
        value: "2,3",
        specialobj:[
            {id:"2",name:"张三"},
            {id:"3",name:"李四"}
        ]
    });
//注意：修改浏览框字段的值，必须有specialobj数组结构对象，其它批量修改、添加明细行赋初始值等接口同理
    WfForm.changeFieldValue("field123", {value:"1"}); //修改check框字段(0不勾选、1勾选)
    WfForm.changeFieldValue("field123", {
        value: "入库真实值",
        specialobj: {
            showhtml: "界面显示值"
        }
    }); //入库真实值与界面显示值不一致场景，只支持文本类型字段且为只读情况


    WfForm.changeFieldAttr("field110", 1); //改变字段的状态，1：只读，2：可编辑，3：必填，4：隐藏字段标签及内容
});

