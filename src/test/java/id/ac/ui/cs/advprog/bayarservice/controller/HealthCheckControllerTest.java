package id.ac.ui.cs.advprog.bayarservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthCheckController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void healthCheckShouldReturn200() throws Exception {
        this.mockMvc.perform(get("/health")).andExpect(status().isOk());
    }
}