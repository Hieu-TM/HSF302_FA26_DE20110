package com.de200110.mvc.repository;

import com.de200110.mvc.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    List<Device> findAllByOrderByInstallationDateDesc();

    List<Device> findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(String deviceName);

    // Q3 Screen Definition #1 - Device Code must be unique
    Optional<Device> findByDeviceCode(String deviceCode);
}
