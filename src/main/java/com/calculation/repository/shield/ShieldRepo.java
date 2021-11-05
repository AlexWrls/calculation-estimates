package com.calculation.repository.shield;

import com.calculation.entity.scheme.Shield;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShieldRepo extends JpaRepository<Shield,Long> {
    List<Shield> findAllByBuildingId(long buildingId);
}
