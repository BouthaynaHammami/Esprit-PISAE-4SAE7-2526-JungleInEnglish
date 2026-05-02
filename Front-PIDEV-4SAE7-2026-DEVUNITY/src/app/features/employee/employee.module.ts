// src/app/features/employee/employee.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { EmployeeRoutingModule } from './employee-routing.module';
import { EmployeeShellComponent } from './components/employee-shell/employee-shell.component';
import { EmployeeNavbarComponent } from './components/employee-navbar/employee-navbar.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { EmployeeProfileComponent } from './components/profile/employee-profile.component';
import { EmployeeRecruitmentsComponent } from './components/recruitments/employee-recruitments.component';
import { EmployeeApplicationsComponent } from './components/applications/employee-applications.component';

@NgModule({
  declarations: [
    EmployeeShellComponent,
    EmployeeNavbarComponent,
    EmployeeDashboardComponent,
    EmployeeProfileComponent,
    EmployeeRecruitmentsComponent,
    EmployeeApplicationsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    EmployeeRoutingModule,
  ]
})
export class EmployeeModule { }