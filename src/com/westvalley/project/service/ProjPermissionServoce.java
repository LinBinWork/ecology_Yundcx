package com.westvalley.project.service;

import com.westvalley.project.dao.PermissionDao;
import com.westvalley.project.dto.PermissionDto;
import com.westvalley.project.dto.ResultDto;

import java.util.List;

/**
 * 项目权限
 */
public class ProjPermissionServoce {

    /**
     * 保存项目拆解、选择权限
     * @param permissionDtoList
     * @return
     */
    public ResultDto savePermission(List<PermissionDto> permissionDtoList){
        PermissionDao dao = new PermissionDao();
        return dao.savePermission(permissionDtoList);
    }
}
