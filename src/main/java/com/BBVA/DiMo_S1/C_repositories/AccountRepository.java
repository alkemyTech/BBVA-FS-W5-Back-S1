package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}

