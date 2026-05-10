// src/app/features/student/student-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { StudentShellComponent } from './components/student-shell/student-shell.component';
import { StudentDashboardComponent } from './components/student-dashboard/student-dashboard.component';
import { StudentProfileComponent } from './components/profile/student-profile.component';

import { StudentCoursesComponent } from './components/courses/student-courses.component';
import { StudentCourseDetailComponent } from './components/courses/student-course-detail.component';
import { StudentQuizComponent } from './components/courses/student-quiz.component';

import { StudentTestNiveauComponent } from './components/test-niveau/student-test-niveau.component';
import { StudentCertificatesComponent } from './components/certificates/student-certificates.component';
import { StudentEnglishKidsComponent } from './components/english-kids/student-english-kids.component';
import { StudentLibraryComponent } from './components/library/student-library.component';
import { StudentClubsComponent } from './components/clubs/student-clubs.component';
import { StudentEventsComponent } from './components/events/student-events.component';
import { StudentTopicsComponent } from './components/topics/student-topics.component';

import { StudentChallengesComponent } from './components/challenges/student-challenges.component';
import { WordBattleArenaComponent } from './components/challenges/word-battle-arena/word-battle-arena.component';
import { StoryChainArenaComponent } from './components/challenges/story-chain-arena/story-chain-arena.component';
import { SpeedTranslationArenaComponent } from './components/challenges/speed-translation-arena/speed-translation-arena.component';

import { StudentJoinOfferComponent } from './components/join-offer/join-offer.component';
import { StudentClassComponent } from './components/class/student-class.component';
import { StudentComplaintsComponent } from './components/complaints/student-complaints.component';

import { StudentKanbanCertifComponent } from './components/kanban-certif/student-kanban-certif.component';
import { StudentKanbanEventsComponent } from './components/kanban-events/student-kanban-events.component';
import { StudentDailyAnalysisComponent } from './components/daily-analysis/student-daily-analysis.component';

import { StudentDropoutFormComponent } from './components/dropout-form/student-dropout-form.component';
import { ParentDashboardComponent } from './components/parent-dashboard/parent-dashboard.component';

const routes: Routes = [
  {
    path: '',
    component: StudentShellComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      { path: 'dashboard', component: StudentDashboardComponent },
      { path: 'join-offer', component: StudentJoinOfferComponent },
      { path: 'dropout-form', component: StudentDropoutFormComponent },

      { path: 'courses', component: StudentCoursesComponent },
      { path: 'courses/:id', component: StudentCourseDetailComponent },
      { path: 'quiz/:id', component: StudentQuizComponent },

      { path: 'class', component: StudentClassComponent },
      { path: 'complaints', component: StudentComplaintsComponent },

      { path: 'challenges', component: StudentChallengesComponent },
      { path: 'challenges/arena/:id', component: WordBattleArenaComponent },
      { path: 'challenges/story-chain/:id', component: StoryChainArenaComponent },
      { path: 'challenges/translation-race/:id', component: SpeedTranslationArenaComponent },

      { path: 'test-niveau', component: StudentTestNiveauComponent },
      { path: 'certificates', component: StudentCertificatesComponent },
      { path: 'english-kids', component: StudentEnglishKidsComponent },
      { path: 'library', component: StudentLibraryComponent },
      { path: 'clubs', component: StudentClubsComponent },
      { path: 'topics', component: StudentTopicsComponent },
      { path: 'events', component: StudentEventsComponent },

      { path: 'kanban-certif', component: StudentKanbanCertifComponent },
      { path: 'kanban-events', component: StudentKanbanEventsComponent },
      { path: 'daily-analysis', component: StudentDailyAnalysisComponent },

      { path: 'parent-dashboard', component: ParentDashboardComponent },

      { path: 'profile', component: StudentProfileComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudentRoutingModule { }