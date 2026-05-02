// src/app/features/admin/components/employees/admin-employees.component.ts
import { Component } from '@angular/core';

@Component({
    selector: 'app-admin-employees',
    templateUrl: './admin-employees.component.html'
})
export class AdminEmployeesComponent {
    pageTitle: string = 'Employees Management';
    pageIcon: string = '👔';
}
