package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentStatDTO;
import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.dto.ReportSummaryDTO;
import com.example.employeemanagement.dto.SalaryAnalyticsDTO;
import com.example.employeemanagement.model.Department;

import java.util.List;

public interface EmployeeService {

    List<EmployeeResponseDTO> getAllEmployees(String name, String department);

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO request);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request);

    void deleteEmployee(Long id);

    List<DepartmentStatDTO> getDepartmentStatistics();

    long countTotalEmployees();

    List<SalaryAnalyticsDTO> getSalaryAnalytics();

    ReportSummaryDTO getReportSummary();

    List<Department> getAllDepartments();
}
