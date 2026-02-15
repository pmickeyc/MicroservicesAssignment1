package com.microservicesassignment.salesapi.service;

import com.microservicesassignment.salesapi.dto.CreateCustomerRequest;
import com.microservicesassignment.salesapi.dto.CustomerResponse;
import com.microservicesassignment.salesapi.dto.UpdateCustomerRequest;
import com.microservicesassignment.salesapi.entity.Customer;
import com.microservicesassignment.salesapi.entity.CustomerProfile;
import com.microservicesassignment.salesapi.exception.ConflictException;
import com.microservicesassignment.salesapi.exception.ResourceNotFoundException;
import com.microservicesassignment.salesapi.mapper.CustomerMapper;
import com.microservicesassignment.salesapi.repository.CustomerRepository;
import com.microservicesassignment.salesapi.repository.SalesOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository,
                           SalesOrderRepository salesOrderRepository,
                           CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
            .map(customerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = findCustomer(customerId);
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String normalizedEmail = normalizeEmail(request.getEmail());
        if (customerRepository.existsByEmail(normalizedEmail)) {
            throw new ConflictException("email already exists");
        }

        Customer customer = new Customer();
        customer.setName(normalizeRequiredText(request.getName()));
        customer.setEmail(normalizedEmail);
        customer.setCreatedAt(LocalDate.now());
        customer.setProfile(createProfileIfSupplied(
            request.getPhoneNumber(),
            request.getBillingAddress(),
            request.getShippingAddress()
        ));

        Customer saved = customerRepository.save(customer);
        return customerMapper.toResponse(saved);
    }

    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = findCustomer(customerId);

        String normalizedEmail = normalizeEmail(request.getEmail());
        if (customerRepository.existsByEmailAndIdNot(normalizedEmail, customerId)) {
            throw new ConflictException("email already exists");
        }

        customer.setName(normalizeRequiredText(request.getName()));
        customer.setEmail(normalizedEmail);
        applyProfileUpdates(
            customer,
            request.getPhoneNumber(),
            request.getBillingAddress(),
            request.getShippingAddress()
        );

        Customer updated = customerRepository.save(customer);
        return customerMapper.toResponse(updated);
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = findCustomer(customerId);

        if (salesOrderRepository.existsByCustomerId(customerId)) {
            throw new ConflictException("cannot delete customer with existing orders");
        }

        customerRepository.delete(customer);
    }

    @Transactional(readOnly = true)
    public Customer findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("customer not found: " + customerId));
    }

    private void applyProfileUpdates(Customer customer,
                                     String phoneNumber,
                                     String billingAddress,
                                     String shippingAddress) {
        if (phoneNumber == null && billingAddress == null && shippingAddress == null) {
            return;
        }

        CustomerProfile profile = customer.getProfile();
        if (profile == null) {
            profile = new CustomerProfile();
            customer.setProfile(profile);
        }

        profile.setPhoneNumber(normalizeToNull(phoneNumber));
        profile.setBillingAddress(normalizeToNull(billingAddress));
        profile.setShippingAddress(normalizeToNull(shippingAddress));
    }

    private CustomerProfile createProfileIfSupplied(String phoneNumber,
                                                    String billingAddress,
                                                    String shippingAddress) {
        if (phoneNumber == null && billingAddress == null && shippingAddress == null) {
            return null;
        }

        CustomerProfile profile = new CustomerProfile();
        profile.setPhoneNumber(normalizeToNull(phoneNumber));
        profile.setBillingAddress(normalizeToNull(billingAddress));
        profile.setShippingAddress(normalizeToNull(shippingAddress));
        return profile;
    }

    private String normalizeEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    private String normalizeRequiredText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeToNull(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
