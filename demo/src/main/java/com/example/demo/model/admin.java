package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class admin {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10)
    private int adminId;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Column(name = "email", length = 50)
    private String adminEmail;

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    @Column(name = "name", length = 50)
    private String adminName;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Column(name = "password", length = 50)
    private String adminPassword;

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Column(name = "address", length = 255)
    private String adminAddress;

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    @Column(name = "city", length = 50)
    private String adminCity;

    public String getAdminCity() {
        return adminCity;
    }

    public void setAdminCity(String adminCity) {
        this.adminCity = adminCity;
    }

    @Column(name = "province", length = 50)
    private String adminProvince;

    public String getAdminProvince() {
        return adminProvince;
    }

    public void setAdminProvince(String adminProvince) {
        this.adminProvince = adminProvince;
    }

    @Column(name = "phone", length = 50)
    private String adminPhone;

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    @Column(name = "status", length = 50)
    private String adminStatus;

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

}