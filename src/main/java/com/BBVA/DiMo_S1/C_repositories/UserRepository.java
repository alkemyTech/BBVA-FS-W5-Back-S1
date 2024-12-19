package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email AND u.softDelete IS NOT NULL")
    Optional<User> findByEmailAndSoftDeleteNotNull(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email AND u.softDelete IS NULL")
    Optional<User> findByEmailAndSoftDeleteIsNull(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.id = :idUser AND u.softDelete IS NULL")
    Optional<User> findByIdAndSoftDeleteIsNull(@Param("idUser") long idUser);

    @Query(value = "SELECT u.* FROM users u JOIN users_favoritos uf ON u.id = uf.favoritos_id WHERE uf.user_id = :idUser",
            countQuery = "SELECT COUNT(*) FROM users u JOIN users_favoritos uf on u.id = uf.favoritos_id WHERE uf.user_id" +
                    " = :idUser", nativeQuery = true)
    Page<User> findFavUsers(@Param("idUser") long idUser, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Query(value = "DELETE uf FROM users_favoritos uf WHERE user_id = :idUser AND favoritos_id = :idFavorito",
            nativeQuery = true)
    void deleteUserFromFavList(@Param("idUser") long idUser, @Param("idFavorito") long idFavorito);
}
