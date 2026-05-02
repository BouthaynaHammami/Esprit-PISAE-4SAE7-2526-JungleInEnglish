// src/app/features/student/student.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { StudentRoutingModule } from './student-routing.module';

import { StudentShellComponent } from './components/student-shell/student-shell.component';
import { StudentNavbarComponent } from './components/student-navbar/student-navbar.component';
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

// Kanban & Daily Analysis
import { StudentKanbanCertifComponent } from './components/kanban-certif/student-kanban-certif.component';
import { StudentKanbanEventsComponent } from './components/kanban-events/student-kanban-events.component';
import { StudentDailyAnalysisComponent } from './components/daily-analysis/student-daily-analysis.component';

@NgModule({
  declarations: [
    StudentShellComponent,
    StudentNavbarComponent,
    StudentDashboardComponent,
    StudentProfileComponent,
    StudentCoursesComponent,
    StudentCourseDetailComponent,
    StudentTestNiveauComponent,
    StudentCertificatesComponent,
    StudentEnglishKidsComponent,
    StudentLibraryComponent,
    StudentClubsComponent,
    StudentEventsComponent,
    StudentTopicsComponent,
    StudentQuizComponent,
    StudentChallengesComponent,
    StudentJoinOfferComponent,
    StudentClassComponent,
    StudentComplaintsComponent,
    WordBattleArenaComponent,
    StoryChainArenaComponent,
    SpeedTranslationArenaComponent,

    // Kanban & Daily Analysis
    StudentKanbanCertifComponent,
    StudentKanbanEventsComponent,
    StudentDailyAnalysisComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    StudentRoutingModule,
  ]
})
export class StudentModule { }