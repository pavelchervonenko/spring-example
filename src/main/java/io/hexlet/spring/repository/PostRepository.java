package io.hexlet.spring.repository;

import io.hexlet.spring.model.Page;
import io.hexlet.spring.model.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPublishedTrue(Pageable pageable);
}