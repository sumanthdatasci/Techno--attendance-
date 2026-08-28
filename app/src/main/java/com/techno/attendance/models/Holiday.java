package com.techno.attendance.models;

import java.io.Serializable;

public class Holiday implements Serializable {
    private long date;
    private String reason;

    public Holiday(long date, String reason) {
        this.date = date;
        this.reason = reason;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
