package com.patrick.aviation.service;

import com.patrick.aviation.model.Flight;
import com.patrick.aviation.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirlineService airlineService;
    private final AirportService airportService;
    private final GateService gateService;

    @Autowired
    public FlightService(FlightRepository flightRepository,
                          AirlineService airlineService,
                          AirportService airportService,
                          GateService gateService) {
        this.flightRepository = flightRepository;
        this.airlineService = airlineService;
        this.airportService = airportService;
        this.gateService = gateService;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // departures for a given airport
    public List<Flight> getDeparturesForAirport(Long airportId) {
        return flightRepository.findByOriginAirportId(airportId);
    }

    // arrivals for a given airport
    public List<Flight> getArrivalsForAirport(Long airportId) {
        return flightRepository.findByDestinationAirportId(airportId);
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
    }

    public Flight createFlight(Flight flight) {
        resolveRelations(flight);
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        Flight existing = getFlightById(id);
        existing.setFlightNumber(updatedFlight.getFlightNumber());
        existing.setScheduledTime(updatedFlight.getScheduledTime());
        existing.setActualTime(updatedFlight.getActualTime());
        existing.setStatus(updatedFlight.getStatus());

        if (updatedFlight.getAirline() != null && updatedFlight.getAirline().getId() != null) {
            existing.setAirline(airlineService.getAirlineById(updatedFlight.getAirline().getId()));
        }
        if (updatedFlight.getOriginAirport() != null && updatedFlight.getOriginAirport().getId() != null) {
            existing.setOriginAirport(airportService.getAirportById(updatedFlight.getOriginAirport().getId()));
        }
        if (updatedFlight.getDestinationAirport() != null && updatedFlight.getDestinationAirport().getId() != null) {
            existing.setDestinationAirport(airportService.getAirportById(updatedFlight.getDestinationAirport().getId()));
        }
        if (updatedFlight.getGate() != null && updatedFlight.getGate().getId() != null) {
            existing.setGate(gateService.getGateById(updatedFlight.getGate().getId()));
        }
        return flightRepository.save(existing);
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    private void resolveRelations(Flight flight) {
        flight.setAirline(airlineService.getAirlineById(flight.getAirline().getId()));
        flight.setOriginAirport(airportService.getAirportById(flight.getOriginAirport().getId()));
        flight.setDestinationAirport(airportService.getAirportById(flight.getDestinationAirport().getId()));
        if (flight.getGate() != null && flight.getGate().getId() != null) {
            flight.setGate(gateService.getGateById(flight.getGate().getId()));
        }
    }
}
