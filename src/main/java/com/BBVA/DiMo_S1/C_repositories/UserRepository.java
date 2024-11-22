package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.role where u.email = :email")
    Optional<User> findByEmail (@Param("email") String email);
}
