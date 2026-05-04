import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminRoutingModule } from './admin-routing.module';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

import { AdminShellComponent } from './components/admin-shell/admin-shell.component';
import { AdminSidebarComponent } from './components/admin-sidebar/admin-sidebar.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AdminProfileComponent } from './components/profile/admin-profile.component';
import { AdminUsersComponent } from './components/users/admin-users.component';

// Academic Management
import { AdminCoursesComponent } from './components/courses/courses/courses.component';
import { CourseDetailsComponent } from './components/courses/course-details/course-details.component';
import { AdminLessonsComponent } from './components/lessons/admin-lessons.component';
import { AdminQuizzesComponent } from './components/quizzes/admin-quizzes.component';
import { AdminEnrollmentsComponent } from './components/courses/admin-enrollments/admin-enrollments.component';

// Activity Management (standalone components → imported)
import { RoomListComponent } from './components/planning/room/room-list.component';
import { RoomFormComponent } from './components/planning/room/room-form.component';
import { RoomDetailComponent } from './components/planning/room/room-detail.component';
import { ClassListComponent } from './components/planning/class/class-list.component';
import { ClassFormComponent } from './components/planning/class/class-form.component';
import { ClassDetailComponent } from './components/planning/class/class-detail.component';
import { ComplaintListComponent } from './components/planning/complaints/complaint-list.component';
import { ComplaintDetailComponent } from './components/planning/complaints/complaint-detail.component';
import { ScheduleListComponent } from './components/planning/schedule/schedule-list.component';

// Business English / Gamification
import { AdminBadgesComponent } from './components/challenges_competitions/badges/admin-badges.component';
import { AdminChallengesComponent } from './components/challenges_competitions/challenges/admin-challenges.component';
import { AdminStudentStatsComponent } from './components/challenges_competitions/studentStats/admin-student-stats.component';
import { AdminOfferComponent } from './components/business_english_learning/offer/admin-offer.component';
import { AdminEmployeeInvitationComponent } from './components/business_english_learning/employee-invitations/admin-employee-invitation.component';

// Other
import { AdminPlanningComponent } from './components/planning/admin-planning.component';
import { AdminClubsComponent } from './components/clubs/admin-clubs.component';
import { AdminEventsComponent } from './components/events/admin-events.component';
import { AddEventComponent } from './components/events/add-event/add-event.component';
import { AdminLibraryComponent } from './components/library/admin-library.component';
import { AdminTestNiveauComponent } from './components/levelTest/admin-test-niveau.component';
import { AdminEnglishKidsComponent } from './components/english-kids/admin-english-kids.component';
import { AdminEmployeesComponent } from './components/employees/admin-employees.component';
import { AdminRecruitmentComponent } from './components/recruitment/admin-recruitment.component';
import { AdminCertificatesComponent } from './components/certificates/admin-certificates.component';
import { AdminApplicationsComponent } from './components/applications/admin-applications.component';
import { AdminKanbanCertifComponent } from './components/kanban-certif/admin-kanban-certif.component';
import { AdminKanbanEventsComponent } from './components/kanban-events/admin-kanban-events.component';
import { AdminDailyAnalysisComponent } from './components/daily-analysis/admin-daily-analysis.component';
import { AdminDropoutFormsComponent } from './components/dropout-forms/admin-dropout-forms.component';

@NgModule({
  declarations: [
    AdminShellComponent,
    AdminSidebarComponent,
    AdminDashboardComponent,
    AdminProfileComponent,
    AdminUsersComponent,

    // Academic
    AdminCoursesComponent,
    CourseDetailsComponent,
    AdminLessonsComponent,
    AdminQuizzesComponent,
    AdminEnrollmentsComponent,

    // Planning non-standalone
    AdminPlanningComponent,

    // Business English / Gamification
    AdminBadgesComponent,
    AdminChallengesComponent,
    AdminStudentStatsComponent,
    AdminOfferComponent,
    AdminEmployeeInvitationComponent,

    // Other
    AdminClubsComponent,
    AdminEventsComponent,
    AddEventComponent,
    AdminLibraryComponent,
    AdminTestNiveauComponent,
    AdminEnglishKidsComponent,
    AdminEmployeesComponent,
    AdminRecruitmentComponent,
    AdminApplicationsComponent,

    // Kanban (ToDo) & Daily Analysis
    AdminKanbanCertifComponent,
    AdminKanbanEventsComponent,
    AdminDailyAnalysisComponent,
    AdminDropoutFormsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    AdminRoutingModule,

    // Standalone Activity components
    RoomListComponent,
    RoomFormComponent,
    RoomDetailComponent,
    ClassListComponent,
    ClassFormComponent,
    ComplaintListComponent,
    ComplaintDetailComponent,
    ScheduleListComponent,
    AdminCertificatesComponent,
    SharedModule,
  ]
})
export class AdminModule { }