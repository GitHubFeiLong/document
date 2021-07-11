package com.data.jpa.controller;

import com.data.jpa.po.Author;
import com.data.jpa.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 18:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping("/author")
    public Object createAuthor (@RequestBody Author author) {
        Author save = authorRepository.save(author);
        return save;
    }
}
