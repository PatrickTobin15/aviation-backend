package com.patrick.aviation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrick.aviation.model.Airport;
import com.patrick.aviation.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
@ActiveProfiles("test")
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AirportService airportService;

    private Airport buildAirport() {
        Airport airport = new Airport();
        airport.setId(1L);
        airport.setCode("YYZ");
        airport.setName("Toronto Pearson");
        airport.setCity("Toronto");
        airport.setCountry("Canada");
        return airport;
    }

    @Test
    void getAllAirports_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/airports"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", password = "aviation123")
    void getAllAirports_withAuth_returnsList() throws Exception {
        when(airportService.getAllAirports()).thenReturn(List.of(buildAirport()));

        mockMvc.perform(get("/api/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("YYZ"));
    }

    @Test
    @WithMockUser
    void createAirport_withValidBody_returns201() throws Exception {
        Airport airport = buildAirport();
        when(airportService.createAirport(any(Airport.class))).thenReturn(airport);

        mockMvc.perform(post("/api/airports")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(airport)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("YYZ"));
    }

    @Test
    @WithMockUser
    void createAirport_withBlankCode_returns400() throws Exception {
        Airport invalid = buildAirport();
        invalid.setCode("");

        mockMvc.perform(post("/api/airports")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deleteAirport_returns204() throws Exception {
        mockMvc.perform(delete("/api/airports/1"))
                .andExpect(status().isNoContent());
    }
}
