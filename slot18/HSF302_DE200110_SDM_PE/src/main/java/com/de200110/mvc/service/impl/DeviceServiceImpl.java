package com.de200110.mvc.service.impl;

import com.de200110.mvc.dto.DeviceDTO;
import com.de200110.mvc.entity.Category;
import com.de200110.mvc.entity.Device;
import com.de200110.mvc.repository.CategoryRepository;
import com.de200110.mvc.repository.DeviceRepository;
import com.de200110.mvc.service.DeviceService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class DeviceServiceImpl implements DeviceService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DeviceRepository deviceRepository;
    private final CategoryRepository categoryRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, CategoryRepository categoryRepository) {
        this.deviceRepository = deviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAllByOrderByInstallationDateDesc();
    }

    @Override
    public List<Device> searchByName(String keyword) {
        return deviceRepository.findByDeviceNameContainingIgnoreCaseOrderByInstallationDateDesc(keyword);
    }

    @Override
    public Device getById(Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public void validateBusinessRules(DeviceDTO deviceDTO, BindingResult bindingResult) {
        if(deviceDTO.getInstallationDate() != null && !deviceDTO.getDeviceCode().isBlank() && !bindingResult.hasFieldErrors("deviceCode")) {
            Optional<Device> optionalDevice = deviceRepository.findByDeviceCode(deviceDTO.getDeviceCode());
            if(optionalDevice.isPresent()) {
                bindingResult.addError(new FieldError("DeviceDTO", "deviceCode", "Device Code already exists"));
            }
        }

        if (deviceDTO.getCategoryId() != null) {
            if(!categoryRepository.existsById(deviceDTO.getCategoryId())) {
                bindingResult.addError(new FieldError("DeviceDTO", "categoryId", "Selected category does not exist"));
            }
        }

        if (deviceDTO.getInstallationDate() != null && !deviceDTO.getDeviceCode().isBlank() && !bindingResult.hasFieldErrors("installationDate")) {
            try {
                LocalDate installationDate = LocalDate.parse(deviceDTO.getInstallationDate().trim(), DATE_FORMATTER);
                if (!installationDate.isAfter(LocalDate.now())) {
                    bindingResult.addError(new FieldError("DeviceDTO", "installationDate", "Installation date must be a future date"));
                }
            } catch (DateTimeParseException e) {
                bindingResult.addError(new FieldError("DeviceDTO", "installationDate", "Invalid date format. Please use dd-MM-yyyy"));
            }
        }
    }

    @Override
    public void createDevice(DeviceDTO deviceDTO) {
        Category category = categoryRepository.findById(deviceDTO.getCategoryId()).orElseThrow(
                ()-> new IllegalArgumentException("Category not found with id: " + deviceDTO.getCategoryId())
        );
        Device device = new Device();
        device.setDeviceCode(deviceDTO.getDeviceCode().trim());
        device.setDeviceName(deviceDTO.getDeviceName().trim());
        device.setPrice(deviceDTO.getPrice());
        device.setWarrantyMonths(deviceDTO.getWarrantyMonths());
        device.setInstallationDate(LocalDate.parse(deviceDTO.getInstallationDate().trim(), DATE_FORMATTER));
        device.setCategory(category);
        device.setStatus(deviceDTO.getStatus());

        deviceRepository.save(device);

    }

    @Override
    public void deleteDevice(Integer id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Device not found with id: "+ id));
        if("AC".equalsIgnoreCase(device.getStatus())) {
            throw new IllegalStateException("Cannot delete an active device");
        }
        deviceRepository.deleteById(id);
    }
}


