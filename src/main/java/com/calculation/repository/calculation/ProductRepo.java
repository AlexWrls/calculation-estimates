package com.calculation.repository.calculation;

import com.calculation.entity.calculation.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findAllByAccountId(long accountId);
}
