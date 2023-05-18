package com.quoraclone.quora.dao;

import com.quoraclone.quora.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value ="SELECT * FROM question t WHERE t.user_id LIKE %?1%", nativeQuery = true)
    List<Question> findAllByUserId(Long userId);

    @Query(value ="SELECT * FROM question t WHERE t.user_id = :userId and t.text like %:keyword%", nativeQuery = true)
    List<Question> findAllByUserIdAndKeyword(Long userId, String keyword);

    @Query(value ="SELECT * FROM question t WHERE t.text like %:keyword%", nativeQuery = true)

    List<Question> findAllBydKeyword(String keyword);
}
