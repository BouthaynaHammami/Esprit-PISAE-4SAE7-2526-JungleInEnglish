package tn.esprit.community_engagement_service.Events.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.community_engagement_service.Events.Entities.Events;

@Repository
public interface EventRepo extends JpaRepository<Events, Long> {
}
