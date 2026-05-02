import { Component } from '@angular/core';

@Component({
    selector: 'app-admin-planning',
    templateUrl: './admin-planning.component.html'
})
export class AdminPlanningComponent {
    activeTab: 'rooms' | 'classes' | 'complaints' | 'schedules' = 'rooms';
}
