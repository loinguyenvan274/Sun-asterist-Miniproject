package com.example.employeemanagement;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void testSearchEmployeesByDepartment() throws Exception {
        mockMvc.perform(get("/api/employees").param("department", "IT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].department").value("IT"));
    }

    @Test
    void testGetEmployeeByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testCreateEmployeeSuccess() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "  dang   hoang   nam  ",
                "nam.dang@example.com",
                "finance",
                "Financial Analyst",
                1400.0
        );

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.fullName").value("Dang Hoang Nam"))
                .andExpect(jsonPath("$.data.department").value("FINANCE"))
                .andExpect(jsonPath("$.data.email").value("nam.dang@example.com"))
                .andExpect(jsonPath("$.data.employeeCode").isNotEmpty());
    }

    @Test
    void testCreateEmployeeValidationFailure() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "",
                "invalid-email",
                "",
                "",
                -100.0
        );

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.data.fullName").exists())
                .andExpect(jsonPath("$.data.email").exists());
    }

    @Test
    void testUpdateEmployeeSuccess() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "Nguyen Van An Updated",
                "an.nguyen.updated@example.com",
                "IT",
                "Senior Developer",
                2000.0
        );

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.fullName").value("Nguyen Van An Updated"))
                .andExpect(jsonPath("$.data.position").value("Senior Developer"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(
                "Temporary Employee",
                "temp.employee@example.com",
                "Operations",
                "Operator",
                900.0
        );

        String createResponse = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Number id = com.jayway.jsonpath.JsonPath.read(createResponse, "$.data.id");

        mockMvc.perform(delete("/api/employees/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        mockMvc.perform(get("/api/employees/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStatistics() throws Exception {
        mockMvc.perform(get("/api/employees/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }
}
