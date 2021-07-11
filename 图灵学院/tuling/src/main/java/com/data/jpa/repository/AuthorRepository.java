package com.data.jpa.repository;

import com.data.jpa.po.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类描述：
 *
 * @Author msi
 * @Date 2021-07-10 18:13
 * @Version 1.0
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
