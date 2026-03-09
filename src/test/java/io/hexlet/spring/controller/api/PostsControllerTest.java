package io.hexlet.spring.controller.api;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        tagRepository.deleteAll();
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

    private Post createAndSavePost(User user, boolean published) {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getUser))
                .ignore(Select.field(Post::getTags))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .supply(Select.field(Post::isPublished), () -> published)
                .create();

        post.setUser(user);
        return postRepository.save(post);
    }

    private Tag createAndSaveTag(String name) {
        var tag = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .ignore(Select.field(Tag::getPosts))
                .set(Select.field(Tag::getName), name)
                .create();

        return tagRepository.save(tag);
    }

    private Post createAndSavePostWithTags(User user, boolean published, List<Tag> tags) {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getUser))
                .ignore(Select.field(Post::getTags))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .supply(Select.field(Post::isPublished), () -> published)
                .create();

        post.setUser(user);
        post = postRepository.save(post);

        for (Tag tag : tags) {
            post.getTags().add(tag);
            tag.getPosts().add(post);
        }

        return postRepository.save(post);
    }

    @Test
    public void testCreate() throws Exception {
        var user = createAndSaveUser();
        var tag1 = createAndSaveTag("java");
        var tag2 = createAndSaveTag("spring");

        var data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("title", "meow");
        data.put("content", "bebebe");
        data.put("tagsId", List.of(tag1.getId(), tag2.getId()));

        var request = post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value("meow"))
                .andExpect(jsonPath("$.content").value("bebebe"))
                .andExpect(jsonPath("$.tagsId").isArray())
                .andExpect(jsonPath("$.tagsId.length()").value(2))
                .andExpect(jsonPath("$.tagsId[0]").value(tag1.getId()))
                .andExpect(jsonPath("$.tagsId[1]").value(tag2.getId()))
                .andExpect(jsonPath("$.published").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println("Response: " + response);
    }

    @Test
    public void testIndex() throws Exception {
        var user = createAndSaveUser();

        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getUser))
                .ignore(Select.field(Post::getTags))
                .set(Select.field(Post::getTitle), "Java Spring")
                .set(Select.field(Post::isPublished), true)
                .supply(Select.field(Post::getContent), () -> faker.lorem().paragraph())
                .create();
        post.setUser(user);
        postRepository.save(post);

        var secondPost = createAndSavePost(user, true);

        System.out.println("First post title: " + post.getTitle());
        System.out.println("Second post title: " + secondPost.getTitle());

        // Проверим, сколько постов в БД
        var allPosts = postRepository.findAll();
        System.out.println("Total posts in DB: " + allPosts.size());
        allPosts.forEach(p -> System.out.println(" - " + p.getTitle()));

        mockMvc.perform(get("/api/posts")
                        .param("titleCont", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Java Spring"));
    }

    @Test
    public void testShow() throws Exception {
        var user = createAndSaveUser();
        var tag1 = createAndSaveTag("java");
        var tag2 = createAndSaveTag("spring");

        var post = createAndSavePostWithTags(user, true, List.of(tag1, tag2));

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.published").value(true))
                .andExpect(jsonPath("$.tagsId").isArray())
                .andExpect(jsonPath("$.tagsId.length()").value(2));
    }

    @Test
    public void testUpdate() throws Exception {
        var user1 = createAndSaveUser();
        var user2 = createAndSaveUser();
        var tag1 = createAndSaveTag("old name");
        var tag2 = createAndSaveTag("new name");

        var post = createAndSavePostWithTags(user1, false, List.of(tag1));

        var data = new HashMap<>();
        data.put("userId", user2.getId());
        data.put("title", "Mike");
        data.put("content", "Doe");
        data.put("tagsId", List.of(tag1.getId(), tag2.getId()));


        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.userId").value(user2.getId()))
                .andExpect(jsonPath("$.title").value("Mike"))
                .andExpect(jsonPath("$.content").value("Doe"))
                .andExpect(jsonPath("$.tagsId.length()").value(2));

        postRepository.flush();

        var updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getUser().getId()).isEqualTo(user2.getId());
        assertThat(updatedPost.getTitle()).isEqualTo("Mike");
        assertThat(updatedPost.getContent()).isEqualTo("Doe");
        assertThat(updatedPost.getTags()).hasSize(2);
        assertThat(updatedPost.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("old name", "new name");
    }

    @Test
    public void testUpdateTags() throws Exception {
        var user = createAndSaveUser();
        var tag1 = createAndSaveTag("java");
        var tag2 = createAndSaveTag("spring");
        var tag3 = createAndSaveTag("hibernate");

        var post = createAndSavePostWithTags(user, true, List.of(tag1, tag2));

        var data = new HashMap<>();
        data.put("tagsId", List.of(tag1.getId(), tag3.getId()));

        var request = patch("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagsId").isArray())
                .andExpect(jsonPath("$.tagsId.length()").value(2));

        postRepository.flush();

        var updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertThat(updatedPost.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("java", "hibernate");
    }

    @Test
    public void testDelete() throws Exception {
        var user = createAndSaveUser();
        var post = createAndSavePost(user, true);

        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());

        assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    public void testDeleteWithTags() throws Exception {
        var user = createAndSaveUser();
        var tag1 = createAndSaveTag("java");
        var tag2 = createAndSaveTag("spring");

        var post = createAndSavePostWithTags(user, true, List.of(tag1, tag2));

        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());

        assertThat(postRepository.findById(post.getId())).isEmpty();
        assertThat(tagRepository.findById(tag1.getId())).isPresent();
        assertThat(tagRepository.findById(tag2.getId())).isPresent();
    }
}
