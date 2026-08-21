package com.patrick.aviation.repository;

import com.patrick.aviation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Departures for an airport
    List<Flight> findByOriginAirportId(Long airportId);

    // Arrivals for an airport
    List<Flight> findByDestinationAirportId(Long airportId);
}
