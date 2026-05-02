// src/app/features/admin/components/users/admin-users.component.ts
import { Component } from '@angular/core';

@Component({
    selector: 'app-admin-users',
    templateUrl: './admin-users.component.html'
})
export class AdminUsersComponent {
    pageTitle: string = 'User Management';
    pageIcon: string = '👥';
}
