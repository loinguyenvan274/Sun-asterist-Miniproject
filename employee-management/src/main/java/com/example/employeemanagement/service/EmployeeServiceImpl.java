package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentStatDTO;
import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               UtilityService utilityService,
                               ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees(String name, String department) {
        List<Employee> employees;
        if ((name != null && !name.isBlank()) || (department != null && !department.isBlank())) {
            employees = employeeRepository.search(name, department);
        } else {
            employees = employeeRepository.findAll();
        }
        return employees.stream()
                .map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class))
                .toList();
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return modelMapper.map(employee, EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        Employee employee = modelMapper.map(request, Employee.class);
        employee.setId(null);
        employee.setFullName(utilityService.normalizeName(request.getFullName()));
        employee.setDepartment(utilityService.formatDepartment(request.getDepartment()));
        employee.setEmployeeCode(utilityService.generateEmployeeCode());

        Employee saved = employeeRepository.save(employee);
        return modelMapper.map(saved, EmployeeResponseDTO.class);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        existing.setFullName(utilityService.normalizeName(request.getFullName()));
        existing.setEmail(request.getEmail());
        existing.setDepartment(utilityService.formatDepartment(request.getDepartment()));
        existing.setPosition(request.getPosition());
        existing.setSalary(request.getSalary());

        Employee updated = employeeRepository.save(existing);
        return modelMapper.map(updated, EmployeeResponseDTO.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
    }

    @Override
    public List<DepartmentStatDTO> getDepartmentStatistics() {
        List<Employee> employees = employeeRepository.findAll();
        Map<String, Long> statMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

        return statMap.entrySet().stream()
                .map(entry -> new DepartmentStatDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}
