package com.solidarix.backend.service;

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

    public HelpRequestComment addComment(User author, HelpRequest helpRequest, HelpRequestComment parentComment, String content){

        HelpRequestComment comment = new HelpRequestComment();
        comment.setAuthor(author);
        comment.setHelpRequest(helpRequest);

        if (parentComment != null && parentComment.getParentComment() != null){
            // Commentaire de niveau 2 max (pas de niveau 3 ou plus)
            comment.setParentComment(parentComment.getParentComment());
        } else {
            comment.setParentComment(parentComment);
        }

        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return helpRequestCommentRepository.save(comment);
    }

    // Create a level 1 comment
    public HelpRequestComment createComment(User author, HelpRequest helpRequest, String content) {
        return addComment(author, helpRequest, null, content);
    }

    // Create a level 2 comment (response to another comment)
    public HelpRequestComment replyToComment(User author, HelpRequest helpRequest, HelpRequestComment parentComment, String content) {
        return addComment(author, helpRequest, parentComment, content);
    }

}
