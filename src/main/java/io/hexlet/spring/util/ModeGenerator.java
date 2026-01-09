package io.hexlet.spring.util;

import io.hexlet.spring.model.User;
import io.hexlet.spring.model.Post;

import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.repository.PostRepository;

import jakarta.annotation.PostConstruct;

import net.datafaker.Faker;

import org.springframework.stereotype.Component;

@Component()
public class ModeGenerator {
    private final Faker faker;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ModeGenerator(Faker faker,
                         UserRepository userRepository,
                         PostRepository postRepository, Faker faker1, UserRepository userRepository1, PostRepository postRepository1) {

        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostConstruct
    public void generateData() {
        for (int i = 0; i < 5; i++) {
            var user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            userRepository.save(user);

            var post = new Post();
            post.setContent(faker.lorem().paragraph());
            post.setPublished(faker.bool().bool());
            post.setTitle(faker.book().title());

            postRepository.save(post);
        }
    }
}
