package com.example.employeemanagement.dto;

import java.util.List;

public class ReportSummaryDTO {

    private long totalEmployees;
    private long totalDepartments;
    private List<DepartmentStatDTO> departmentStats;
    private List<SalaryAnalyticsDTO> salaryAnalytics;

    public ReportSummaryDTO() {
    }

    public ReportSummaryDTO(long totalEmployees, long totalDepartments, List<DepartmentStatDTO> departmentStats, List<SalaryAnalyticsDTO> salaryAnalytics) {
        this.totalEmployees = totalEmployees;
        this.totalDepartments = totalDepartments;
        this.departmentStats = departmentStats;
        this.salaryAnalytics = salaryAnalytics;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public List<DepartmentStatDTO> getDepartmentStats() {
        return departmentStats;
    }

    public void setDepartmentStats(List<DepartmentStatDTO> departmentStats) {
        this.departmentStats = departmentStats;
    }

    public List<SalaryAnalyticsDTO> getSalaryAnalytics() {
        return salaryAnalytics;
    }

    public void setSalaryAnalytics(List<SalaryAnalyticsDTO> salaryAnalytics) {
        this.salaryAnalytics = salaryAnalytics;
    }
}
