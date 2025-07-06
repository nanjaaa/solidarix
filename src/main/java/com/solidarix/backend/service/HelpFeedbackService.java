package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpFeedbackDto;
import com.solidarix.backend.model.HelpFeedback;
import com.solidarix.backend.repository.HelpFeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpFeedbackService {

    private final HelpFeedbackRepository helpFeedbackRepository;

    public HelpFeedbackService(HelpFeedbackRepository helpFeedbackRepository, HelpOfferService helpOfferService) {
        this.helpFeedbackRepository = helpFeedbackRepository;
    }


    public List<HelpFeedbackDto> getFeedbacksForOffer(Long helpOfferId) {
        List<HelpFeedback> feedbacks = helpFeedbackRepository.findByHelpOfferId(helpOfferId);
        return feedbacks.stream()
                .map(HelpFeedbackDto::fromEntity)
                .toList();
    }

    public HelpFeedback save(HelpFeedback feedback) {
        return helpFeedbackRepository.save(feedback);
    }
}
