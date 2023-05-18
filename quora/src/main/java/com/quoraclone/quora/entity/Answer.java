package com.quoraclone.quora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="answer")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="question_id", nullable = false)
    private Question question;

    @Column(name="text")
    private String text;


}