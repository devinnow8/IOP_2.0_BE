package com.nis.model;

import lombok.Data;

@Data
public class DashboardResponse {

    public int total;
    public int attended;
    public int rescheduled;
    public int cancelled;
    public int pending;


}
