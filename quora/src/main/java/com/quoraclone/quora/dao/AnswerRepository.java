package com.quoraclone.quora.dao;

import com.quoraclone.quora.entity.Answer;
import com.quoraclone.quora.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query(value ="SELECT * FROM answer t WHERE t.question_id LIKE %?1%", nativeQuery = true)
    List<Answer> findAllByQuestionId(Long questionId);
}