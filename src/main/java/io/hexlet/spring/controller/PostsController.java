package io.hexlet.spring.controller;

import io.hexlet.spring.model.Post;

import io.hexlet.spring.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAllPosts() {
        var posts = postRepository.findAll();
        return posts;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable Long id) {
        var user = postRepository.findById(id).get();
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        postRepository.save(post);
        return post;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable("id") Long id,
                                       @RequestBody Post data) {
        var maybePost = postRepository.findById(id);
        if (maybePost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var post = maybePost.get();
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());

        postRepository.save(post);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") Long id) {
        postRepository.deleteById(id);
    }
}
