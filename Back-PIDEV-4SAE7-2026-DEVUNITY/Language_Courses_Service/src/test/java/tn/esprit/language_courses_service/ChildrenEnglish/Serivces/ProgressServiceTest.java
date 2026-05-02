package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Progress;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ProgressRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;

    @InjectMocks
    private ProgressService progressService;

    @Test
    void addProgress_shouldSetLastAccessWhenMissing() {
        Progress progress = new Progress();
        progress.setCompletionRate(50f);
        progress.setLastAccess(null);

        when(progressRepository.save(progress)).thenReturn(progress);

        Progress saved = progressService.addProgress(progress);

        assertNotNull(saved.getLastAccess());
        verify(progressRepository).save(progress);
    }

    @Test
    void getProgressByChildAndCourse_shouldDelegateToRepository() {
        List<Progress> expected = List.of(new Progress(1L, 20f, new Date(), null, null));
        when(progressRepository.findByChild_ChildIdAndCourse_CourseId(10L, 99L)).thenReturn(expected);

        List<Progress> result = progressService.getProgressByChildAndCourse(10L, 99L);

        assertSame(expected, result);
        verify(progressRepository).findByChild_ChildIdAndCourse_CourseId(10L, 99L);
    }
}
