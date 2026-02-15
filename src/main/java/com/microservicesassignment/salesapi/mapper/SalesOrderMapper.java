package com.microservicesassignment.salesapi.mapper;

import com.microservicesassignment.salesapi.dto.SalesOrderResponse;
import com.microservicesassignment.salesapi.entity.SalesOrder;
import org.springframework.stereotype.Component;

@Component
public class SalesOrderMapper {

    public SalesOrderResponse toResponse(SalesOrder order) {
        SalesOrderResponse response = new SalesOrderResponse();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        return response;
    }
}
