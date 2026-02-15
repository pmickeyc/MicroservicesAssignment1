package com.microservicesassignment.salesapi.controller;

import com.microservicesassignment.salesapi.dto.CreateSalesOrderRequest;
import com.microservicesassignment.salesapi.dto.PagedResponse;
import com.microservicesassignment.salesapi.dto.SalesOrderResponse;
import com.microservicesassignment.salesapi.dto.UpdateSalesOrderRequest;
import com.microservicesassignment.salesapi.entity.OrderStatus;
import com.microservicesassignment.salesapi.service.SalesOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales/v1/customers/{customerId}/orders")
@Validated
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<SalesOrderResponse>> getOrdersByCustomer(
        @PathVariable @Positive Long customerId,
        @RequestParam(name = "from-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(name = "to-date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        @RequestParam(name = "status", required = false) OrderStatus status,
        @RequestParam(name = "min-amount", required = false) BigDecimal minAmount,
        @RequestParam(name = "max-amount", required = false) BigDecimal maxAmount,
        @PageableDefault(size = 20, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SalesOrderResponse> page = salesOrderService.getOrdersByCustomer(
            customerId,
            fromDate,
            toDate,
            status,
            minAmount,
            maxAmount,
            pageable
        );
        return ResponseEntity.ok(PagedResponse.fromPage(page));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<SalesOrderResponse> getOrder(@PathVariable @Positive Long customerId,
                                                       @PathVariable @Positive Long orderId) {
        return ResponseEntity.ok(salesOrderService.getOrderById(customerId, orderId));
    }

    @PostMapping
    public ResponseEntity<SalesOrderResponse> createOrder(@PathVariable @Positive Long customerId,
                                                          @Valid @RequestBody CreateSalesOrderRequest request) {
        SalesOrderResponse created = salesOrderService.createOrder(customerId, request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{orderId}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<SalesOrderResponse> updateOrder(@PathVariable @Positive Long customerId,
                                                          @PathVariable @Positive Long orderId,
                                                          @Valid @RequestBody UpdateSalesOrderRequest request) {
        return ResponseEntity.ok(salesOrderService.updateOrder(customerId, orderId, request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @Positive Long customerId,
                                            @PathVariable @Positive Long orderId) {
        salesOrderService.deleteOrder(customerId, orderId);
        return ResponseEntity.noContent().build();
    }
}
