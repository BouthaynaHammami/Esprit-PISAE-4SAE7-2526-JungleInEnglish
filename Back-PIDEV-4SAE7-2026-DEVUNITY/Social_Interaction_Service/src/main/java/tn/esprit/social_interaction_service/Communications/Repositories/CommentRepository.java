package tn.esprit.social_interaction_service.Communications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.social_interaction_service.Communications.Entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
