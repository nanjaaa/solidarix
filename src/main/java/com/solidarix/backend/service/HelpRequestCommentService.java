package com.solidarix.backend.service;

import com.solidarix.backend.dto.HelpRequestCommentDto;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.HelpRequestComment;
import com.solidarix.backend.model.User;
import com.solidarix.backend.repository.HelpRequestCommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HelpRequestCommentService {

    private final HelpRequestCommentRepository helpRequestCommentRepository;
    private final HelpRequestService helpRequestService;

    public HelpRequestCommentService(HelpRequestCommentRepository helpRequestCommentRepository, HelpRequestService helpRequestService) {
        this.helpRequestCommentRepository = helpRequestCommentRepository;
        this.helpRequestService = helpRequestService;
    }

    public HelpRequestComment findById(Long helpRequestId){
        return helpRequestCommentRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public HelpRequestComment addComment(User author, HelpRequestCommentDto commentDto){

        HelpRequest helpRequest = helpRequestService.findById(commentDto.getHelpRequestId());
        HelpRequestComment comment = new HelpRequestComment();
        comment.setAuthor(author);
        comment.setHelpRequest(helpRequest);

        if (commentDto.getParentCommentId() != null ){
            HelpRequestComment parentComment = helpRequestCommentRepository.findById(commentDto.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            // Commentaire de niveau 2 max (pas de niveau 3 ou plus)
            if(parentComment.getParentComment() != null){
                comment.setParentComment(parentComment.getParentComment());
            } else {
                comment.setParentComment(parentComment);
            }
        }

        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        return helpRequestCommentRepository.save(comment);

    }


}
