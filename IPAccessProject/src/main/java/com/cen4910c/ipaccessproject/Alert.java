package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {
    /**
     * Represents an alert in the administrator alert system.
     * Alerts can have different types, priorities and expirations.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alertID;

    @Column(name = "alertName")
    private String alertName;

    @Column(name = "alertBody")
    private String alertBody;

    @Column(name = "timestamp", insertable = false, updatable = false)
    private LocalDateTime timestamp;

    public enum AlertType {
        NEW_OVERDUE_DEVICE,
        ANNOUNCEMENT,
        DEVICE_REPAIR_STATUS_UPDATE,
        NEW_DEVICE_REQUEST,
        DEVICE_RETURNED,
        LOAN_APPROVAL_REQUIRED,
        SYSTEM_MAINTENANCE,
        URGENT_ACTION_REQUIRED,
        NOTIFICATION
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "alertType")
    private AlertType alertType;

    public enum AlertPriority {
        HIGH, MEDIUM, LOW
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "alertPriority")
    private AlertPriority alertPriority;

    @Column(name = "isRead")
    private boolean isRead;

    @Column(name = "expiryDate")
    private LocalDateTime expiryDate;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertBody() {
        return alertBody;
    }

    public void setAlertBody(String alertBody) {
        this.alertBody = alertBody;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void setAlertType(String alertType) {
        try {
            this.alertType = AlertType.valueOf(alertType);
        } catch (IllegalArgumentException e) {
            this.alertType = AlertType.SYSTEM_MAINTENANCE;
        }
    }

    public AlertPriority getAlertPriority() {
        return alertPriority;
    }

    public void setAlertPriority(AlertPriority alertPriority) {
        this.alertPriority = alertPriority;
    }

    public void setAlertPriority(String alertPriority) {
        try {
            this.alertPriority = AlertPriority.valueOf(alertPriority);
        } catch (IllegalArgumentException e) {
            this.alertPriority = AlertPriority.HIGH;
        }
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Alert() {
    }

    public Alert(String alertName, String alertBody, AlertType alertType,
                 AlertPriority alertPriority, LocalDateTime expiryDate) {
        this.alertName = alertName;
        this.alertBody = alertBody;
        this.timestamp = LocalDateTime.now();
        this.alertType = alertType;
        this.alertPriority = alertPriority;
        this.isRead = false;
        this.expiryDate = expiryDate;
    }


}
