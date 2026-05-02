package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    public Room getRoomByName(String name) {
        return roomRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Room not found with name: " + name));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {
        Room existing = getRoomById(id);
        existing.setName(room.getName());
        existing.setCapacity(room.getCapacity());
        existing.setLevel(room.getLevel());
        existing.setAvailable(room.isAvailable());
        return roomRepository.save(existing);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailable(true);
    }

    public List<Room> getRoomsByLevel(int level) {
        return roomRepository.findByLevel(level);
    }

    public List<Room> getRoomsByMinCapacity(int capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }
}
