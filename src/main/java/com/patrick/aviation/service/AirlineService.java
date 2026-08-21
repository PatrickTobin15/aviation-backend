package com.patrick.aviation.service;

import com.patrick.aviation.model.Airline;
import com.patrick.aviation.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    @Autowired
    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    public Airline getAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airline not found with id: " + id));
    }

    public Airline createAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    public Airline updateAirline(Long id, Airline updatedAirline) {
        Airline existing = getAirlineById(id);
        existing.setName(updatedAirline.getName());
        existing.setIataCode(updatedAirline.getIataCode());
        return airlineRepository.save(existing);
    }

    public void deleteAirline(Long id) {
        airlineRepository.deleteById(id);
    }
}
