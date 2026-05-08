package com.arturoroes.minierp.repository;

import com.arturoroes.minierp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional<Customer> findByTaxId(String taxId);

    boolean existsByTaxId(String taxId);
}
