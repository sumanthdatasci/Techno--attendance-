package com.techno.attendance.models;

import java.io.Serializable;

public class AttendanceRecord implements Serializable {
    private String memberId;
    private long date;
    private String status; // PRESENT, ABSENT, LEAVE

    public AttendanceRecord(String memberId, long date, String status) {
        this.memberId = memberId;
        this.date = date;
        this.status = status;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
