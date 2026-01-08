package io.hexlet.spring;

import io.hexlet.spring.model.Page;
import io.hexlet.spring.model.Post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;


import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@RestController
//@RequestMapping("/api/posts")
public class Application {
    // Хранилище добавленных СТРАНИЦ, то есть обычный список
    // private List<Page> pages = new ArrayList<Page>();

    // Хранилище добавленных ПОСТОВ
    // private List<Post> posts = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // ---- PAGES ----

    @GetMapping("/")
    public String hello() {
        return "Добро пожаловать в Hexlet Spring Blog!";
    }

    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog!";
    }

//    @GetMapping("/pages") // Список страница
//    public List<Page> index(@RequestParam(defaultValue = "10") Integer limit) {
//        return pages.stream().limit(limit).toList();
//    }
//
//    @PostMapping("/pages") // Создание страницы
//    public Page create(@RequestBody Page page) {
//        pages.add(page);
//        return page;
//    }
//
//    @GetMapping("/pages/{id}") // Вывод страницы
//    public Optional<Page> show(@PathVariable String id) {
//        var page = pages.stream()
//                .filter(p -> p.getSlug().equals(id))
//                .findFirst();
//        return page;
//    }
//
//    @PutMapping("/pages/{id}") // Обновление страницы
//    public Page update(@PathVariable String id, @RequestBody Page data) {
//        var maybePage = pages.stream()
//                .filter(p -> p.getSlug().equals(id))
//                .findFirst();
//        if (maybePage.isPresent()) {
//            var page = maybePage.get();
//            page.setSlug(data.getSlug());
//            page.setName(data.getName());
//            page.setBody(data.getBody());
//        }
//        return data;
//    }
//
//    @DeleteMapping("/pages/{id}") // Удаление страницы
//    public void destroy(@PathVariable String id) {
//        pages.removeIf(p -> p.getSlug().equals(id));
//    }

//    // ---- POSTS ----
//
//    @GetMapping()
//    public ResponseEntity<List<Post>> index(
//            @RequestParam(defaultValue = "10") Integer limit,
//            @RequestParam(defaultValue = "1") Integer page) {
//
//        var result = posts.stream().skip((page - 1) * limit).limit(limit).toList();
//
//        return ResponseEntity.status(200).body(result);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Post> show(@PathVariable String id) {
//        var post = posts.stream()
//                .filter(p -> p.getTitle().equals(id))
//                .findFirst();
//
//        if (post.isPresent()) {
//            return ResponseEntity.status(200).body(post.get());
//        } else {
//            return ResponseEntity.status(404).build();
//        }
//    }
//
//    @PostMapping("")
//    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
//        posts.add(post);
//
//        return ResponseEntity.status(201).body(post);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Post> update(@PathVariable String id, @Valid @RequestBody Post data) {
//        var maybePost = posts.stream()
//                .filter(p -> p.getTitle().equals(id))
//                .findFirst();
//
//        if (maybePost.isPresent()) {
//            var post = maybePost.get();
//            post.setTitle(data.getTitle());
//            post.setAuthor(data.getAuthor());
//            post.setContent(data.getContent());
//            post.setCreatedAt(data.getCreatedAt());
//
//            return ResponseEntity.status(200).body(post);
//        } else {
//            return ResponseEntity.status(404).build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable String id) {
//        boolean removed = posts.removeIf(p -> p.getTitle().equals(id));
//
//        if (removed) {
//            return ResponseEntity.status(204).build();
//        } else {
//            return ResponseEntity.status(404).build();
//        }
//    }
}