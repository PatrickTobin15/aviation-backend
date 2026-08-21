package com.patrick.aviation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "YYZ" - keep it short and unique like a real IATA code
    @NotBlank
    @Column(unique = true, length = 10)
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @OneToMany(mappedBy = "airport", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Gate> gates = new ArrayList<>();

    @OneToMany(mappedBy = "originAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> departingFlights = new ArrayList<>();

    @OneToMany(mappedBy = "destinationAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> arrivingFlights = new ArrayList<>();
}
