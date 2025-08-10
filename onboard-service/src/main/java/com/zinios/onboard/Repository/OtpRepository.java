package com.zinios.onboard.Repository;

import com.zinios.onboard.Entity.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByEmailAndOtpAndIsActiveTrueOrderByCreatedTimeDesc(String email, String otp);

    @Transactional
    @Modifying
    @Query("UPDATE Otp o SET o.isActive = :isActive WHERE o.email = :email")
    void updateIsActiveByEmail(String email, Boolean isActive);

    @Transactional
    @Modifying
    @Query("update Otp o set o.isActive = false where o.email = :email and o.isActive = true")
    void deactivateAllActiveOtpsForEmail(@Param("email") String email);
}
