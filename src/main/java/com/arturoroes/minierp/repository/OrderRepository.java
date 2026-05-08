package com.arturoroes.minierp.repository;

import com.arturoroes.minierp.entity.Order;
import com.arturoroes.minierp.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCustomer_Id(Long customerId);

    List<Order> findByStatus(OrderStatus status);
}