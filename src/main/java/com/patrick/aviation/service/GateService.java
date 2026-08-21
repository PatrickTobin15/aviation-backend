package com.patrick.aviation.service;

import com.patrick.aviation.model.Airport;
import com.patrick.aviation.model.Gate;
import com.patrick.aviation.repository.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GateService {

    private final GateRepository gateRepository;
    private final AirportService airportService;

    @Autowired
    public GateService(GateRepository gateRepository, AirportService airportService) {
        this.gateRepository = gateRepository;
        this.airportService = airportService;
    }

    public List<Gate> getAllGates() {
        return gateRepository.findAll();
    }

    public List<Gate> getGatesByAirport(Long airportId) {
        return gateRepository.findByAirportId(airportId);
    }

    public Gate getGateById(Long id) {
        return gateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gate not found with id: " + id));
    }

    public Gate createGate(Gate gate) {
        Airport airport = airportService.getAirportById(gate.getAirport().getId());
        gate.setAirport(airport);
        return gateRepository.save(gate);
    }

    public Gate updateGate(Long id, Gate updatedGate) {
        Gate existing = getGateById(id);
        existing.setGateNumber(updatedGate.getGateNumber());
        if (updatedGate.getAirport() != null && updatedGate.getAirport().getId() != null) {
            existing.setAirport(airportService.getAirportById(updatedGate.getAirport().getId()));
        }
        return gateRepository.save(existing);
    }

    public void deleteGate(Long id) {
        gateRepository.deleteById(id);
    }
}
