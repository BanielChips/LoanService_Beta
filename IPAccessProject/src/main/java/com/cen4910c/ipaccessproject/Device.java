package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deviceID;

    public enum DeviceType {
        LAPTOP, TABLET, PHONE, HOTSPOT
    }

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    public enum DeviceStatus {
        ACTIVE, INACTIVE, LOANED, DAMAGED
    }

    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;

    @Column(name = "availability")
    private boolean availability;

    @Column(name = "renterID")
    private Integer renterID;

    @ManyToOne
    @JoinColumn(name = "locationID", referencedColumnName = "locationID")
    private Location location;


    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    public Device() {
    }

    public Device(String deviceType, boolean availability) {
        setDeviceType(deviceType);
        setAvailability(availability);
        setDeviceStatus(DeviceStatus.ACTIVE);
    }

    public Device(String deviceType, boolean availability, int renterID) {
        setDeviceType(deviceType);
        setAvailability(availability);
        setRenterID(renterID);
        setDeviceStatus(DeviceStatus.ACTIVE);
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceName) {
        try {
            this.deviceType = DeviceType.valueOf(deviceName.toUpperCase());
        } catch (Exception e) {
            this.deviceType = DeviceType.LAPTOP;
        }
    }


    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        try {
            this.deviceStatus = DeviceStatus.valueOf(deviceStatus);
        } catch (Exception e) {
            this.deviceStatus = DeviceStatus.INACTIVE;
        }
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Device [" +
                "getDeviceID: " + deviceID +
                ", deviceName: " + deviceType +
                ", available: " + availability +
                ", created_at: " + createdAt +
                "]";
    }

}
