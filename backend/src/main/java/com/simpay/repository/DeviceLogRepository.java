package com.simpay.repository;

import com.simpay.entity.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {
    List<DeviceLog> findByUserEmail(String userEmail);
    Optional<DeviceLog> findByVerificationCodeAndProviderAndVerifiedFalse(String code, String provider);
    List<DeviceLog> findByVerifiedTrue();
}
