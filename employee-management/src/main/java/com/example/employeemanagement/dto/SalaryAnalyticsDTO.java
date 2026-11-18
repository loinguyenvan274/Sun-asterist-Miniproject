package com.example.employeemanagement.dto;

public class SalaryAnalyticsDTO {

    private String department;
    private Double averageSalary;
    private Double minSalary;
    private Double maxSalary;
    private Long employeeCount;

    public SalaryAnalyticsDTO() {
    }

    public SalaryAnalyticsDTO(String department, Double averageSalary, Double minSalary, Double maxSalary, Long employeeCount) {
        this.department = department;
        this.averageSalary = averageSalary;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.employeeCount = employeeCount;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Long employeeCount) {
        this.employeeCount = employeeCount;
    }
}
