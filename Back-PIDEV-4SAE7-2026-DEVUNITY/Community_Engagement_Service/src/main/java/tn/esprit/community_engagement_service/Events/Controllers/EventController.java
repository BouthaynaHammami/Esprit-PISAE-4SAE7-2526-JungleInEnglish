package tn.esprit.community_engagement_service.Events.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.community_engagement_service.Events.Entities.EventStatus;
import tn.esprit.community_engagement_service.Events.Entities.Events;
import tn.esprit.community_engagement_service.Events.Services.EventService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    private static final String UPLOAD_DIR = "uploads/";
    private static final String BASE_URL = "http://localhost:8081/communities/api/uploads/";

    @PostMapping("/add-with-image")
    public Events addEventWithImage(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String location,
            @RequestParam int capacity,
            @RequestParam String status,
            @RequestParam MultipartFile image
    ) throws Exception {

        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, image.getBytes());

        Events event = new Events();
        event.setTitle(title);
        event.setDescription(description);
        event.setStartDate(LocalDateTime.parse(startDate));
        event.setEndDate(LocalDateTime.parse(endDate));
        event.setLocation(location);
        event.setCapacity(capacity);
        event.setStatus(parseStatus(status));
        event.setImageUrl(BASE_URL + fileName);

        return eventService.addEvent(event);
    }

    @PutMapping("/update-with-image/{id}")
    public Events updateEventWithImage(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String location,
            @RequestParam int capacity,
            @RequestParam String status,
            @RequestParam(required = false) MultipartFile image
    ) throws Exception {

        Events event = eventService.getEventById(id);

        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

        event.setTitle(title);
        event.setDescription(description);
        event.setStartDate(LocalDateTime.parse(startDate));
        event.setEndDate(LocalDateTime.parse(endDate));
        event.setLocation(location);
        event.setCapacity(capacity);
        event.setStatus(parseStatus(status));

        if (image != null && !image.isEmpty()) {
            File folder = new File(UPLOAD_DIR);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, image.getBytes());

            event.setImageUrl(BASE_URL + fileName);
        }

        return eventService.updateEvent(id, event);
    }

    @GetMapping("/all")
    public List<Events> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Events getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    private EventStatus parseStatus(String status) {
        return EventStatus.valueOf(status.trim().toUpperCase().replace(" ", "_"));
    }
}