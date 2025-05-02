package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpOfferCreationDto;
import com.solidarix.backend.dto.HelpOfferMessageDto;
import com.solidarix.backend.model.*;
import com.solidarix.backend.repository.HelpOfferMessageRepository;
import com.solidarix.backend.repository.HelpOfferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelpOfferService {

    private final HelpOfferRepository helpOfferRepository;
    private final HelpOfferMessageRepository messageRepository;
    public final HelpRequestService helpRequestService;

    public HelpOfferService(HelpOfferRepository helpOfferRepository, HelpOfferMessageRepository messageRepository, HelpRequestService helpRequestService) {
        this.helpOfferRepository = helpOfferRepository;
        this.messageRepository = messageRepository;
        this.helpRequestService = helpRequestService;
    }

    public HelpOffer findById(Long id){
        return helpOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help Offer not found"));
    }

    public HelpOffer createHelpOffer(User helper, HelpOfferCreationDto helpOfferCreationDto){
        // Quand un HelpOffer se crée, il y a un premier message qui l'accompagne

        HelpRequest helpRequest = helpRequestService.findById(helpOfferCreationDto.getHelpRequestId());
        String firstMessageContent = helpOfferCreationDto.getFirstMessage();
        // setting help offer
        HelpOffer helpOffer = new HelpOffer();
        helpOffer.setHelper(helper);
        helpOffer.setHelpRequest(helpRequest);
        helpOffer.setCreatedAt(LocalDateTime.now());
        helpOffer.setStatus(HelpOfferStatus.IN_DISCUSSION);

        // setting th first message
        HelpOfferMessage message = new HelpOfferMessage();
        message.setHelpOffer(helpOffer);
        message.setSender(helper);
        message.setMessage(firstMessageContent);
        message.setSentAt(LocalDateTime.now());

        helpOfferRepository.save(helpOffer);
        messageRepository.save(message);
        return helpOffer;
    }

    public HelpOfferMessage addMessageToHelpOffer(User sender, HelpOfferMessageDto helpOfferMessageDto){

        String messageContent = helpOfferMessageDto.getMessage();
        HelpOffer helpOffer = helpOfferRepository.findById(helpOfferMessageDto.getHelpOfferId())
                .orElseThrow(()-> new RuntimeException("Help Offer not found"));

        HelpOfferMessage message = new HelpOfferMessage();
        message.setHelpOffer(helpOffer);
        message.setSender(sender);
        message.setMessage(messageContent);
        message.setSentAt(LocalDateTime.now());

        return messageRepository.save(message);
    }


}
