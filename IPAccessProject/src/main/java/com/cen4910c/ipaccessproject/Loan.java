package com.cen4910c.ipaccessproject;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "loaninformation")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanID;

    @Column(name = "userID")
    private int userID;

    @Column(name = "deviceId")
    private int deviceID;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    public enum LoanStatus {
        ACTIVE, OVERDUE, RESERVED, REVIEW
    }

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String startDate) {
        try {
            this.startDate = LocalDate.parse(startDate);
        } catch (Exception e) {
            this.startDate = LocalDate.now();
        }
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String endDate) {
        try {
            this.endDate = LocalDate.parse(endDate);
        } catch (Exception e) {
            this.endDate = LocalDate.now();
        }
    }

    public String getLoanStatus() {
        return loanStatus.toString();
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public void setLoanStatus(String status) {
        this.loanStatus = LoanStatus.valueOf(status.toUpperCase());
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Loan() {
    }

    ;

    public Loan(int userID, int deviceID, LocalDate startDate, LocalDate endDate, String loanStatus) {
        this.userID = userID;
        this.deviceID = deviceID;
        this.startDate = startDate;
        this.endDate = endDate;
        setLoanStatus(loanStatus);
    }

    public Loan(int userID, int deviceID, String startDate, String endDate, String loanStatus) {
        setUserID(userID);
        setDeviceID(deviceID);
        setStartDate(startDate);
        setEndDate(endDate);
        setLoanStatus(loanStatus);
    }

    // I noticed the database was not automatically assigning a created_at date because JPA was overriding it
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

    @Override
    public String toString() {
        return "LoanInformation [" +
                "getLoanID=" + loanID +
                ", userID=" + userID +
                ", getDeviceID=" + deviceID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", LoanStatus=" + loanStatus +
                ", created: " + createdAt +
                "]";
    }
}
