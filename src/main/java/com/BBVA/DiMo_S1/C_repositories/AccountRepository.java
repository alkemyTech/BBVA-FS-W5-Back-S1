package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u WHERE u.id = :idUser")
    List<Account> getByIdUser(@Param("idUser") long idUser);



}

