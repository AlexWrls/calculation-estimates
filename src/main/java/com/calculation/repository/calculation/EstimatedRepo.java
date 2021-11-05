package com.calculation.repository.calculation;

import com.calculation.entity.calculation.Estimated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EstimatedRepo extends JpaRepository<Estimated, Long> {
    Page<Estimated> findAllByAccountId(Pageable pageable, long accountId);
    Page<Estimated> findAllByContractContainingOrBuildingContainingAndAccountId
            (String contract, String building, Pageable pageable,long accountID);
    List<Estimated> findAllByAccountId(long accountId);
    List<Estimated> findAllByAccountIdAndDateIsBetween(long accountId, LocalDate begin,LocalDate ending);

}
