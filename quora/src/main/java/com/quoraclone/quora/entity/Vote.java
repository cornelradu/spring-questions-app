package com.quoraclone.quora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="vote")
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="answer_id")
    private Answer answer;

    @Column(name="direction")
    private String direction;

}