package com.westvalley.project.model.del;

import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.service.BudgetService;
import weaver.conn.RecordSet;
import weaver.formmode.interfaces.PopedomCommonAction;
import weaver.general.Util;

/**
 * 祖项维护成本中心定义删除
 */
public class ProjcostDeleteBtn implements PopedomCommonAction {

	/**
	 * 说明
	 * 修改时
	 * 类名要与文件名保持一致
	 * class文件存放位置与路径保持一致。
	 * 请把编译后的class文件，放在对应的目录中才能生效
	 * 注意 同一路径下java代码名不能相同。
	 *
	 * 得到是否显示操作项
	 * @param modeid 模块id
	 * @param customid 查询列表id
	 * @param uid 当前用户id
	 * @param billid 表单数据id
	 * @param buttonname 按钮名称
	 * @retrun "true"或者"false"true显示/false不显示
	 */
	@Override
	public String getIsDisplayOperation(String modeid, String customid,String uid, String billid, String buttonname) {

		RecordSet rs =new RecordSet();
		//如果以导入预算数据，不允许删除
		rs.executeQuery(" select p.projAmt,p.projYear,p.projNo,p.id,b.isVirtual from uf_proj p join uf_projcost b " +
						" on p.projNo = b.projNo " +
						" and p.projDeptNo = b.costNo " +
//						" and p.businessType = b.businessType " +
						" where b.id = ?",
				billid);
		if(rs.next()){
			//判断是否虚拟祖项，如果是虚拟祖项，判断是否存在预算
			int isVirtual = Util.getIntValue(rs.getString("isVirtual"), -1);
			if(isVirtual == 0) {
				//判断是否存在预算使用记录
				rs.executeQuery("select a.id from wv_proj_excuDetail a join workflow_requestbase b on a.requestid = b.requestid and b.currentnodetype != 0 where a.projID =? and a.projtype =0",rs.getString("id"));
				if(rs.next()){
					//存在使用预算，不允许删除
					return "false";
				}else{
					return "true";
				}
			}
			return "false";
		}else{
			return "true";
		}
	}
}