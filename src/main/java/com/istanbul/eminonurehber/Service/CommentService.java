package com.istanbul.eminonurehber.Service;

import com.istanbul.eminonurehber.DTO.CommentDTO;
import com.istanbul.eminonurehber.Entity.Comments;
import com.istanbul.eminonurehber.Repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    public List<CommentDTO> getAllComments(){
        return commentRepository.findAll().stream().map(comments -> modelMapper.map(comments,CommentDTO.class)).toList();

    }

    public List<CommentDTO> getCommentsByCompany(Long companyId) {
        return commentRepository.findByCompanyId(companyId).stream().map(comments -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(comments.getId());
            dto.setText(comments.getText());
            dto.setRating(comments.getRating());
            dto.setUsername(comments.getUser().getEmail());

            return dto;
        }).collect(Collectors.toList());
    }

    public void addComment(Comments comments) {
        commentRepository.save(comments);
    }
}
