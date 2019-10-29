package com.detelin.productshop.repository;

import com.detelin.productshop.domain.entities.Product;
import com.detelin.productshop.domain.models.service.ProductServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

}
