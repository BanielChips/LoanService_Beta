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

    @Column(name = "password")
    private String password;

    public enum Role {
        USER, ADMIN
    }
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;



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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(String role) {
        this.role = Role.valueOf(role.toUpperCase());
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

    public User() {}
    public User(String firstName, String lastName, String zipCode, String email, String password, String phoneNumber) {
        setFirstName(firstName);
        setLastName(lastName);
        setZipCode(zipCode);
        setEmail(email);
        setPassword(password);
        setRole("USER");
        setPhoneNumber(phoneNumber);
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

