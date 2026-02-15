package com.microservicesassignment.salesapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservicesassignment.salesapi.entity.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateSalesOrderRequest {

    @NotNull(message = "orderDate is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotNull(message = "status is required")
    private OrderStatus status;

    @NotNull(message = "totalAmount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "totalAmount must be zero or positive")
    private BigDecimal totalAmount;

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
