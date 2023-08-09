package ca.sxxxi.twitter_clone_backend.repository;

import ca.sxxxi.twitter_clone_backend.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface UserFeedRepository extends PagingAndSortingRepository<PostEntity, UUID> {
    Page<PostEntity> getPostEntitiesByAuthorIdInAndDateCreatedBefore(List<String> followed, Date dateTime, Pageable pageable);
}
