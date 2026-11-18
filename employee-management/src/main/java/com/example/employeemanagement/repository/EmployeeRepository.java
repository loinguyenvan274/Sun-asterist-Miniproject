package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentStatDTO;
import com.example.employeemanagement.dto.SalaryAnalyticsDTO;
import com.example.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentNameIgnoreCase(String departmentName);

    @Query("SELECT e FROM Employee e LEFT JOIN e.department d WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:department IS NULL OR :department = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :department, '%')))")
    List<Employee> searchEmployees(@Param("name") String name, @Param("department") String department);

    @Query("SELECT new com.example.employeemanagement.dto.DepartmentStatDTO(d.name, COUNT(e)) " +
            "FROM Department d LEFT JOIN d.employees e GROUP BY d.name")
    List<DepartmentStatDTO> countEmployeesByDepartment();

    @Query("SELECT COUNT(e) FROM Employee e")
    long countTotalEmployees();

    @Query("SELECT new com.example.employeemanagement.dto.SalaryAnalyticsDTO(d.name, AVG(e.salary), MIN(e.salary), MAX(e.salary), COUNT(e)) " +
            "FROM Department d JOIN d.employees e GROUP BY d.name")
    List<SalaryAnalyticsDTO> getSalaryAnalyticsByDepartment();
}
