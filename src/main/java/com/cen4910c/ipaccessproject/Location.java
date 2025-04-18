package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationID;

    @Column(nullable = false)
    private String locationName;

    private String address;
    private String city;
    private String state;
    private String zipcode;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp createdAt;

    public Location() {
    }

    public Location(int deviceID, String locationName, String address, String city, String state, String zip) {
        this.locationName = locationName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zip;
        this.createdAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public int getLocationID() {
        return locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZip(String zipcode) {
        this.zipcode = zipcode;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

}
