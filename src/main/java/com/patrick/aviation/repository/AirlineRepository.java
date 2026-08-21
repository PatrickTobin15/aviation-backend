package com.patrick.aviation.repository;

import com.patrick.aviation.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Airline findByIataCode(String iataCode);
}
