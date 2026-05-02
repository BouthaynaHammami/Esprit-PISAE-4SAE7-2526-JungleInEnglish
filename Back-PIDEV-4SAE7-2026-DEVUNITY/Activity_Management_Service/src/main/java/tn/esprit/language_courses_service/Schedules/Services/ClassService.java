package tn.esprit.language_courses_service.Schedules.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Clients.UserClient;
import tn.esprit.language_courses_service.DTO.UserDTO;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;
import tn.esprit.language_courses_service.Schedules.Repositories.ClassRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserClient userClient;

    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }

    public ClassEntity getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
    }

    public ClassEntity getClassByName(String name) {
        return classRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Class not found with name: " + name));
    }

    public ClassEntity assignClassToStudent(Long classId, Long studentId) {
        ClassEntity classEntity = getClassById(classId);
        UserDTO userDTO=userClient.getUserById(studentId);
        if (classEntity==null) {
            throw new RuntimeException("Class not found with id: " + classId);
        }
        if(userDTO==null) {
            throw new RuntimeException("User not found with id: " + studentId);
        }
        userDTO.setClassId(classEntity.getClassId());
        userClient.update(userDTO.getUserId(), userDTO);
        classEntity.setNumberStudents(classEntity.getNumberStudents() + 1);
        return classRepository.save(classEntity);
    }

    public ClassEntity createClass(ClassEntity classEntity) {
        return classRepository.save(classEntity);
    }

    public ClassEntity updateClass(Long id, ClassEntity classEntity) {
        ClassEntity existing = getClassById(id);
        existing.setName(classEntity.getName());
        existing.setNumberStudents(classEntity.getNumberStudents());
        existing.setLevel(classEntity.getLevel());
        return classRepository.save(existing);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }

    public List<ClassEntity> getClassesByLevel(String level) {
        return classRepository.findByLevel(level);
    }
}
