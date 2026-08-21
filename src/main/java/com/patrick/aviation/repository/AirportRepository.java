package com.patrick.aviation.repository;

import com.patrick.aviation.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Airport findByCode(String code);
}
