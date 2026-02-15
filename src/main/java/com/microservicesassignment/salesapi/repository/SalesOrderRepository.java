package com.microservicesassignment.salesapi.repository;

import com.microservicesassignment.salesapi.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {

    Optional<SalesOrder> findByIdAndCustomerId(Long id, Long customerId);

    boolean existsByCustomerId(Long customerId);
}
