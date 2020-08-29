package io.github.positoy.oauthaccountboard.topics;

import com.sun.istack.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {
    Page<TopicEntity> findAll(@Nullable Specification<TopicEntity> specs, Pageable pageable);
}
