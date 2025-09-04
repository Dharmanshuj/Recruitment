package com.zinios.onboard.Repository;

import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserTypeAndIsActiveTrue(UserType userType);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmployeeId(String employeeId);
}

