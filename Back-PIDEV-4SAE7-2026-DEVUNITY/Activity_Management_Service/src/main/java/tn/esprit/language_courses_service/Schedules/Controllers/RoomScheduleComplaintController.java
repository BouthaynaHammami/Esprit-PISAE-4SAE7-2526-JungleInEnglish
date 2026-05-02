package tn.esprit.language_courses_service.Schedules.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.Schedules.Entities.RoomScheduleComplaint;
import tn.esprit.language_courses_service.Schedules.Services.RoomScheduleComplaintService;

import java.util.List;

@RestController
@RequestMapping({"/room-schedule-complaints", "/complaints"})
@RequiredArgsConstructor
public class RoomScheduleComplaintController {

    private final RoomScheduleComplaintService complaintService;

    @GetMapping("/all")
    public List<RoomScheduleComplaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public RoomScheduleComplaint getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    @PostMapping("/add/room/{roomId}")
    public RoomScheduleComplaint createComplaintForRoom(@PathVariable Long roomId,
                                                        @RequestBody RoomScheduleComplaint complaint) {
        return complaintService.createComplaintForRoom(roomId, complaint);
    }

    @PostMapping("/add/schedule/{scheduleId}")
    public RoomScheduleComplaint createComplaintForSchedule(@PathVariable Long scheduleId,
                                                            @RequestBody RoomScheduleComplaint complaint) {
        return complaintService.createComplaintForSchedule(scheduleId, complaint);
    }

    @PutMapping("/update/{id}")
    public RoomScheduleComplaint updateComplaint(@PathVariable Long id,
                                                 @RequestBody RoomScheduleComplaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }

    @PatchMapping("/{id}/answer")
    public RoomScheduleComplaint answerComplaint(@PathVariable Long id,
                                                 @RequestParam String answer) {
        return complaintService.answerComplaint(id, answer);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return true;
    }

    @GetMapping("/room/{roomId}")
    public List<RoomScheduleComplaint> getComplaintsByRoom(@PathVariable Long roomId) {
        return complaintService.getComplaintsByRoom(roomId);
    }

    @GetMapping("/schedule/{scheduleId}")
    public List<RoomScheduleComplaint> getComplaintsBySchedule(@PathVariable Long scheduleId) {
        return complaintService.getComplaintsBySchedule(scheduleId);
    }

    @GetMapping("/pending")
    public List<RoomScheduleComplaint> getPendingComplaints() {
        return complaintService.getPendingComplaints();
    }
}