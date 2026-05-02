// src/app/features/tutor/tutor.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TutorRoutingModule } from './tutor-routing.module';
import { TutorShellComponent } from './components/tutor-shell/tutor-shell.component';
import { TutorNavbarComponent } from './components/tutor-navbar/tutor-navbar.component';
import { TutorDashboardComponent } from './components/tutor-dashboard/tutor-dashboard.component';
import { TutorProfileComponent } from './components/profile/tutor-profile.component';
import { TutorPlanningComponent } from './components/planning/tutor-planning.component';
import { TutorComplaintsComponent } from './components/complaints/tutor-complaints.component';
import { TutorLevelTestComponent } from './components/level-test/tutor-level-test.component';

@NgModule({
  declarations: [
    TutorShellComponent,
    TutorNavbarComponent,
    TutorDashboardComponent,
    TutorProfileComponent,
    TutorPlanningComponent,
    TutorComplaintsComponent,
    TutorLevelTestComponent,
  ],
  imports: [
    CommonModule,
    TutorRoutingModule,
    FormsModule
  ]
})
export class TutorModule { }