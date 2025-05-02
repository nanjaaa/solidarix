package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long> {



}
