package com.data.jpa.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 类描述：
 * 文章
 * @Author msi
 * @Date 2021-07-10 17:59
 * @Version 1.0
 */
@Entity
@Data
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(targetEntity = Author.class)
    @JoinColumn(name = "author_id", referencedColumnName = "id", foreignKey =  @ForeignKey(name = "fk_article_author_id"))
    private Author author;
}
