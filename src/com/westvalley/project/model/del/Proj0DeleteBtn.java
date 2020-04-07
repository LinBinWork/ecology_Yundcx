package com.westvalley.project.model.del;

import weaver.conn.RecordSet;
import weaver.formmode.interfaces.PopedomCommonAction;

/**
 * 祖项维护自定义删除
 */
public class Proj0DeleteBtn implements PopedomCommonAction {

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
		//如果已配置预算成本中心则不允许删除
		rs.executeQuery("select p.projNo from uf_projcost p join uf_OrgProject b on p.projNo = b.orgCode where b.id = ?",billid);
		if(rs.next()){
			return "false";
		}else{
			return "true";
		}
	}
}