package com.dova.apimaster.common.domain;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class UnitGroup{
    private int id;
    private int projectId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
