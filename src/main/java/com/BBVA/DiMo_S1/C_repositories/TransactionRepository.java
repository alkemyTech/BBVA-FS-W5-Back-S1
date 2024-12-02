package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT tr FROM Transaction tr JOIN FETCH tr.account ac JOIN FETCH ac.user u WHERE u.id = :idUser")
    List<Transaction> getTransactionsByIdUser(@Param("idUser") long idUser);

    Optional<Transaction> findByIdAndAccount_UserId(Long id, Long userId);

    Page<Transaction> findAllByAccount_UserId(Long userId, Pageable pageable);
}
