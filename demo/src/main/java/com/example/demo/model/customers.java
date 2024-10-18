package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class customers {

    @Id
    @Column(name = "id", length = 10, nullable = false)
    private String CustomerId;

    @Column(name = "email", length = 50, nullable = false)
    private String CustomerEmail;

    @Column(name = "name", length = 50, nullable = false)
    private String CustomerName;

    @Column(name = "password", length = 50, nullable = false)
    private String CustomerPassword;

    @Column(name = "address", length = 255, nullable = false)
    private String CustomerAddress;

    @Column(name = "city", length = 50, nullable = false)
    private String CustomerCity;

    @Column(name = "province", length = 50, nullable = false)
    private String CustomerProvince;

    @Column(name = "phone", length = 50, nullable = false)
    private String CustomerPhone;

    @Column(name = "status", length = 50, nullable = false)
    private String CustomerStatus;

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerPassword() {
        return CustomerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        CustomerPassword = customerPassword;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getCustomerCity() {
        return CustomerCity;
    }

    public void setCustomerCity(String customerCity) {
        CustomerCity = customerCity;
    }

    public String getCustomerProvince() {
        return CustomerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        CustomerProvince = customerProvince;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public String getCustomerStatus() {
        return CustomerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        CustomerStatus = customerStatus;
    }

}
