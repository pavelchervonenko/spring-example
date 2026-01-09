package io.hexlet.spring;

import net.datafaker.Faker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Bean
    public Faker getFaker() {
        return new Faker();
    }

    @GetMapping("/")
    public String hello() {
        return "Добро пожаловать в Hexlet Spring Blog!";
    }

    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog!";
    }
}