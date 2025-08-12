package com.zinios.onboard.Mapper;

import com.zinios.onboard.Entity.Otp;
import com.zinios.onboard.Entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class Mapper {

    public Map<String, Object> mapUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("employeeId", user.getEmployeeId());
        claims.put("userType", user.getUserType().name());
        claims.put("isActive", user.isActive());
        return claims;
    }

    public Otp generateOtp(String otpValue, String email) {
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setOtp(otpValue);
        otp.setCreatedTime(LocalDateTime.now());
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(2)); // 10-minute expiry
        otp.setActive(true);
        return otp;
    }
}
