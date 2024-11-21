package com.BBVA.DiMo_S1.C_repositories;

import com.BBVA.DiMo_S1.D_models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email);
}
