package com.patrick.aviation.service;

import com.patrick.aviation.model.Airport;
import com.patrick.aviation.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    public Airport getAirportById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id: " + id));
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport updateAirport(Long id, Airport updatedAirport) {
        Airport existing = getAirportById(id);
        existing.setCode(updatedAirport.getCode());
        existing.setName(updatedAirport.getName());
        existing.setCity(updatedAirport.getCity());
        existing.setCountry(updatedAirport.getCountry());
        return airportRepository.save(existing);
    }

    public void deleteAirport(Long id) {
        airportRepository.deleteById(id);
    }
}
