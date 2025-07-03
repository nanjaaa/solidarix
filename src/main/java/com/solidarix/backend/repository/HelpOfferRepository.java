package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferStatus;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long> {

    // Vérifie s'il existe un HelpOffer tel que l'aideur est helper et le HelpRequest correspondant est helpRequest
    boolean existsByHelperAndHelpRequest(User helper, HelpRequest helpRequest);

    @Query("""
        SELECT ho FROM HelpOffer ho
        JOIN ho.helpRequest hr
        WHERE ho.helper.id = :userId OR hr.requester.id = :userId
    """)
    List<HelpOffer> findAllByUserInvolved(@Param("userId") Long userId);

    List<HelpOffer> findByStatusInAndExpirationReferenceBefore(List<HelpOfferStatus> proposed, LocalDateTime threshold);

    // Trouve la liste de HelpOffer dont le help_request_id est helpRequestId
    List<HelpOffer> findByHelpRequestId(Long helpRequestId);
}
