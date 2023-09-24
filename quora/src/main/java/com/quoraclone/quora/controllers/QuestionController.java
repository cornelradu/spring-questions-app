package com.quoraclone.quora.controllers;

import com.quoraclone.quora.dtos.*;
import com.quoraclone.quora.entity.User;
import com.quoraclone.quora.services.QuestionService;
import com.quoraclone.quora.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")

public class QuestionController {
    private final QuestionService service;

    private final UserService userService;
    @Autowired
    QuestionController(QuestionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @PostMapping("/question")
    public ResponseEntity<CreateQuestionResponse> postQuestion(
            @RequestBody CreateQuestionRequest request
    ){
        CreateQuestionResponse res = this.service.createQuestion(request);
        return ResponseEntity.ok(res);
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @GetMapping("/question")
    public List<QuestionDto> getUserQuestions(@RequestParam(name = "keyword", required = false) String keyword
    ,@RequestParam(name = "searchAll", required = false) String all){

        if(keyword == null) {
            return this.service.getUserQuestions(all == null || all.equals("false"));
        } else {
            return this.service.getUserQuestions(keyword, all == null || all.equals("false"));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @GetMapping("/question/{id}")
    public QuestionDto getQuestion(@PathVariable Long id) {
        QuestionDto res = this.service.getQuestion(id);
        return res;
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @PostMapping("question/{id}/answer")
    public ResponseEntity<PostAnswerResponseDto> postQuestion(
            @RequestBody PostAnswerRequest request, @PathVariable Long id
    ){
        PostAnswerResponseDto res = this.service.answerQuestion(id, request);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @DeleteMapping("answer/{id}")
    public ResponseEntity<DeleteAnswerDto> deleteAnswer(
             @PathVariable Long id
    ){
        DeleteAnswerDto res = this.service.deleteAnswer(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @DeleteMapping("question/{id}")
    public ResponseEntity<GeneralAnswerDto> deleteQuestion(
            @PathVariable Long id
    ){
        this.service.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @PostMapping("question/{id}/vote")
    public ResponseEntity<GeneralAnswerDto> postQuestion(
            @RequestBody VoteRequestDto request, @PathVariable Long id
    ){
        this.service.voteQuestion(id, request);
        return ResponseEntity.ok().build();
    }

}
