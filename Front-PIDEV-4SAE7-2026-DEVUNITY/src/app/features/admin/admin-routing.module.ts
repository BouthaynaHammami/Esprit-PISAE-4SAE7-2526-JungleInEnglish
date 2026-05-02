// src/app/features/admin/admin-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminShellComponent } from './components/admin-shell/admin-shell.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AdminProfileComponent } from './components/profile/admin-profile.component';
import { AdminUsersComponent } from './components/users/admin-users.component';
import { AdminCoursesComponent } from './components/courses/courses/courses.component';
import { CourseDetailsComponent } from './components/courses/course-details/course-details.component';
import { AdminLessonsComponent } from './components/lessons/admin-lessons.component';
import { AdminQuizzesComponent } from './components/quizzes/admin-quizzes.component';
import { AdminEnrollmentsComponent } from './components/courses/admin-enrollments/admin-enrollments.component';
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
import { RoomListComponent } from './components/planning/room/room-list.component';
import { ClassListComponent } from './components/planning/class/class-list.component';
import { ComplaintListComponent } from './components/planning/complaints/complaint-list.component';
import { AdminBadgesComponent } from './components/challenges_competitions/badges/admin-badges.component';
import { AdminChallengesComponent } from './components/challenges_competitions/challenges/admin-challenges.component';
import { AdminStudentStatsComponent } from './components/challenges_competitions/studentStats/admin-student-stats.component';
import { AdminOfferComponent } from './components/business_english_learning/offer/admin-offer.component';
import { AdminEmployeeInvitationComponent } from './components/business_english_learning/employee-invitations/admin-employee-invitation.component';

const routes: Routes = [
  {
    path: '',
    component: AdminShellComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'users', component: AdminUsersComponent },
      { path: 'courses', component: AdminCoursesComponent },
      { path: 'courses/:id', component: CourseDetailsComponent },
      { path: 'lessons', component: AdminLessonsComponent },
      { path: 'quizzes', component: AdminQuizzesComponent },
      { path: 'enrollments', component: AdminEnrollmentsComponent },
      { path: 'planning', component: AdminPlanningComponent },
      { path: 'rooms', component: RoomListComponent },
      { path: 'classes', component: ClassListComponent },
      { path: 'complaints', component: ComplaintListComponent },
      { path: 'badges', component: AdminBadgesComponent },
      { path: 'challenges', component: AdminChallengesComponent },
      { path: 'student-stats', component: AdminStudentStatsComponent },
      { path: 'offers', component: AdminOfferComponent },
      { path: 'employee-invitations', component: AdminEmployeeInvitationComponent },
      { path: 'clubs', component: AdminClubsComponent },

      { path: 'events', component: AddEventComponent },

      { path: 'library', component: AdminLibraryComponent },
      { path: 'test-niveau', component: AdminTestNiveauComponent },
      { path: 'english-kids', component: AdminEnglishKidsComponent },
      { path: 'employees', component: AdminEmployeesComponent },
      { path: 'recruitment', component: AdminRecruitmentComponent },

      { path: 'certificates', component: AdminCertificatesComponent },

      { path: 'applications', component: AdminApplicationsComponent },

      // Kanban (ToDo) boards & Daily Analysis
      { path: 'kanban-certif', component: AdminKanbanCertifComponent },
      { path: 'kanban-events', component: AdminKanbanEventsComponent },
      { path: 'daily-analysis', component: AdminDailyAnalysisComponent },

      { path: 'profile', component: AdminProfileComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }