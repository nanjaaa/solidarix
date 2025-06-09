package com.solidarix.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullAddress;

    private String number;
    private String streetName;
    private String postalCode;
    private String city;

    private Double latitude;
    private Double longitude;

}
