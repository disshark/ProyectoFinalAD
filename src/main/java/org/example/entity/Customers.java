package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
