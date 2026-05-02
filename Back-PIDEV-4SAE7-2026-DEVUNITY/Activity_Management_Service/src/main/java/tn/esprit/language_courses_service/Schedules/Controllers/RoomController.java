package tn.esprit.language_courses_service.Schedules.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.Schedules.Services.RoomService;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/all")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @GetMapping("/name/{name}")
    public Room getRoomByName(@PathVariable String name) {
        return roomService.getRoomByName(name);
    }

    @PostMapping("/add")
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }

    @PutMapping("/update/{id}")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return true;
    }

    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    @GetMapping("/level/{level}")
    public List<Room> getRoomsByLevel(@PathVariable int level) {
        return roomService.getRoomsByLevel(level);
    }

    @GetMapping("/capacity/{min}")
    public List<Room> getRoomsByMinCapacity(@PathVariable int min) {
        return roomService.getRoomsByMinCapacity(min);
    }
}