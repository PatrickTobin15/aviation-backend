package com.patrick.aviation.service;

import com.patrick.aviation.model.*;
import com.patrick.aviation.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirlineService airlineService;
    @Mock
    private AirportService airportService;
    @Mock
    private GateService gateService;

    @InjectMocks
    private FlightService flightService;

    private Airport toronto;
    private Airport montreal;
    private Airline airCanada;
    private Flight flight;

    @BeforeEach
    void setUp() {
        toronto = new Airport();
        toronto.setId(1L);
        toronto.setCode("YYZ");

        montreal = new Airport();
        montreal.setId(2L);
        montreal.setCode("YUL");

        airCanada = new Airline();
        airCanada.setId(1L);
        airCanada.setIataCode("AC");

        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AC456");
        flight.setAirline(airCanada);
        flight.setOriginAirport(toronto);
        flight.setDestinationAirport(montreal);
        flight.setScheduledTime(LocalDateTime.now().plusHours(2));
        flight.setStatus(FlightStatus.SCHEDULED);
    }

    @Test
    void getDeparturesForAirport_returnsFlightsOriginatingThere() {
        when(flightRepository.findByOriginAirportId(1L)).thenReturn(List.of(flight));

        List<Flight> result = flightService.getDeparturesForAirport(1L);

        assertEquals(1, result.size());
        assertEquals("AC456", result.get(0).getFlightNumber());
    }

    @Test
    void getArrivalsForAirport_returnsFlightsLandingThere() {
        when(flightRepository.findByDestinationAirportId(2L)).thenReturn(List.of(flight));

        List<Flight> result = flightService.getArrivalsForAirport(2L);

        assertEquals(1, result.size());
        assertEquals("YUL", result.get(0).getDestinationAirport().getCode());
    }

    @Test
    void createFlight_resolvesRelationsAndSaves() {
        Flight incoming = new Flight();
        Airline airlineRef = new Airline();
        airlineRef.setId(1L);
        Airport originRef = new Airport();
        originRef.setId(1L);
        Airport destRef = new Airport();
        destRef.setId(2L);

        incoming.setFlightNumber("AC789");
        incoming.setAirline(airlineRef);
        incoming.setOriginAirport(originRef);
        incoming.setDestinationAirport(destRef);
        incoming.setScheduledTime(LocalDateTime.now());
        incoming.setStatus(FlightStatus.SCHEDULED);

        when(airlineService.getAirlineById(1L)).thenReturn(airCanada);
        when(airportService.getAirportById(1L)).thenReturn(toronto);
        when(airportService.getAirportById(2L)).thenReturn(montreal);
        when(flightRepository.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        Flight result = flightService.createFlight(incoming);

        assertEquals("AC789", result.getFlightNumber());
        assertEquals("YYZ", result.getOriginAirport().getCode());
        assertEquals("YUL", result.getDestinationAirport().getCode());
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void getFlightById_whenMissing_throwsException() {
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> flightService.getFlightById(99L));
    }
}
