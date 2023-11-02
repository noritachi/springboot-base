package com.base.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"article")
@EntityListeners(AuditingEntityListener.class)
public class Article extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
    private String avatar;
    private String banner;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    private Integer pinTop = 0; // 0 unpin, 1 pin

    private Integer kind; // 0 internal, ctv

}
