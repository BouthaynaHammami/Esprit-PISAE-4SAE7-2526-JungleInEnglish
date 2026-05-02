package tn.esprit.language_courses_service.Schedules.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.language_courses_service.Schedules.Entities.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsAvailable(boolean isAvailable);
    List<Room> findByLevel(int level);
    List<Room> findByCapacityGreaterThanEqual(int capacity);
    Optional<Room> findByNameIgnoreCase(String name);
}