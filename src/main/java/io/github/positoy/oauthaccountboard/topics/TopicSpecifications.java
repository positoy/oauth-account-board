package io.github.positoy.oauthaccountboard.topics;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

final public class TopicSpecifications {

    public static Specification<TopicEntity> titleLike(String keyword) {
        return new Specification<TopicEntity>() {
            @Override
            public Predicate toPredicate(Root<TopicEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
            }
        };
    }

    public static Specification<TopicEntity> contentLike(String keyword) {
        return new Specification<TopicEntity>() {
            @Override
            public Predicate toPredicate(Root<TopicEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
            }
        };
    }
}
