package project.boot.fideco.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.boot.fideco.dto.CommentRequestDTO;
import project.boot.fideco.dto.CommentResponseDTO;
import project.boot.fideco.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDTO save(@RequestBody CommentRequestDTO requestDto) {
        return commentService.save(requestDto);
    }

    @GetMapping("/notice/{notice_num}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByNotice(@PathVariable("notice_num") Integer noticeNum) {
    	List<CommentResponseDTO> comments = commentService.findByNoticeNum(noticeNum);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
