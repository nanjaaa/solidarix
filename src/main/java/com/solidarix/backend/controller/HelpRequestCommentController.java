package com.solidarix.backend.controller;

import com.solidarix.backend.dto.HelpRequestCommentCreationDto;
import com.solidarix.backend.dto.HelpRequestCommentDto;
import com.solidarix.backend.model.HelpRequestComment;
import com.solidarix.backend.model.User;
import com.solidarix.backend.security.CustomUserDetails;
import com.solidarix.backend.service.HelpRequestCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("help-request/comments/")
public class HelpRequestCommentController {

    private final HelpRequestCommentService commentService;

    public HelpRequestCommentController(HelpRequestCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<HelpRequestCommentDto> createComment(
            @RequestBody HelpRequestCommentCreationDto commentCreationDto
            , @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        User user = userDetails.getUser();
        HelpRequestComment comment = commentService.addComment(user, commentCreationDto);
        HelpRequestCommentDto commentDto = HelpRequestCommentDto.fromEntity(comment, new ArrayList<>());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

}
