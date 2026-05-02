package tn.esprit.employee.Services.ImplServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.employee.Entities.Recruitment;
import tn.esprit.employee.Repositories.RecruitmentRepository;
import tn.esprit.employee.Services.IServices.IApplicantService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentServiceImplTest {

    @Mock
    private RecruitmentRepository recruitmentRepository;

    @Mock
    private IApplicantService applicantService;

    @InjectMocks
    private RecruitmentServiceImpl recruitmentService;

    private Recruitment testRecruitment;

    @BeforeEach
    void setUp() {
        testRecruitment = new Recruitment();
        testRecruitment.setId(1L);
        testRecruitment.setPositionTitle("Java Developer");
        testRecruitment.setApplicants(List.of());
    }

    @Test
    void getAll_shouldReturnAllRecruitments() {
        when(recruitmentRepository.findAll()).thenReturn(List.of(testRecruitment));

        List<Recruitment> result = recruitmentService.getAll();

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getPositionTitle());
        verify(recruitmentRepository).findAll();
    }

    @Test
    void getById_shouldReturnRecruitmentWhenExists() {
        when(recruitmentRepository.findById(1L)).thenReturn(Optional.of(testRecruitment));

        Recruitment result = recruitmentService.getById(1L);

        assertNotNull(result);
        assertEquals("Java Developer", result.getPositionTitle());
    }

    @Test
    void getById_shouldReturnNullWhenNotFound() {
        when(recruitmentRepository.findById(999L)).thenReturn(Optional.empty());

        Recruitment result = recruitmentService.getById(999L);

        assertNull(result);
    }

    @Test
    void create_shouldPersistRecruitment() {
        when(recruitmentRepository.save(testRecruitment)).thenReturn(testRecruitment);

        Recruitment result = recruitmentService.create(testRecruitment);

        assertNotNull(result);
        verify(recruitmentRepository).save(testRecruitment);
    }

    @Test
    void update_shouldUpdateAndSave() {
        when(recruitmentRepository.save(any(Recruitment.class))).thenReturn(testRecruitment);

        Recruitment result = recruitmentService.update(1L, testRecruitment);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(recruitmentRepository).save(any(Recruitment.class));
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        recruitmentService.delete(1L);

        verify(recruitmentRepository).deleteById(1L);
    }
}
