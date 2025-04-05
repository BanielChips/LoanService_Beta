package com.cen4910c.ipaccessproject;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "password")
    private String password;

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public User() {}
    public User(String firstName, String lastName, String zipCode, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [" +
                "userID: " + userID +
                ", firstName: " + firstName +
                ", lastName: " + lastName +
                ", email: " + email +
                ", phoneNumber: " + phoneNumber +
                ", zipCode: " + zipCode +
                ", createdAt: " + createdAt +
                "]";
    }

}

