package com.hsf302.hieutm.ch08.b3;

import java.time.LocalDate;
import java.util.List;

/**
 * Record lưu thông tin người dùng được đăng ký.
 * Dùng Record của Java 16+ để tạo immutable data carrier gọn gàng.
 */
public record User(
        String fullName,
        String email,
        String username,
        String password,
        String gender,
        LocalDate dob,
        String major,
        List<String> hobbies
) {}

