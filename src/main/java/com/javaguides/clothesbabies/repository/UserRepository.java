package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String userName);

    User findByPrincipalId(String principalId);
}
