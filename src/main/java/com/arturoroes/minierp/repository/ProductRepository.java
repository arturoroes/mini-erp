package com.arturoroes.minierp.repository;

import com.arturoroes.minierp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findBySupplier_Id(Long supplierId);
}
