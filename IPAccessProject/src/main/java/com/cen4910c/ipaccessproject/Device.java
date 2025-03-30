package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deviceID;

    @Column(name = "deviceName")
    private String deviceName;

    @Column(name = "availability")
    private boolean availability;

    @Column(name = "renterID")
    private Integer renterID;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public Device(){}

    public Device(String deviceName, boolean availability) {
        setDeviceName(deviceName);
        setAvailability(availability);
    }

    public Device(String deviceName, boolean availability, int renterID) {
        setDeviceName(deviceName);
        setAvailability(availability);
        setRenterID(renterID);
    }

    public int getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    @Override
    public String toString() {
        return "Device [" +
                "getDeviceID: " + deviceID +
                ", deviceName: " + deviceName +
                ", available: " + availability +
                ", renterID: " + renterID +
                ", created_at: " + createdAt +
                "]";
    }

}
