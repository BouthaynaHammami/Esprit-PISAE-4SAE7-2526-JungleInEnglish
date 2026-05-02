package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Schedules.Entities.Room;
import tn.esprit.language_courses_service.Schedules.Entities.RoomScheduleComplaint;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomScheduleComplaintRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.RoomRepository;
import tn.esprit.language_courses_service.Schedules.Repositories.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomScheduleComplaintService {

    private final RoomScheduleComplaintRepository complaintRepository;
    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;

    public List<RoomScheduleComplaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public RoomScheduleComplaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));
    }

    public RoomScheduleComplaint createComplaintForRoom(Long roomId, RoomScheduleComplaint complaint) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        complaint.setRoom(room);
        return complaintRepository.save(complaint);
    }

    public RoomScheduleComplaint createComplaintForSchedule(Long scheduleId, RoomScheduleComplaint complaint) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));
        complaint.setSchedule(schedule);
        if (schedule.getRoom() != null) {
            complaint.setRoom(schedule.getRoom());
        }
        return complaintRepository.save(complaint);
    }

    public RoomScheduleComplaint updateComplaint(Long id, RoomScheduleComplaint complaint) {
        RoomScheduleComplaint existing = getComplaintById(id);
        existing.setSubject(complaint.getSubject());
        existing.setDescription(complaint.getDescription());
        existing.setStatus(complaint.isStatus());
        existing.setAnswer(complaint.getAnswer());
        existing.setComplainantEmail(complaint.getComplainantEmail());
        existing.setComplainantRole(complaint.getComplainantRole());
        return complaintRepository.save(existing);
    }

    public RoomScheduleComplaint answerComplaint(Long id, String answer) {
        RoomScheduleComplaint complaint = getComplaintById(id);
        complaint.setAnswer(answer);
        complaint.setStatus(true);
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    public List<RoomScheduleComplaint> getComplaintsByRoom(Long roomId) {
        return complaintRepository.findByRoomRoomId(roomId);
    }

    public List<RoomScheduleComplaint> getPendingComplaints() {
        return complaintRepository.findByStatus(false);
    }

    public List<RoomScheduleComplaint> getComplaintsBySchedule(Long scheduleId) {
        return complaintRepository.findByScheduleScheduleId(scheduleId);
    }
}
