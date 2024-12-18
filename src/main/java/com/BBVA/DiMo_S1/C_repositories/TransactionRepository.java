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

    @Query("SELECT tr FROM Transaction tr JOIN FETCH tr.account ac JOIN FETCH ac.user u WHERE u.id = :idUser ORDER BY tr.transactionDate DESC")
    Page<Transaction> getTransactionsByIdUserPageable(@Param("idUser") long idUser, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN FETCH tr.account ac JOIN FETCH ac.user u WHERE u.id = :idUser ORDER BY tr.amount DESC")
    Page<Transaction> getTransactionsOrderMoneyByIdUserPageable(@Param("idUser") long idUser, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN FETCH tr.account ac JOIN FETCH ac.user u WHERE u.id = :idUser ORDER BY tr.amount ASC")
    Page<Transaction> getTransactionsOrderMoneyAscByIdUserPageable(@Param("idUser") long idUser, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN tr.account ac WHERE tr.type = 'deposit' AND ac.user.id = :userId ORDER BY tr.transactionDate DESC")
    Page<Transaction> getDepositsByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN tr.account ac WHERE tr.type = 'payment' AND ac.user.id = :userId ORDER BY tr.transactionDate DESC")
    Page<Transaction> getPaymentByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN tr.account ac WHERE ac.currency = 0 AND ac.user.id = :userId ORDER BY tr.transactionDate DESC")
    Page<Transaction> getTransactionsArsByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN tr.account ac WHERE ac.currency = 1 AND ac.user.id = :userId ORDER BY tr.transactionDate DESC")
    Page<Transaction> getTransactionsUsdByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT tr FROM Transaction tr JOIN FETCH tr.account ac JOIN FETCH ac.user u WHERE u.id = :idUser")
    List<Transaction> getTransactionsByIdUser(@Param("idUser") long idUser);

    Optional<Transaction> findByIdAndAccount_UserId(Long id, Long userId);
}
