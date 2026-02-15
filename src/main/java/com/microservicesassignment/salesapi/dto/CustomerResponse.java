package com.microservicesassignment.salesapi.dto;

import java.time.LocalDate;

public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private long totalOrders;
    private LocalDate createdAt;
    private CustomerProfileResponse profile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public CustomerProfileResponse getProfile() {
        return profile;
    }

    public void setProfile(CustomerProfileResponse profile) {
        this.profile = profile;
    }
}
