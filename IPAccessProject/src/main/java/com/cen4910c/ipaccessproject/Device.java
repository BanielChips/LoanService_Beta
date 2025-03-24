package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deviceID;

    @Enumerated(EnumType.STRING)
    @Column(name = "deviceType", nullable = false)
    private ApplicationEnums.DeviceType deviceType;

    @Column(name = "availability")
    private boolean availability;

    @Column(name = "renterID")
    private Integer renterID;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "deviceStatus", nullable = false)
    private ApplicationEnums.DeviceStatus deviceStatus = ApplicationEnums.DeviceStatus.ACTIVE;

    public int getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public ApplicationEnums.DeviceType getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(ApplicationEnums.DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isAvailable() {
        return availability;
    }
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Integer getRenterID() {
        return renterID;
    }
    public void setRenterID(Integer renterID) {
        this.renterID = renterID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp created_at) {
        this.createdAt = created_at;
    }

    public ApplicationEnums.DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }
    public void setDeviceStatus(ApplicationEnums.DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Device(){}
    public Device(ApplicationEnums.DeviceType deviceType, boolean availability, int renterID) {
        this.deviceType = deviceType;
        this.availability = availability;
        this.renterID = renterID;
    }

    @Override
    public String toString() {
        return "Device: [" +
                "deviceID: " + deviceID +
                ", deviceType: " + deviceType +
                ", available: " + availability +
                ", renterID: " + renterID +
                ", created_at: " + createdAt +
                ", deviceStatus: " + deviceStatus +
                "]";
    }

}
