// src/app/features/employee/employee-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeShellComponent } from './components/employee-shell/employee-shell.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { EmployeeProfileComponent } from './components/profile/employee-profile.component';
import { EmployeeRecruitmentsComponent } from './components/recruitments/employee-recruitments.component';
import { EmployeeApplicationsComponent } from './components/applications/employee-applications.component';

const routes: Routes = [
    {
        path: '',
        component: EmployeeShellComponent,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: 'dashboard', component: EmployeeDashboardComponent },
            { path: 'recruitments', component: EmployeeRecruitmentsComponent },
            { path: 'applications', component: EmployeeApplicationsComponent },
            { path: 'profile', component: EmployeeProfileComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EmployeeRoutingModule { }
