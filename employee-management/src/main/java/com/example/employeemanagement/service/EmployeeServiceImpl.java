package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentStatDTO;
import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.dto.ReportSummaryDTO;
import com.example.employeemanagement.dto.SalaryAnalyticsDTO;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               UtilityService utilityService,
                               ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees(String name, String department) {
        logger.debug("Fetching employees with filter name: {}, department: {}", name, department);
        List<Employee> employees;
        if ((name != null && !name.isBlank()) || (department != null && !department.isBlank())) {
            employees = employeeRepository.searchEmployees(
                    name != null ? name.trim() : null,
                    department != null ? department.trim() : null
            );
        } else {
            employees = employeeRepository.findAll();
        }
        return employees.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        logger.debug("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        return mapToResponseDTO(employee);
    }

    @Override
    @CacheEvict(value = "employeeCount", allEntries = true)
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        logger.info("Creating new employee: name={}, email={}, department={}",
                request.getFullName(), request.getEmail(), request.getDepartment());

        if (employeeRepository.existsByEmail(request.getEmail())) {
            logger.warn("Duplicate email attempted: {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        String formattedDeptName = utilityService.formatDepartment(request.getDepartment());
        Department department = departmentRepository.findByNameIgnoreCase(formattedDeptName)
                .orElseGet(() -> departmentRepository.save(new Department(null, formattedDeptName, formattedDeptName + " Department")));

        Employee employee = new Employee();
        employee.setName(utilityService.normalizeName(request.getFullName()));
        employee.setEmail(request.getEmail().trim());
        employee.setPosition(request.getPosition().trim());
        employee.setSalary(request.getSalary());
        employee.setDepartment(department);
        employee.setEmployeeCode(utilityService.generateEmployeeCode());

        Employee saved = employeeRepository.save(employee);
        logger.info("Employee created successfully with id: {}, code: {}", saved.getId(), saved.getEmployeeCode());
        return mapToResponseDTO(saved);
    }

    @Override
    @CacheEvict(value = "employeeCount", allEntries = true)
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        logger.info("Updating employee with id: {}", id);

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found for update with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });

        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            logger.warn("Duplicate email attempted during update for id {}: {}", id, request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        String formattedDeptName = utilityService.formatDepartment(request.getDepartment());
        Department department = departmentRepository.findByNameIgnoreCase(formattedDeptName)
                .orElseGet(() -> departmentRepository.save(new Department(null, formattedDeptName, formattedDeptName + " Department")));

        existing.setName(utilityService.normalizeName(request.getFullName()));
        existing.setEmail(request.getEmail().trim());
        existing.setPosition(request.getPosition().trim());
        existing.setSalary(request.getSalary());
        existing.setDepartment(department);

        Employee updated = employeeRepository.save(existing);
        logger.info("Employee updated successfully with id: {}", updated.getId());
        return mapToResponseDTO(updated);
    }

    @Override
    @CacheEvict(value = "employeeCount", allEntries = true)
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with id: {}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found for deletion with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        employeeRepository.delete(existing);
        logger.info("Employee deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentStatDTO> getDepartmentStatistics() {
        logger.debug("Fetching department statistics using repository query");
        return employeeRepository.countEmployeesByDepartment();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "employeeCount")
    public long countTotalEmployees() {
        logger.debug("Counting total employees from database (cacheable)");
        return employeeRepository.countTotalEmployees();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryAnalyticsDTO> getSalaryAnalytics() {
        logger.debug("Fetching salary analytics by department");
        return employeeRepository.getSalaryAnalyticsByDepartment();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportSummaryDTO getReportSummary() {
        logger.debug("Generating report summary");
        long totalEmp = countTotalEmployees();
        long totalDept = departmentRepository.count();
        List<DepartmentStatDTO> deptStats = getDepartmentStatistics();
        List<SalaryAnalyticsDTO> salaryStats = getSalaryAnalytics();
        return new ReportSummaryDTO(totalEmp, totalDept, deptStats, salaryStats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = modelMapper.map(employee, EmployeeResponseDTO.class);
        if (employee.getDepartment() != null) {
            dto.setDepartment(employee.getDepartment().getName());
        }
        dto.setFullName(employee.getName());
        return dto;
    }
}
