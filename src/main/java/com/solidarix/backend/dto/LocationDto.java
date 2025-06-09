package com.solidarix.backend.dto;

import lombok.Data;

@Data
public class LocationDto {

    private String fullAddress; // utilisé pour appeler l'API adresse
    private String number;
    private String streetName;
    private String postalCode;
    private String city;
    private Double latitude;
    private Double longitude;

}
