package com.solidarix.backend.service;

import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferStatus;
import com.solidarix.backend.repository.HelpOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelpOfferExpirationScheduler {

    private final HelpOfferRepository helpOfferRepository;

    @Scheduled(fixedRate = 60_000) // toutes les 1 min
    public void expireHelpOffers() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);

        List<HelpOffer> offersToExpire = helpOfferRepository
                .findByStatusInAndExpirationReferenceBefore(
                        List.of(
                                HelpOfferStatus.PROPOSED,
                                HelpOfferStatus.VALIDATED_BY_REQUESTER
                        ),
                        threshold
                );

        if (offersToExpire.isEmpty()) return;

        for (HelpOffer offer : offersToExpire) {

            offer.setCanceledAt(LocalDateTime.now());
            switch (offer.getStatus()) {
                case PROPOSED -> {
                    offer.setCancellationJustification("EXPIRATION_AFTER_REQUESTER_INACTION");
                }
                case VALIDATED_BY_REQUESTER -> {
                    offer.setCancellationJustification("EXPIRATION_AFTER_HELPER_INACTION");
                }
            }
            offer.setStatus(HelpOfferStatus.EXPIRED);
        }

        helpOfferRepository.saveAll(offersToExpire);
        log.info("Expired {} help offers", offersToExpire.size());
    }
}

