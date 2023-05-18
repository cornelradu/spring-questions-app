package com.quoraclone.quora.dao;

import com.quoraclone.quora.entity.Question;
import com.quoraclone.quora.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value ="SELECT * FROM vote t WHERE t.question_id LIKE %?1%", nativeQuery = true)

    List<Vote> findAllByQuestionId(Long id);

    @Query(value ="SELECT * FROM vote t WHERE t.user_id LIKE %?1% and t.question_id LIKE %?2%", nativeQuery = true)
    Optional<Vote> findAllByQuestionIdAndUserId(Long userId, Long questionId);
}
