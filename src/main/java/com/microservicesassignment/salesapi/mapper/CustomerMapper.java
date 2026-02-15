package com.microservicesassignment.salesapi.mapper;

import com.microservicesassignment.salesapi.dto.CustomerProfileResponse;
import com.microservicesassignment.salesapi.dto.CustomerResponse;
import com.microservicesassignment.salesapi.entity.CustomerProfile;
import com.microservicesassignment.salesapi.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setCreatedAt(customer.getCreatedAt());
        response.setTotalOrders(customer.getOrders() == null ? 0 : customer.getOrders().size());
        response.setProfile(toProfileResponse(customer.getProfile()));
        return response;
    }

    private CustomerProfileResponse toProfileResponse(CustomerProfile profile) {
        if (profile == null) {
            return null;
        }

        CustomerProfileResponse profileResponse = new CustomerProfileResponse();
        profileResponse.setPhoneNumber(profile.getPhoneNumber());
        profileResponse.setBillingAddress(profile.getBillingAddress());
        profileResponse.setShippingAddress(profile.getShippingAddress());
        return profileResponse;
    }
}
