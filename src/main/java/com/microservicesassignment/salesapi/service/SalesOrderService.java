package com.microservicesassignment.salesapi.service;

import com.microservicesassignment.salesapi.dto.CreateSalesOrderRequest;
import com.microservicesassignment.salesapi.dto.SalesOrderResponse;
import com.microservicesassignment.salesapi.dto.UpdateSalesOrderRequest;
import com.microservicesassignment.salesapi.entity.Customer;
import com.microservicesassignment.salesapi.entity.OrderStatus;
import com.microservicesassignment.salesapi.entity.SalesOrder;
import com.microservicesassignment.salesapi.exception.InvalidRequestException;
import com.microservicesassignment.salesapi.exception.ResourceNotFoundException;
import com.microservicesassignment.salesapi.mapper.SalesOrderMapper;
import com.microservicesassignment.salesapi.repository.SalesOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerService customerService;
    private final SalesOrderMapper salesOrderMapper;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             CustomerService customerService,
                             SalesOrderMapper salesOrderMapper) {
        this.salesOrderRepository = salesOrderRepository;
        this.customerService = customerService;
        this.salesOrderMapper = salesOrderMapper;
    }

    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> getOrdersByCustomer(Long customerId,
                                                        LocalDate fromDate,
                                                        LocalDate toDate,
                                                        OrderStatus status,
                                                        BigDecimal minAmount,
                                                        BigDecimal maxAmount,
                                                        Pageable pageable) {
        customerService.findCustomer(customerId);
        validateDateRange(fromDate, toDate);
        validateAmountRange(minAmount, maxAmount);

        Specification<SalesOrder> specification = buildOrderSpecification(
            customerId,
            fromDate,
            toDate,
            status,
            minAmount,
            maxAmount
        );

        Page<SalesOrder> orders = salesOrderRepository.findAll(specification, pageable);
        return orders.map(salesOrderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SalesOrderResponse getOrderById(Long customerId, Long orderId) {
        return salesOrderMapper.toResponse(findOrder(customerId, orderId));
    }

    public SalesOrderResponse createOrder(Long customerId, CreateSalesOrderRequest request) {
        Customer customer = customerService.findCustomer(customerId);

        SalesOrder order = new SalesOrder();
        order.setCustomer(customer);
        order.setOrderDate(request.getOrderDate());
        order.setStatus(request.getStatus());
        order.setTotalAmount(request.getTotalAmount());

        SalesOrder saved = salesOrderRepository.save(order);
        return salesOrderMapper.toResponse(saved);
    }

    public SalesOrderResponse updateOrder(Long customerId, Long orderId, UpdateSalesOrderRequest request) {
        SalesOrder order = findOrder(customerId, orderId);

        order.setOrderDate(request.getOrderDate());
        order.setStatus(request.getStatus());
        order.setTotalAmount(request.getTotalAmount());

        SalesOrder updated = salesOrderRepository.save(order);
        return salesOrderMapper.toResponse(updated);
    }

    public void deleteOrder(Long customerId, Long orderId) {
        SalesOrder order = findOrder(customerId, orderId);
        salesOrderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public SalesOrder findOrder(Long customerId, Long orderId) {
        return salesOrderRepository.findByIdAndCustomerId(orderId, customerId)
            .orElseThrow(() -> new ResourceNotFoundException("order not found for customerId=" + customerId + ", orderId=" + orderId));
    }

    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new InvalidRequestException("from-date must be less than or equal to to-date");
        }
    }

    private void validateAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount != null && minAmount.signum() < 0) {
            throw new InvalidRequestException("min-amount must be zero or positive");
        }

        if (maxAmount != null && maxAmount.signum() < 0) {
            throw new InvalidRequestException("max-amount must be zero or positive");
        }

        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new InvalidRequestException("min-amount must be less than or equal to max-amount");
        }
    }

    private Specification<SalesOrder> buildOrderSpecification(Long customerId,
                                                              LocalDate fromDate,
                                                              LocalDate toDate,
                                                              OrderStatus status,
                                                              BigDecimal minAmount,
                                                              BigDecimal maxAmount) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("customer").get("id"), customerId));

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), toDate));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), minAmount));
            }

            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), maxAmount));
            }

            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
