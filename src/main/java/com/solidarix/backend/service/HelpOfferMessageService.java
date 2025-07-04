package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpOfferMessageCreationDto;
import com.solidarix.backend.model.HelpOffer;
import com.solidarix.backend.model.HelpOfferMessage;
import com.solidarix.backend.model.User;
import com.solidarix.backend.repository.HelpOfferMessageRepository;
import com.solidarix.backend.repository.HelpOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HelpOfferMessageService {

    private final HelpOfferMessageRepository messageRepository;
    private final HelpOfferRepository helpOfferRepository;

    public HelpOfferMessageService(HelpOfferMessageRepository messageRepository, HelpOfferRepository helpOfferRepository) {
        this.messageRepository = messageRepository;
        this.helpOfferRepository = helpOfferRepository;
    }

    @Transactional
    public HelpOfferMessage addMessageToHelpOffer(User sender, HelpOfferMessageCreationDto dto) {
        HelpOffer helpOffer = helpOfferRepository.findById(dto.getHelpOfferId())
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));

        HelpOfferMessage message = new HelpOfferMessage();
        message.setHelpOffer(helpOffer);
        message.setSender(sender);
        message.setMessage(dto.getMessage());
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    /**
     * Récupère tous les messages d'un help offer
     */
    public List<HelpOfferMessage> findAllMessagesForHelpOffer(Long helpOfferId) {
        return messageRepository.findByHelpOfferId(helpOfferId);
    }

    @Transactional
    private void markAsReadInternal(User user, HelpOfferMessage message) {
        User sender = message.getSender();
        HelpOffer helpOffer = message.getHelpOffer();
        User requester = helpOffer.getHelpRequest().getRequester();
        User helper = helpOffer.getHelper();

        User receiver = sender.equals(requester) ? helper : requester;

        if (user.equals(receiver) && !message.isReadByReceiver()) {
            message.setReadByReceiver(true);
            messageRepository.save(message);
        }
    }


    @Transactional
    public void markAllMessagesAsReadForUser(User user, Long helpOfferId) {
        List<HelpOfferMessage> messages = messageRepository.findByHelpOfferId(helpOfferId);

        for (HelpOfferMessage message : messages) {
            markAsReadInternal(user, message);
        }
    }




}
