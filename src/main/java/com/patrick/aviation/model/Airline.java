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
@Table(name = "airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    // e.g. "AC" for Air Canada
    @NotBlank
    @Column(unique = true, length = 5)
    private String iataCode;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> flights = new ArrayList<>();
}
