package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table (name = "admin")
public class admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private int AdminId;

    @Column(name = "email", length = 50, nullable = false)
    private String AdminEmail;

    @Column(name = "name", length = 50, nullable = false)
    private String AdminName;

    @Column(name = "password", length = 50, nullable = false)
    private String AdminPassword;

    @Column(name = "address", length = 255, nullable = false)
    private String AdminAddress;

    @Column(name = "city", length = 50, nullable = false)
    private String AdminCity;

    @Column(name = "province", length = 50, nullable = false)
    private String AdminProvince;

    @Column(name = "phone", length = 50, nullable = false)
    private String AdminPhone;

    @Column(name = "status", length = 50, nullable = false)
    private String AdminStatus;

    public int getAdminId() {
        return AdminId;
    }

    public void setAdminId(int AdminId) {
        AdminId = AdminId;
    }

    public String getAdminEmail() {
        return AdminEmail;
    }

    public void setAdminEmail(String AdminEmail) {
        AdminEmail = AdminEmail;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String AdminName) {
        AdminName = AdminName;
    }

    public String getAdminPassword() {
        return AdminPassword;
    }

    public void setAdminPassword(String AdminPassword) {
        AdminPassword = AdminPassword;
    }

    public String getAdminAddress() {
        return AdminAddress;
    }

    public void setAdminAddress(String AdminAddress) {
        AdminAddress = AdminAddress;
    }

    public String getAdminCity() {
        return AdminCity;
    }

    public void setAdminCity(String AdminCity) {
        AdminCity = AdminCity;
    }

    public String getAdminProvince() {
        return AdminProvince;
    }

    public void setAdminProvince(String AdminProvince) {
        AdminProvince = AdminProvince;
    }

    public String getAdminPhone() {
        return AdminPhone;
    }

    public void setAdminPhone(String AdminPhone) {
        AdminPhone = AdminPhone;
    }

    public String getAdminStatus() {
        return AdminStatus;
    }

    public void setAdminStatus(String AdminStatus) {
        AdminStatus = AdminStatus;
    }

}
