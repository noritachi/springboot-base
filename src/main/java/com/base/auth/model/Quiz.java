package com.base.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"quiz")
@EntityListeners(AuditingEntityListener.class)
public class Quiz extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", columnDefinition = "LONGTEXT")
    private String question;

    @Column(name = "option1", columnDefinition = "LONGTEXT")
    private String option1;
    @Column(name = "option2", columnDefinition = "LONGTEXT")
    private String option2;
    @Column(name = "option3", columnDefinition = "LONGTEXT")
    private String option3;
    @Column(name = "option4", columnDefinition = "LONGTEXT")
    private String option4;

    private String answer;

    private Integer kind; // 0 internal, ctv

}
