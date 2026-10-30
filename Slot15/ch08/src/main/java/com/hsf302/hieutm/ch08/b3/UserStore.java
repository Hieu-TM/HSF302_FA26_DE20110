package com.hsf302.hieutm.ch08.b3;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Kho lưu trữ tài khoản trong bộ nhớ (In-memory User Store).
 */
public final class UserStore {
    private static final Map<String, User> USERS = new HashMap<>();

    static {
        // Tài khoản mẫu có sẵn trong hệ thống
        add(new User("Quản trị viên", "admin@example.com", "admin", "Admin@123",
                "Khác", LocalDate.of(2000, 1, 1), "Kỹ thuật phần mềm", List.of()));
    }

    private UserStore() {}

    public static boolean exists(String username) {
        if (username == null) return false;
        return USERS.containsKey(username.trim().toLowerCase());
    }

    public static void add(User u) {
        USERS.put(u.username().trim().toLowerCase(), u);
    }

    public static Optional<User> login(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        return Optional.ofNullable(USERS.get(username.trim().toLowerCase()))
                .filter(u -> u.password().equals(password));
    }
}

