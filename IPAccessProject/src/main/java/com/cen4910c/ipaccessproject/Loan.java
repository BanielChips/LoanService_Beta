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

    @Column(name = "loanStatus")
    private String loanStatus;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public int loanID() {
        return loanID;
    }
    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public int userID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int deviceID() {
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

    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }
    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Loan(){};
    public Loan(int userID, int deviceID, LocalDate startDate, LocalDate endDate, String loanStatus) {
        this.userID = userID;
        this.deviceID = deviceID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loanStatus = loanStatus;
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
                "loanID=" + loanID +
                ", userID=" + userID +
                ", deviceID=" + deviceID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", loanStatus=" + loanStatus +
                ", created: " + createdAt +
                "]";
    }
}
