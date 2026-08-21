package com.patrick.aviation.controller;

import com.patrick.aviation.model.Flight;
import com.patrick.aviation.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    // GET /api/flights/departures/3 - departures for airport id 3
    @GetMapping("/departures/{airportId}")
    public List<Flight> getDepartures(@PathVariable Long airportId) {
        return flightService.getDeparturesForAirport(airportId);
    }

    // GET /api/flights/arrivals/3 - arrivals for airport id 3
    @GetMapping("/arrivals/{airportId}")
    public List<Flight> getArrivals(@PathVariable Long airportId) {
        return flightService.getArrivalsForAirport(airportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody Flight flight) {
        Flight created = flightService.createFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @Valid @RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.updateFlight(id, flight));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
