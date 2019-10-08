package com.demo.compass.data.model;

public class OutletDownloadEntity {
    private int outletId;
    private int projectId;

    public OutletDownloadEntity(int outletId, int projectId) {
        this.outletId = outletId;
        this.projectId = projectId;
    }

    public int getOutletId() {
        return outletId;
    }

    public int getProjectId() {
        return projectId;
    }
}
