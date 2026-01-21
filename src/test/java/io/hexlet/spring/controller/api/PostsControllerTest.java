package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hexlet.spring.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;

import net.datafaker.Faker;

import org.instancio.Instancio;
import org.instancio.Select;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<>();
        data.put("title", "meow");
        data.put("content", "bebebe");

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("meow"))
                .andExpect(jsonPath("$.content").value("bebebe"));
    }

    @Test
    public void testIndex() throws Exception {
        for (int i = 0; i < 3; i++) {
            var post = Instancio.of(Post.class)
                    .ignore(Select.field(Post::getId))
                    .supply(Select.field(Post::getTitle), () -> faker.book().title())
                    .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                    .supply(Select.field(Post::isPublished), () -> true)
                    .create();
            postRepository.save(post);
        }

        var unpublishedPost = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::isPublished), () -> false)
                .create();
        postRepository.save(unpublishedPost);

        var result = mockMvc.perform(get("/api/posts")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();

        // Отладка - выводим что пришло
        System.out.println("Response: " + response);
        System.out.println("Response length: " + response.length());

        assertThatJson(response).isArray().hasSize(3);
    }

    @Test
    public void testShow() throws Exception {
        var title = faker.book().title();
        var content = faker.lorem().paragraph();
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> title)
                .supply(Select.field(Post::getContent), () -> content)
                .supply(Select.field(Post::isPublished), () -> true)
                .create();
        postRepository.save(post);

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    public void testUpdate() throws Exception {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .create();
        postRepository.save(post);

        var data = new HashMap<>();
        data.put("title", "Mike");
        data.put("content", "Doe");

        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedPost = postRepository.findById(post.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo("Mike");
        assertThat(updatedPost.getContent()).isEqualTo("Doe");
    }

    @Test
    public void testDelete() throws Exception {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .supply(Select.field(Post::isPublished), () -> true)
                .create();
        postRepository.save(post);

        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isNotFound());

    }
}
