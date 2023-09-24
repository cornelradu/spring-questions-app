package com.quoraclone.quora.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="question")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="text")
    private String text;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private Set<Answer> answers;

    // Created timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    // Last modified timestamp
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date last_updated;
}
