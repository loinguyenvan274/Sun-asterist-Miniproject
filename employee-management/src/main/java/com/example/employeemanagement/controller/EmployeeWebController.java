package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeService employeeService;

    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String index() {
        return "redirect:/employees/list";
    }

    @GetMapping("/list")
    public String listEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Model model) {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees(name, department);
        model.addAttribute("employees", employees);
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepartment", department);
        return "employee-list";
    }

    @GetMapping("/search")
    public String searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            Model model) {
        return listEmployees(name, department, model);
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeRequestDTO());
        model.addAttribute("departments", employeeService.getAllDepartments());
        return "employee-form";
    }

    @PostMapping("/add")
    public String addEmployee(
            @Valid @ModelAttribute("employee") EmployeeRequestDTO request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", employeeService.getAllDepartments());
            return "employee-form";
        }

        try {
            employeeService.createEmployee(request);
            redirectAttributes.addFlashAttribute("successMessage", "Employee created successfully!");
            return "redirect:/employees/list";
        } catch (DuplicateResourceException | DataIntegrityViolationException ex) {
            bindingResult.rejectValue("email", "duplicate", "Email already exists");
            model.addAttribute("departments", employeeService.getAllDepartments());
            return "employee-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/employees/list";
    }
}
