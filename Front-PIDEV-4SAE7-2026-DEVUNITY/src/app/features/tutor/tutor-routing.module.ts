// src/app/features/tutor/tutor-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TutorShellComponent } from './components/tutor-shell/tutor-shell.component';
import { TutorDashboardComponent } from './components/tutor-dashboard/tutor-dashboard.component';
import { TutorProfileComponent } from './components/profile/tutor-profile.component';
import { TutorPlanningComponent } from './components/planning/tutor-planning.component';
import { TutorComplaintsComponent } from './components/complaints/tutor-complaints.component';
import { TutorLevelTestComponent } from './components/level-test/tutor-level-test.component';

const routes: Routes = [
    {
        path: '',
        component: TutorShellComponent,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: 'dashboard', component: TutorDashboardComponent },
            { path: 'planning', component: TutorPlanningComponent },
            { path: 'complaints', component: TutorComplaintsComponent },
            { path: 'profile', component: TutorProfileComponent },
            { path: 'level-test', component: TutorLevelTestComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TutorRoutingModule { }
