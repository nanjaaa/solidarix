package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpFeedbackRepository extends JpaRepository<HelpFeedback, Long> {

    List<HelpFeedback> findByHelpOfferId(Long helpOfferId);

    // Pour empêcher un utilisateur de laisser plusieurs feedbacks sur la même HelpOffer (optionnel)
    boolean existsByHelpOfferIdAndAuthorId(Long helpOfferId, Long authorId);
}

