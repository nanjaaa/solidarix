package com.solidarix.backend.repository;

import com.solidarix.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByFullAddress(String fullAddress);

    boolean existsByFullAddress(String fullAddress);

}
