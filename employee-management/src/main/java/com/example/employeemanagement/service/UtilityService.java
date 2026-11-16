package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UtilityService {

    private final AtomicLong sequence = new AtomicLong(System.currentTimeMillis() % 1000000);

    public String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        return Arrays.stream(name.trim().split("\\s+"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(" "));
    }

    public String formatDepartment(String department) {
        if (department == null || department.isBlank()) {
            return "GENERAL";
        }
        return department.trim().toUpperCase(Locale.ROOT);
    }

    public String generateEmployeeCode() {
        long nextId = sequence.incrementAndGet();
        return "EMP" + nextId;
    }
}
