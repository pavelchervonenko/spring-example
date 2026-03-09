package io.hexlet.spring.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hexlet.spring.model.Post;
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

import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;

import net.datafaker.Faker;

import org.instancio.Instancio;
import org.instancio.Select;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User createAndSaveUser() {
        var user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getBirthday), () -> LocalDate.of(1990, 1, 1))
                .create();

        return userRepository.save(user);
    }

    private Post createAndSavePost(User user) {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getUser))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .supply(Select.field(Post::isPublished), () -> true)
                .create();

        post.setUser(user);
        user.getPosts().add(post);

        return postRepository.save(post);
    }

    @Test
    public void testCreate() throws Exception {
        var data = new HashMap<>();
        data.put("firstName", "John");
        data.put("lastName", "Doe");
        data.put("email", "john@example.com");

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testIndex() throws Exception {
        createAndSaveUser();

        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        assertThatJson(response).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var user = createAndSaveUser();

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void testUpdate() throws Exception {
        var user = createAndSaveUser();

        var data = new HashMap<>();
        data.put("firstName", "Mike");
        data.put("lastName", "Doe");

        var request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo("Mike");
        assertThat(updated.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testDelete() throws Exception {
        var user = createAndSaveUser();

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteWithPosts() throws Exception {
        var user = createAndSaveUser();
        var post = createAndSavePost(user);

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}
