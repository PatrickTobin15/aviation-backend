package com.patrick.aviation.controller;

import com.patrick.aviation.model.Gate;
import com.patrick.aviation.service.GateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gates")
public class GateController {

    private final GateService gateService;

    @Autowired
    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping
    public List<Gate> getAllGates() {
        return gateService.getAllGates();
    }

    // eexample GET /api/gates/airport/3 all gates at a given airport
    @GetMapping("/airport/{airportId}")
    public List<Gate> getGatesByAirport(@PathVariable Long airportId) {
        return gateService.getGatesByAirport(airportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gate> getGateById(@PathVariable Long id) {
        return ResponseEntity.ok(gateService.getGateById(id));
    }

    @PostMapping
    public ResponseEntity<Gate> createGate(@Valid @RequestBody Gate gate) {
        Gate created = gateService.createGate(gate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gate> updateGate(@PathVariable Long id, @Valid @RequestBody Gate gate) {
        return ResponseEntity.ok(gateService.updateGate(id, gate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGate(@PathVariable Long id) {
        gateService.deleteGate(id);
        return ResponseEntity.noContent().build();
    }
}
