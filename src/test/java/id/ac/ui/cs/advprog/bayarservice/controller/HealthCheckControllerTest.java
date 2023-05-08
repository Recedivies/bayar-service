package id.ac.ui.cs.advprog.bayarservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthCheckController.class)
class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthCheckShouldReturn200() throws Exception {
        this.mockMvc.perform(get("/health")).andExpect(status().isOk());
    }
}
