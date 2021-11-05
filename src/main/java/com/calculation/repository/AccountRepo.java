package com.calculation.repository;

import com.calculation.entity.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends CrudRepository<Account,Long> {
   Account findByEmail(String email);
   Account findByActivationCode(String code);
}
