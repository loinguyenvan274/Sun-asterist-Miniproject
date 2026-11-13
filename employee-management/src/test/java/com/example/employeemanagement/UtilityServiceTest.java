package com.example.employeemanagement;

import com.example.employeemanagement.service.UtilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityServiceTest {

    private UtilityService utilityService;

    @BeforeEach
    void setUp() {
        utilityService = new UtilityService();
    }

    @Test
    void testNormalizeName() {
        assertEquals("Nguyen Van A", utilityService.normalizeName("  nguyen   van   a  "));
        assertEquals("John Doe", utilityService.normalizeName("JOHN DOE"));
        assertEquals("", utilityService.normalizeName(""));
        assertEquals("", utilityService.normalizeName(null));
    }

    @Test
    void testFormatDepartment() {
        assertEquals("IT", utilityService.formatDepartment("it"));
        assertEquals("HUMAN RESOURCES", utilityService.formatDepartment("  human resources  "));
        assertEquals("GENERAL", utilityService.formatDepartment(""));
        assertEquals("GENERAL", utilityService.formatDepartment(null));
    }

    @Test
    void testGenerateEmployeeCode() {
        String code1 = utilityService.generateEmployeeCode();
        String code2 = utilityService.generateEmployeeCode();

        assertNotNull(code1);
        assertNotNull(code2);
        assertTrue(code1.startsWith("EMP"));
        assertTrue(code2.startsWith("EMP"));
        assertNotEquals(code1, code2);
    }
}
