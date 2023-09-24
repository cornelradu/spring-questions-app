package com.quoraclone.quora.services;

import com.quoraclone.quora.controllers.*;
import com.quoraclone.quora.dao.AnswerRepository;
import com.quoraclone.quora.dao.QuestionRepository;
import com.quoraclone.quora.dao.UserRepository;
import com.quoraclone.quora.dao.VoteRepository;
import com.quoraclone.quora.dtos.*;
import com.quoraclone.quora.entity.Answer;
import com.quoraclone.quora.entity.Question;
import com.quoraclone.quora.entity.User;
import com.quoraclone.quora.entity.Vote;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionService {
    private final QuestionRepository repository;

    private final AnswerRepository answerRepository;
    private final AuthenticationManager authenticationManager;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;


    private HashMap<String, String> map = new HashMap<>();

    @Autowired
    QuestionService(QuestionRepository repository, AuthenticationManager authenticationManager,
                    AnswerRepository answerRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.answerRepository = answerRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    public String sendPost(String urlVar){
        String result = "";
        try {
            // Specify the URL you want to send the POST request to
            URL url = new URL("http://localhost:8081/api/url/shorten");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams for sending data
            connection.setDoOutput(true);

            // Set the content type and content length (if applicable)
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonPayload = "{\"originalURL\":\"" + urlVar + "\"}";


            connection.setRequestProperty("Content-Length", String.valueOf(jsonPayload.length()));

            // Replace 'data' with your JSON data as a string

            // Get the output stream to send the data
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Request was successful, read and handle the response
                // You can use connection.getInputStream() to read the response data
                byte[] output = connection.getInputStream().readAllBytes();
                String text = new String(output, "UTF-8");
                System.out.println(text);
                result = text;
            } else {
                // Handle the error (e.g., log, throw exception, etc.)
                System.err.println("HTTP error code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            // Handle exceptions (e.g., IOException, MalformedURLException, etc.)
            e.printStackTrace();
        }
        return result;
    }

    public CreateQuestionResponse createQuestion(CreateQuestionRequest request) {

        Question s = new Question();
        s.setText(request.getQuestion());
        s.setCreated(new Date());
        s.setLast_updated(new Date());

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
        return  findQuestions(questions, principal);
    }

    public List<QuestionDto> getUserQuestions(String keyword,boolean allQuestions){
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();
        List<Question> questions = null;
        if (!allQuestions)
            questions = repository.findAllByUserIdAndKeyword(principal.getId(), keyword);
        else
            questions = repository.findAllBydKeyword(keyword);


        return  findQuestions(questions, principal);
    }

    List<QuestionDto> findQuestions(List<Question> questions, User user){
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
                boolean owner = false;
                if ( user.getId() == answer.getUser().getId()){
                    owner = true;
                }
                String text = answer.getText();
                String regex = "\\b(?:https?|ftp):\\/\\/[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

                // Create a Matcher object
                Matcher matcher = pattern.matcher(text);

                // Find and print URLs
                while (matcher.find()) {
                    String url = matcher.group();
                    System.out.println("Found URL: " + url);
                    String result = "";
                    if (map.containsKey(url)){
                        result = map.get(url);
                    } else {
                        result = this.sendPost(url);
                        map.put(url, result);
                    }
                    text = text.replace(url, "<a href=\"http://localhost:8081/api/url/" + result + "\">http://localhost:8081/api/url/" + result + "</a>");
                }
                answerList.add(new AnswerDto(answer.getId(), text, answer.getUser().getName(), owner));
            }

            String text = question.getText();
            String regex = "\\b(?:https?|ftp):\\/\\/[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            // Create a Matcher object
            Matcher matcher = pattern.matcher(text);

            // Find and print URLs
            while (matcher.find()) {
                String url = matcher.group();
                System.out.println("Found URL: " + url);
                String result = "";
                if (map.containsKey(url)){
                    result = map.get(url);
                } else {
                    result = this.sendPost(url);
                    map.put(url, result);
                }
                text = text.replace(url, "<a href=\"http://localhost:8081/api/url/" + result + "\">http://localhost:8081/api/url/" + result + "</a>");
            }

            boolean owner = false;
            if ( user.getId() == question.getUser().getId()){
                owner = true;
            }

            questionDtoArrayList.add(new QuestionDto(question.getId(),
                    text,
                    question.getUser().getId(),
                    question.getUser().getUsername(),answerList, sum, owner, question.getCreated()));
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

                    String text = answer.getText();
                    String regex = "\\b(?:https?|ftp):\\/\\/[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
                    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

                    // Create a Matcher object
                    Matcher matcher = pattern.matcher(text);

                    // Find and print URLs
                    while (matcher.find()) {
                        String url = matcher.group();
                        System.out.println("Found URL: " + url);
                        String result = "";
                        if (map.containsKey(url)){
                            result = map.get(url);
                        } else {
                            result = this.sendPost(url);
                            map.put(url, result);
                        }
                        text = text.replace(url, "<a href=\"http://localhost:8081/api/url/" + result + "\">http://localhost:8081/api/url/" + result + "</a>");
                    }
                    answersList.add(new AnswerDto(answer.getId(), text, answer.getUser().getName(), false));
                }

                String text = q.getText();
                String regex = "\\b(?:https?|ftp):\\/\\/[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

                // Create a Matcher object
                Matcher matcher = pattern.matcher(text);

                // Find and print URLs
                while (matcher.find()) {
                    String url = matcher.group();
                    System.out.println("Found URL: " + url);
                    String result = "";
                    if (map.containsKey(url)){
                        result = map.get(url);
                    } else {
                        result = this.sendPost(url);
                        map.put(url, result);
                    }
                    text = text.replace(url, "<a href=\"http://localhost:8081/api/url/" + result + "\">http://localhost:8081/api/url/" + result + "</a>");
                }

                return new QuestionDto(q.getId(),
                        text,
                        q.getUser().getId(),
                        q.getUser().getUsername(), answersList,sum, false, q.getCreated());
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
