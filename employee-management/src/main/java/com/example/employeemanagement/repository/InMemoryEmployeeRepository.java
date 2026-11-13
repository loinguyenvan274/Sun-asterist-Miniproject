package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<Long, Employee> employeeStorage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        save(new Employee(null, "EMP1001", "Nguyen Van An", "an.nguyen@example.com", "IT", "Backend Developer", 1500.0));
        save(new Employee(null, "EMP1002", "Tran Thi Binh", "binh.tran@example.com", "HR", "HR Specialist", 1200.0));
        save(new Employee(null, "EMP1003", "Le Van Cuong", "cuong.le@example.com", "IT", "DevOps Engineer", 1800.0));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employeeStorage.values());
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employeeStorage.get(id));
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idSequence.incrementAndGet());
        }
        employeeStorage.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public boolean deleteById(Long id) {
        return employeeStorage.remove(id) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeStorage.values().stream()
                .anyMatch(emp -> emp.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return employeeStorage.values().stream()
                .anyMatch(emp -> emp.getEmail().equalsIgnoreCase(email) && !emp.getId().equals(id));
    }

    @Override
    public List<Employee> search(String name, String department) {
        return employeeStorage.values().stream()
                .filter(emp -> {
                    boolean matchName = (name == null || name.isBlank()) ||
                            emp.getFullName().toLowerCase(Locale.ROOT).contains(name.trim().toLowerCase(Locale.ROOT));
                    boolean matchDept = (department == null || department.isBlank()) ||
                            emp.getDepartment().equalsIgnoreCase(department.trim());
                    return matchName && matchDept;
                })
                .toList();
    }
}

