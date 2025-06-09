package com.solidarix.backend.service;

import com.solidarix.backend.dto.LocationDto;
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

    public Location findOrCreateLocation(LocationDto address){
        return locationRepository
                .findByFullAddress(address.getFullAddress())
                .orElseGet(()->{
                    return this.createLocation(address);
                });
    }

    public Location createLocation(LocationDto address){

        Location newLocation = new Location();
        newLocation.setNumber(address.getNumber());
        newLocation.setStreetName(address.getStreetName());
        newLocation.setPostalCode(address.getPostalCode());
        newLocation.setCity(address.getCity());

        //Nomralisation des fullAddrress de façon à mettre la virgule après la rue
        newLocation.setFullAddress(""
                + address.getNumber() + " "
                + address.getStreetName() + ", "
                + address.getPostalCode() + " "
                + address.getCity()
        );
        newLocation.setLatitude(address.getLatitude());

        newLocation.setLongitude(address.getLongitude());

        return locationRepository.save(newLocation);
    }
}
