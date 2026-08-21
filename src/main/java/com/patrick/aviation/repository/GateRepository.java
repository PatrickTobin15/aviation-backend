package com.patrick.aviation.repository;

import com.patrick.aviation.model.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GateRepository extends JpaRepository<Gate, Long> {
    List<Gate> findByAirportId(Long airportId);
}
