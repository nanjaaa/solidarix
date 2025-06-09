package com.solidarix.backend.repository;

import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpOfferRepository extends JpaRepository<HelpOffer, Long> {

    boolean existsByHelperAndHelpRequest(User helper, HelpRequest helpRequest);

}
