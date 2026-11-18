package com.example.employeemanagement.config;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.Role;
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.service.UtilityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(DepartmentRepository departmentRepository,
                           EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           UtilityService utilityService,
                           PasswordEncoder passwordEncoder) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            Department it = departmentRepository.save(new Department(null, "IT", "Information Technology"));
            Department hr = departmentRepository.save(new Department(null, "HR", "Human Resources"));
            Department finance = departmentRepository.save(new Department(null, "FINANCE", "Finance & Accounting"));

            if (employeeRepository.count() == 0) {
                employeeRepository.save(new Employee(null, utilityService.generateEmployeeCode(), "Nguyen Van An", "an.nguyen@example.com", "Backend Developer", 1500.0, it));
                employeeRepository.save(new Employee(null, utilityService.generateEmployeeCode(), "Tran Thi Binh", "binh.tran@example.com", "HR Specialist", 1200.0, hr));
                employeeRepository.save(new Employee(null, utilityService.generateEmployeeCode(), "Le Van Cuong", "cuong.le@example.com", "DevOps Engineer", 1800.0, it));
            }
        }

        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "admin", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN));
            userRepository.save(new User(null, "user", passwordEncoder.encode("user123"), Role.ROLE_USER));
        }
    }
}
