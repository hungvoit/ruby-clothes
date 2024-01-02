package com.javaguides.clothesbabies.repository;

import com.javaguides.clothesbabies.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    User findByPhoneOrEmail(String phone, String email);

    User findByPrincipalId(String principalId);
}
