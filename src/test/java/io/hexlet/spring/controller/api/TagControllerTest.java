package io.hexlet.spring.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

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

    private Tag createAndSaveTag() {
        var tag = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .ignore(Select.field(Tag::getPosts))
                .supply(Select.field(Tag::getName), () -> faker.book().title())
                .create();

        return tagRepository.save(tag);
    }

    private Tag createAndSaveTag(String name) {
        var tag = Instancio.of(Tag.class)
                .ignore(Select.field(Tag::getId))
                .ignore(Select.field(Tag::getPosts))
                .set(Select.field(Tag::getName), name)
                .create();

        return tagRepository.save(tag);
    }

    private Tag createAndSaveTagWithPosts(List<Post> posts) {
        var tag = createAndSaveTag("first");

        for (Post post : posts) {
            post.addTag(tag);
            postRepository.save(post);
        }

        return tagRepository.findById(tag.getId()).orElseThrow();
    }

    @Test
    public void testIndex() throws Exception {
        createAndSaveTag("java");
        createAndSaveTag("spring");

        var result = mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        assertThatJson(response).isArray();
        assertThatJson(response).isArray().hasSize(2);
    }

    @Test
    public void testShow() throws Exception {
        var tag = createAndSaveTag("java");

        var result = mockMvc.perform(get("/api/tags/" + tag.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        assertThatJson(response).and(
                v -> v.node("id").isEqualTo(tag.getId()),
                v -> v.node("name").isEqualTo("java")
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = new TagCreateDTO();
        dto.setName("spring");

        var request = post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        assertThatJson(response).node("name").isEqualTo("spring");
        assertThat(tagRepository.findByName("spring")).isPresent();
    }

    @Test
    public void testUpdate() throws Exception {
        var tag = createAndSaveTag("old name");
        var dto = new TagUpdateDTO();
        dto.setName("new name");

        var request = put("/api/tags/" + tag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse().getContentAsString();
        assertThatJson(response).node("name").isEqualTo("new name");

        var updatedTag = tagRepository.findById(tag.getId()).orElseThrow();
        assertThat(updatedTag.getName()).isEqualTo("new name");
    }

    @Test
    public void testDelete() throws Exception {
        var tag = createAndSaveTag("delete");

        mockMvc.perform(delete("/api/tags/" + tag.getId()))
                .andExpect(status().isNoContent());

        assertThat(tagRepository.findById(tag.getId())).isEmpty();
    }

    @Test
    public void testDeleteTagWithPosts() throws Exception {
        var user = createAndSaveUser();
        var post1 = createAndSavePost(user, true);
        var post2 = createAndSavePost(user, true);
        var tag = createAndSaveTag("to delete");

        post1.addTag(tag);
        post2.addTag(tag);
        postRepository.saveAll(List.of(post1, post2));

        Long tagId = tag.getId();
        Long post1Id = post1.getId();
        Long post2Id = post2.getId();

        mockMvc.perform(delete("/api/tags/" + tagId))
                .andExpect(status().isNoContent());

        assertThat(tagRepository.findById(tagId)).isEmpty();

        var updatedPost1 = postRepository.findById(post1Id).orElseThrow();
        var updatedPost2 = postRepository.findById(post2Id).orElseThrow();

        assertThat(updatedPost1.getTags())
                .extracting(Tag::getId)
                .doesNotContain(tagId);

        assertThat(updatedPost2.getTags())
                .extracting(Tag::getId)
                .doesNotContain(tagId);
    }
}
