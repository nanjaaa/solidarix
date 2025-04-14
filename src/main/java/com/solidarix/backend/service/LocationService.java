package com.solidarix.backend.service;

import com.solidarix.backend.model.Location;
import com.solidarix.backend.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final AddressApiService addressApiService;


    public LocationService(LocationRepository locationRepository, AddressApiService addressApiService) {
        this.locationRepository = locationRepository;
        this.addressApiService = addressApiService;
    }

    public Location findOrCreateLocation(String fullAddress){
        return locationRepository
                .findByFullAddress(fullAddress)
                .orElseGet(()->{
                    Location newLocation = addressApiService.fetchLocationFromAddress(fullAddress);
                    return locationRepository.save(newLocation);
                });
    }
}
