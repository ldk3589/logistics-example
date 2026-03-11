package com.dk.logistics.framework.datascope;

import java.util.ArrayList;
import java.util.List;

public class DataScopeContext {
    private String dataScope;
    private Long userId;
    private Long deptId;
    private List<Long> deptIds = new ArrayList<>();

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }
}