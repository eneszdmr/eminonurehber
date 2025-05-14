package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.DTO.CommentDTO;
import com.istanbul.eminonurehber.Entity.Comments;
import com.istanbul.eminonurehber.Service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDTO> allComments(){
        return commentService.getAllComments();
    }

    @GetMapping("/company/{companyId}")
    public List<CommentDTO> getCommentsByCompany(@PathVariable Long companyId) {
        return commentService.getCommentsByCompany(companyId);
    }

    @PostMapping
    public void addComment(@RequestBody Comments comments) {
        commentService.addComment(comments);
    }
}
