package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedTermDepositRepository extends JpaRepository<FixedTermDeposit, Long> {

    @Query("SELECT ftd FROM FixedTermDeposit ftd JOIN FETCH ftd.account ac JOIN FETCH ac.user u WHERE u.id = :idUser")
    List<FixedTermDeposit> getFixedTermDepositByIdUser(@Param("idUser") long idUser);

    @Query("SELECT ftd FROM FixedTermDeposit ftd JOIN FETCH ftd.account ac")
    List<FixedTermDeposit> getFixedTermDepositsWithAccounts();
}
