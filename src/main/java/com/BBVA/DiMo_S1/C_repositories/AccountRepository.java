package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.List;



@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u JOIN FETCH u.role WHERE u.id = :idUser")
    List<Account> getByIdUser(@Param("idUser") long idUser);

    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u JOIN FETCH u.role WHERE u.id = :idUser AND ac.currency = 1")
    Optional <Account> getUsdAccountByIdUser(@Param("idUser") long idUser);

    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u JOIN FETCH u.role WHERE u.id = :idUser AND ac.currency = 0")
    Optional <Account> getArsAccountByIdUser(@Param("idUser") long idUser);

    Optional<Account> findByCbu (String cbu);

    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user u JOIN FETCH u.role WHERE u.id = :userId AND ac.currency = :currency")
    Optional<Account> findByUserIdAndCurrency(@Param("userId") Long userId, @Param("currency") CurrencyType currency);

    Page<Account> findAll(Pageable pageable);
}

