package com.calculation.repository.shield;

import com.calculation.entity.scheme.PartShied;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PartShieldRepo extends JpaRepository<PartShied,Long> {
    List<PartShied> findAllByShieldID(long shieldId);
}
