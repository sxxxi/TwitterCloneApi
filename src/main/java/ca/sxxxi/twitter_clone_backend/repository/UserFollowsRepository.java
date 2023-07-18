package ca.sxxxi.twitter_clone_backend.repository;

import ca.sxxxi.twitter_clone_backend.entity.UserFollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowsRepository extends JpaRepository<UserFollowEntity, UserFollowEntity.UserFollowId> {
}
