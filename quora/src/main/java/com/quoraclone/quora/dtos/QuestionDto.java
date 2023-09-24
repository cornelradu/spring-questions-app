package com.quoraclone.quora.dtos;

import com.quoraclone.quora.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionDto {
    private Long id;
    private String text;
    private Long userId;
    private String username;
    private List<AnswerDto> answers;
    private int sumVotes;
    private boolean owner;
    private Date created;
}
