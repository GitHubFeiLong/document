package com.data.jpa.po;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 类描述：
 * 作者
 * @Author msi
 * @Date 2021-07-10 17:57
 * @Version 1.0
 */
@Data
@Entity
@Table
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(targetEntity = Article.class, mappedBy = "author")
    private List<Article> articles;
}
