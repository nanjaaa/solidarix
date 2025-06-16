package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpOfferMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpOfferMessageRepository extends JpaRepository<HelpOfferMessage, Long> {

    List<HelpOfferMessage> findByHelpOfferIdOrderBySentAtDesc(Long helpOfferId);

}
