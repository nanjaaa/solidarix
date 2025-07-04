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

    /**
     * Expire les offres en statut PROPOSED ou VALIDATED_BY_REQUESTER selon expirationReference,
     * exécuté toutes les 1 minute.
     */
    @Scheduled(fixedRate = 60_000) // toutes les 1 min
    public void expireOffersByExpirationReference() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusHours(24);

        List<HelpOffer> offersToExpireByReference = helpOfferRepository
                .findByStatusInAndExpirationReferenceBefore(
                        List.of(
                                HelpOfferStatus.PROPOSED,
                                HelpOfferStatus.VALIDATED_BY_REQUESTER
                        ),
                        threshold
                );

        if (!offersToExpireByReference.isEmpty()) {
            for (HelpOffer offer : offersToExpireByReference) {
                offer.setCanceledAt(now);
                switch (offer.getStatus()) {
                    case PROPOSED -> offer.setCancellationJustification("EXPIRATION_AFTER_REQUESTER_INACTION");
                    case VALIDATED_BY_REQUESTER -> offer.setCancellationJustification("EXPIRATION_AFTER_HELPER_INACTION");
                }
                offer.setStatus(HelpOfferStatus.EXPIRED);
            }
            helpOfferRepository.saveAll(offersToExpireByReference);
            log.info("Expired {} help offers by expirationReference", offersToExpireByReference.size());
        }
    }

    /**
     * Expire les offres dont la date de la demande d'aide est dépassée depuis au moins 2 jours,
     * et dont le statut n'est ni DONE, ni FAILED, ni EXPIRED, ni CANCELLED,
     * exécuté une fois par jour à 2h00 du matin.
     */
    @Scheduled(cron = "0 0 2 * * *") // tous les jours à 02:00
    public void expireOffersByHelpRequestDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusDays(2);

        List<HelpOffer> offersToExpireByHelpDate = helpOfferRepository
                .findByStatusNotInAndHelpRequestHelpDateLessThanEqual(
                        List.of(
                                HelpOfferStatus.DONE,
                                HelpOfferStatus.FAILED,
                                HelpOfferStatus.EXPIRED,
                                HelpOfferStatus.CANCELED_BY_HELPER,
                                HelpOfferStatus.CANCELED_BY_REQUESTER
                        ),
                        threshold
                );

        if (!offersToExpireByHelpDate.isEmpty()) {
            for (HelpOffer offer : offersToExpireByHelpDate) {
                offer.setCanceledAt(now);
                offer.setCancellationJustification("NO_FEEDBACK_AFTER_CONFIRMATION");
                offer.setStatus(HelpOfferStatus.EXPIRED);
            }
            helpOfferRepository.saveAll(offersToExpireByHelpDate);
            log.info("Expired {} help offers by helpRequest date", offersToExpireByHelpDate.size());
        }
    }
}
