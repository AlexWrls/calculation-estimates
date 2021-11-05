package com.calculation.repository.calculation;

import com.calculation.entity.calculation.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CalculationRepo extends JpaRepository<Calculation,Long> {
    List<Calculation> findAllByEstimateId(long id);
}
