package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {

    @Query(value = """
        SELECT * FROM help_request
        WHERE latitude IS NOT NULL AND longitude IS NOT NULL
        AND (
            6371 * acos(
                cos(radians(:lat)) * cos(radians(latitude)) *
                cos(radians(longitude) - radians(:lon)) +
                sin(radians(:lat)) * sin(radians(latitude))
            )
        ) <= :radiusKm
        ORDER BY 
            CASE WHEN status = 'WAITING_FOR_PROPOSITION' THEN 0 ELSE 1 END,
            created_at DESC
    """, nativeQuery = true)
    List<HelpRequest> findNearbyFeed(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radiusKm") double radiusKm
    );

}
