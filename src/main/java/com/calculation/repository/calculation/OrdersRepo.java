package com.calculation.repository.calculation;

import com.calculation.entity.calculation.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Orders,Long> {
    List<Orders> findAllByCalculationId(long CalculationId);
}
