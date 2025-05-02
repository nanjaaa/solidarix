package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpRequestCommentDto;
import com.solidarix.backend.model.HelpRequest;
import com.solidarix.backend.model.HelpRequestComment;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpRequestCommentService;
import com.solidarix.backend.service.HelpRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments/")
public class HelpRequestCommentController {

    private final HelpRequestCommentService commentService;
    private final HelpRequestService helpRequestService;

    public HelpRequestCommentController(HelpRequestCommentService helpRequestCommentService, HelpRequestCommentService commentService, HelpRequestService helpRequestService) {
        this.commentService = commentService;
        this.helpRequestService = helpRequestService;
    }

    @PostMapping("/add")
    public ResponseEntity<HelpRequestComment> createComment(
            @RequestBody HelpRequestCommentDto commentDto
            , @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();
        HelpRequestComment comment = commentService.addComment(user, commentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

}
