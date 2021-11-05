package com.calculation.repository.shield;

import com.calculation.entity.scheme.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepo extends JpaRepository<Building,Long> {
    Page<Building> findAllByAccountId(long accountId, Pageable pageable);
    Page<Building> findAllByAccountIdAndNameStartingWith(long accountId, String name, Pageable pageable);
}
