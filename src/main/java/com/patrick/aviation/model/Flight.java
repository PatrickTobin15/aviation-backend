package com.patrick.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // example "AC 456"
    @NotBlank
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    @NotNull
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "origin_airport_id", nullable = false)
    @NotNull
    private Airport originAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id", nullable = false)
    @NotNull
    private Airport destinationAirport;

    @ManyToOne
    @JoinColumn(name = "gate_id")
    private Gate gate;

    @NotNull
    private LocalDateTime scheduledTime;

    private LocalDateTime actualTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FlightStatus status;
}
