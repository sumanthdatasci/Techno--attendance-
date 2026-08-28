package com.techno.attendance.models;

import java.io.Serializable;

public class Member implements Serializable {
    private String memberId;
    private String fullName;
    private String mobileNumber;
    private long joiningDate;
    private long leavingDate;
    private boolean active;

    public Member(String memberId, String fullName, String mobileNumber, long joiningDate) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.joiningDate = joiningDate;
        this.leavingDate = 0;
        this.active = true;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public long getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(long joiningDate) {
        this.joiningDate = joiningDate;
    }

    public long getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(long leavingDate) {
        this.leavingDate = leavingDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
