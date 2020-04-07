<%@page import="weaver.hrm.HrmUserVarify" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.general.*" %>
<%@ include file="/hrm/header.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page"/>
<%
    //权限
/* if (!HrmUserVarify.checkUserRight("WV_ATTQX:See" ,user) ) {
		response.sendRedirect("/notice/noright.jsp") ;
		return ;
	} */
    String perpage = "10";
    String imagefilename = "/images/hdReport_wev8.gif";
    String titlename = "数据同步";
    String needfav = "1";
    String needhelp = "";
%>
<HTML>
<HEAD>
    <LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
    <script language="javascript" src="/js/weaver_wev8.js"></script>
    <SCRIPT language="javascript" defer="defer" src="/js/datetime_wev8.js"></script>
    <SCRIPT language="javascript" defer="defer" src="/js/JSDateTime/WdatePicker_wev8.js"></script>

    <script language=javascript>
        //全部统计
        function allSyn() {
            var synName = jQuery("#dataselect  option:selected").val();
            if (synName == '') {
                window.top.Dialog.alert("请选择需要同步的数据");
                return;
            }
            var url = "/westvalley/synData/OperationOfOA.jsp?synName=" + synName;
            hideB();
            jQuery.ajax({
                type: "POST",
                url: url,
                dataType: "json",
                success: function (data) {
                    if (data.isOK) {
                        showB(data.msg);
                    } else {
                        showB("同步失败，请联系管理员！");
                    }
                },
                error: function (msg) {
                    showB("同步异常，请联系管理员！");
                }
            });

        }

        function hideB() {
            jQuery("input[type='button']").hide();
            jQuery("#rightMenuIframe").hide();
            jQuery("img[id^=synNo]").show();
            jQuery("span[id^='synNoMsg']").html("");
        }

        function showB(msg) {
            jQuery("span[id^='synNoMsg']").html(msg);
            jQuery("img[id^=synNo]").hide();
            jQuery("input[type='button']").show();
            jQuery("#rightMenuIframe").show();
        }

        function reset() {
            jQuery("#synName").val("");
            jQuery("#synNamespan").html("");
        }
    </script>
</head>
<BODY>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
    RCMenu += "{手工同步,javascript:allSyn(),_self} ";
    RCMenuHeight += RCMenuHeightStep;

    RCMenu += "{重置,javascript:reset(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<jsp:include page="/systeminfo/commonTabHead.jsp">
    <jsp:param name="mouldID" value="assest"/>
    <jsp:param name="navName" value="<%=titlename %>"/>
</jsp:include>
<table id="topTitle" cellpadding="0" cellspacing="0">
    <tr>
        <td>
        </td>
        <td class="rightSearchSpan" style="text-align:right;">
            <input type="button" value="手工同步" class="e8_btn_top" onclick="allSyn()"/>
            <span title="<%=SystemEnv.getHtmlLabelName(23036,user.getLanguage())%>" class="cornerMenu"></span>
        </td>
    </tr>
</table>
<div>
    <form id="frmmain" name=frmmain method=post action="">
        <wea:layout type="2col" attributes="{expandAllGroup:true}">
            <wea:group context="主数据数据手工同步">
                <wea:item>请选择需要同步的数据</wea:item>
                <%--<wea:item>
                    <brow:browser viewType="0" id="synName" name="synName" browserValue=""
                                  browserUrl="/systeminfo/BrowserMain.jsp?url=/interface/CommonBrowser.jsp?type=browser.gongsdmsap "
                                  hasInput="true" isSingle="true" hasBrowser="true" isMustInput='2'
                                  completeUrl="/data.jsp?type=161&f_weaver_belongto_userid=&f_weaver_belongto_usertype=null&fielddbtype=browser.timepart&f_weaver_belongto_userid=&f_weaver_belongto_usertype=null&whereClause="
                                  width="200px"
                                  browserSpanValue="">
                    </brow:browser>
                </wea:item>--%>
                <wea:item>
                    <select id="dataselect" class=inputstyle style="width:120px;">
                        <option value="">请选择...</option>
                        <option value="gsdm">公司代码</option>
                        <option value="km">科目</option>
                        <option value="cbzx">成本中心</option>
                        <option value="gysbm">供应商</option>
                    </select>
                </wea:item>
                <wea:item></wea:item>
                <wea:item>
                    <div>
                        <input id="synNo" type="button" value="手工同步" class="e8_btn_top" onclick="allSyn()"/>
                        <span id="synNoMsg"></span>
                        <img id="synNo" src="loading.gif" style="display:none" title="同步数据中 ···"/>
                    </div>
                </wea:item>
            </wea:group>
        </wea:layout>
    </form>
</div>
</body>
</HTML>