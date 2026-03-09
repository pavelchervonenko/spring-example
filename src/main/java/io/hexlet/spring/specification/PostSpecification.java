package io.hexlet.spring.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.model.Post;

@Component // Для возможности автоматической инъекции
public class PostSpecification {
    // Генерация спецификации на основе параметров внутри DTO
    // Для удобства каждый фильтр вынесен в свой метод
    public Specification<Post> build(PostParamsDTO params) {
        return withCreatedAtGt(params.getCreatedAtGt())
                .and(withCreatedAtLt(params.getCreatedAtLt()))
                .and(withTitleCont(params.getTitleCont()));
    }

    private Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("createdAt"), date);
    }

    private Specification<Post> withCreatedAtLt(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.lessThan(root.get("createdAt"), date);
    }

    private Specification<Post> withTitleCont(String titlePart) {
        return (root, query, cb) -> titlePart == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("title")), "%" + titlePart.toLowerCase() + "%");
    }
}