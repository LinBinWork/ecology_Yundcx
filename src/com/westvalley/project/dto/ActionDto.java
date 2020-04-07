package com.westvalley.project.dto;

import weaver.soa.workflow.request.RequestInfo;


/**
 * 流程基本信息
 * @author ys ou
 * @Despration
 */
public class ActionDto {

	
	private String title;
	private String target;
	private String requestID;
	private String workflowID;
	private int nodeID;
	private ResultDto resultDto;
	
	
	/**流程目标[<b>动作参数</b>] */
	private static final String Target_Submit = "submit";
	private static final String Target_Reject = "reject";
	
	private ActionDto(){}
	
	/**
	 * 初始化操作
	 * @param info
	 * @return
	 */
	public static ActionDto init(RequestInfo info){
		ActionDto dto = new ActionDto();
		dto.setTitle(info.getDescription()+"");
		dto.setRequestID(info.getRequestid()+"");
		dto.setWorkflowID(info.getWorkflowid()+"");
		dto.setNodeID(info.getRequestManager().getNodeid());
		dto.setTarget(info.getRequestManager().getSrc());
		return dto;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getWorkflowID() {
		return workflowID;
	}
	public void setWorkflowID(String workflowID) {
		this.workflowID = workflowID;
	}
	public int getNodeID() {
		return nodeID;
	}
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public String getTarget() {
		if(target == null || "".equals(target)){
			return "非提交或退回操作！";
		}
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public ResultDto getResultDto() {
		return resultDto;
	}
	public void setResultDto(ResultDto resultDto) {
		this.resultDto = resultDto;
	}

	public String getResult(){
		return this.resultDto == null || this.resultDto.isOk() ? "SUCCESS" : "ERROR";
	}
	
	/**判断流程流向*/
	public final boolean isSubmit(){return Target_Submit.equals(target);}
	public final boolean isReject(){return Target_Reject.equals(target);}
	
	public String begin() {
		return String.format(" --> Action开始 ## RequestID：%s , 标题：%s , Src:%s , WorkflowID：%s , nodeID：%s ", 
				requestID,
				title,
				getTarget(),
				workflowID,
				nodeID
			);
	}
	
	public String end() {
		return String.format(" --> Action终止 ## RequestID：%s , 标题：%s , 结果：%s ",
				requestID,
				title,
				resultDto
			);
	}
	
}
