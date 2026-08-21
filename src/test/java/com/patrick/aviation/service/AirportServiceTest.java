package com.patrick.aviation.service;

import com.patrick.aviation.model.Airport;
import com.patrick.aviation.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport();
        airport.setId(1L);
        airport.setCode("YYZ");
        airport.setName("Toronto Pearson");
        airport.setCity("Toronto");
        airport.setCountry("Canada");
    }

    @Test
    void getAllAirports_returnsAllAirports() {
        when(airportRepository.findAll()).thenReturn(List.of(airport));

        List<Airport> result = airportService.getAllAirports();

        assertEquals(1, result.size());
        assertEquals("YYZ", result.get(0).getCode());
        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void getAirportById_whenExists_returnsAirport() {
        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));

        Airport result = airportService.getAirportById(1L);

        assertEquals("Toronto Pearson", result.getName());
    }

    @Test
    void getAirportById_whenMissing_throwsException() {
        when(airportRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> airportService.getAirportById(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void createAirport_savesAndReturnsAirport() {
        when(airportRepository.save(airport)).thenReturn(airport);

        Airport result = airportService.createAirport(airport);

        assertEquals("YYZ", result.getCode());
        verify(airportRepository, times(1)).save(airport);
    }

    @Test
    void updateAirport_updatesFieldsAndSaves() {
        Airport updated = new Airport();
        updated.setCode("YUL");
        updated.setName("Montreal-Trudeau");
        updated.setCity("Montreal");
        updated.setCountry("Canada");

        when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
        when(airportRepository.save(any(Airport.class))).thenAnswer(inv -> inv.getArgument(0));

        Airport result = airportService.updateAirport(1L, updated);

        assertEquals("YUL", result.getCode());
        assertEquals("Montreal-Trudeau", result.getName());
    }

    @Test
    void deleteAirport_callsRepositoryDelete() {
        airportService.deleteAirport(1L);
        verify(airportRepository, times(1)).deleteById(1L);
    }
}
