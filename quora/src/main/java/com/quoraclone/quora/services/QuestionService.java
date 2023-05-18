package com.quoraclone.quora.services;

import com.quoraclone.quora.controllers.*;
import com.quoraclone.quora.dao.AnswerRepository;
import com.quoraclone.quora.dao.QuestionRepository;
import com.quoraclone.quora.dao.VoteRepository;
import com.quoraclone.quora.dtos.*;
import com.quoraclone.quora.entity.Answer;
import com.quoraclone.quora.entity.Question;
import com.quoraclone.quora.entity.User;
import com.quoraclone.quora.entity.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    private final AnswerRepository answerRepository;
    private final AuthenticationManager authenticationManager;

    private final VoteRepository voteRepository;

    @Autowired
    QuestionService(QuestionRepository repository, AuthenticationManager authenticationManager,
                    AnswerRepository answerRepository, VoteRepository voteRepository) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.answerRepository = answerRepository;
        this.voteRepository = voteRepository;
    }

    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {

        Question s = new Question();
        s.setText(request.getQuestion());

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();
        s.setUser(principal);
        repository.save(s);


        return CreateQuestionResponse.builder().build();
    }

    public List<QuestionDto> getUserQuestions(boolean allQuestions){
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();


        List<Question> questions = null;
        if(allQuestions == false) {
            questions = repository.findAllByUserId(principal.getId());
        } else {
            questions = repository.findAll();
        }
        return  findQuestions(questions);
    }

    public List<QuestionDto> getUserQuestions(String keyword,boolean allQuestions){
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();
        List<Question> questions = null;
        if (!allQuestions)
            questions = repository.findAllByUserIdAndKeyword(principal.getId(), keyword);
        else
            questions = repository.findAllBydKeyword(keyword);
        return  findQuestions(questions);
    }

    List<QuestionDto> findQuestions(List<Question> questions){
        ArrayList<QuestionDto> questionDtoArrayList = new ArrayList<QuestionDto>();

        for(Question question: questions){
            List<Answer> answers = answerRepository.findAllByQuestionId(question.getId());
            ArrayList<AnswerDto> answerList = new ArrayList<>();


            List<Vote> votes = voteRepository.findAllByQuestionId(question.getId());
            int sum = 0;
            for(Vote vote : votes){
                if(vote.getDirection().equals("up")){
                    sum += 1;
                } else {
                    sum -= 1;
                }
            }
            for(Answer answer : answers){
                answerList.add(new AnswerDto(answer.getId(), answer.getText(), answer.getUser().getName()));
            }
            questionDtoArrayList.add(new QuestionDto(question.getId(),
                    question.getText(),
                    question.getUser().getId(),
                    question.getUser().getUsername(),answerList, sum));
        }
        return questionDtoArrayList;
    }

    public QuestionDto getQuestion(Long id) {
        List<Question> questions = repository.findAll();
        for(Question q : questions){
            if(q.getId() == id){
                List<Answer> answers = answerRepository.findAllByQuestionId(q.getId());
                ArrayList<AnswerDto> answersList = new ArrayList<>();

                List<Vote> votes = voteRepository.findAllByQuestionId(q.getId());
                int sum = 0;
                for(Vote vote : votes){
                    if(vote.getDirection().equals("up")){
                        sum += 1;
                    } else {
                        sum -= 1;
                    }
                }
                for(Answer answer : answers){
                    answersList.add(new AnswerDto(answer.getId(), answer.getText(), answer.getUser().getName()));
                }
                return new QuestionDto(q.getId(),
                        q.getText(),
                        q.getUser().getId(),
                        q.getUser().getUsername(), answersList,sum);
            }
        }
        return null;
    }

    public PostAnswerResponseDto answerQuestion(Long qId, PostAnswerRequest request) {
        Answer ans = new Answer();
        Question q = repository.findById(qId).get();

        ans.setText(request.getAnswer());
        ans.setQuestion(q);
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        ans.setUser(principal);

        answerRepository.save(ans);

        return new PostAnswerResponseDto("success");
    }

    public DeleteAnswerDto deleteAnswer(Long id) {
        this.answerRepository.deleteById(id);
        return new DeleteAnswerDto("Success");
    }

    public void deleteQuestion(Long id) {
        List<Vote> votes = this.voteRepository.findAllByQuestionId(id);

        this.voteRepository.deleteAll(votes);
        this.repository.deleteById(id);
    }

    public void voteQuestion(Long id, VoteRequestDto request) {
        Vote vote = new Vote();
        Question q = repository.findById(id).get();
        vote.setQuestion(q);
        vote.setDirection(request.getVoteDirection());
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();
        vote.setUser(principal);
        Optional<Vote> foundVote = voteRepository.findAllByQuestionIdAndUserId(principal.getId(), q.getId());
        if(foundVote.isPresent()){
            voteRepository.delete(foundVote.get());
        }
        voteRepository.save(vote);
    }
}
