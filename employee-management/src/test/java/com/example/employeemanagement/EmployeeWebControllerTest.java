package com.example.employeemanagement;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.dto.EmployeeResponseDTO;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class EmployeeWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Test
    void testListEmployeesView() throws Exception {
        mockMvc.perform(get("/employees/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("departments"));
    }

    @Test
    void testShowAddFormView() throws Exception {
        mockMvc.perform(get("/employees/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("departments"));
    }

    @Test
    void testAddEmployeeSuccess() throws Exception {
        mockMvc.perform(post("/employees/add")
                        .param("fullName", "Hoang Van Duc")
                        .param("email", "duc.hoang.unique@example.com")
                        .param("department", "IT")
                        .param("position", "Frontend Developer")
                        .param("salary", "1400.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/list"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    void testAddEmployeeValidationError() throws Exception {
        mockMvc.perform(post("/employees/add")
                        .param("fullName", "")
                        .param("email", "invalid-email")
                        .param("department", "")
                        .param("position", "")
                        .param("salary", "-50.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-form"))
                .andExpect(model().hasErrors());
    }

    @Test
    void testDeleteEmployeeRedirect() throws Exception {
        EmployeeResponseDTO created = employeeService.createEmployee(
                new EmployeeRequestDTO("Temp Web Delete", "temp.web.unique@example.com", "IT", "Tester", 1100.0)
        );

        mockMvc.perform(get("/employees/delete/" + created.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/list"))
                .andExpect(flash().attributeExists("successMessage"));
    }
}
