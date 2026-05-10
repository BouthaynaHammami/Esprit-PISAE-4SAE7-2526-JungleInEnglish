import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserDTO } from '../../../../core/models/user-dto.model';
import { LearnerUserLookupService } from '../../../../core/services/learner-user-lookup.service';

@Component({
    selector: 'app-admin-users',
    templateUrl: './admin-users.component.html'
})
export class AdminUsersComponent implements OnInit {
    pageTitle: string = 'User Management';
    pageIcon: string = '👥';
    users: UserDTO[] = [];
    loading: boolean = true;
    error: string = '';

    // Filters & Search
    searchTerm: string = '';
    roleFilter: string = 'ALL';
    statusFilter: string = 'ALL';
    sortBy: string = 'name';

    selectedUser: UserDTO | null = null;
    isViewModalOpen: boolean = false;
    isEditModalOpen: boolean = false;
    editForm: FormGroup;

    constructor(
        private userService: LearnerUserLookupService,
        private fb: FormBuilder
    ) {
        this.editForm = this.fb.group({
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            role: ['', Validators.required],
            classId: ['']
        });
    }

    ngOnInit(): void {
        this.loadUsers();
    }

    loadUsers(): void {
        this.loading = true;
        this.userService.getAll().subscribe({
            next: (data) => {
                this.users = data;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load users. Please make sure the Learner Management Service is running.';
                this.loading = false;
                console.error(err);
            }
        });
    }

    get filteredUsers(): UserDTO[] {
        let filtered = [...this.users];

        // Search filter
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(u => 
                u.firstName.toLowerCase().includes(term) || 
                u.lastName.toLowerCase().includes(term) || 
                u.email.toLowerCase().includes(term)
            );
        }

        // Role filter
        if (this.roleFilter !== 'ALL') {
            filtered = filtered.filter(u => u.role === this.roleFilter);
        }

        // Status filter (Mocked since DTO might not have it yet, but logic is ready)
        // if (this.statusFilter !== 'ALL') { ... }

        // Sort
        filtered.sort((a, b) => {
            if (this.sortBy === 'name') {
                return (a.firstName + a.lastName).localeCompare(b.firstName + b.lastName);
            }
            if (this.sortBy === 'role') {
                return (a.role || '').localeCompare(b.role || '');
            }
            return 0;
        });

        return filtered;
    }

    getRoleBadgeClass(role: string): string {
        switch (role?.toUpperCase()) {
            case 'ADMIN': return 'bg-[#FFDDD2] text-[#E29578] shadow-sm';
            case 'STUDENT': return 'bg-green-50 text-green-600 border border-green-100';
            case 'TUTOR': return 'bg-blue-50 text-blue-600 border border-blue-100';
            case 'COMPANY': return 'bg-[#83C5BE] text-[#006D77]';
            default: return 'bg-gray-100 text-gray-500';
        }
    }

    viewUser(user: UserDTO): void {
        this.selectedUser = user;
        this.isViewModalOpen = true;
        console.log('Viewing user details:', user);
    }

    editUser(user: UserDTO): void {
        this.selectedUser = user;
        this.editForm.patchValue({
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            role: user.role,
            classId: user.classId
        });
        this.isEditModalOpen = true;
    }

    saveUser(): void {
        if (this.editForm.valid && this.selectedUser) {
            const updatedUser = { ...this.selectedUser, ...this.editForm.value };
            
            // Mocking the update in the local array
            const index = this.users.findIndex(u => u.userId === updatedUser.userId);
            if (index !== -1) {
                this.users[index] = updatedUser;
                this.users = [...this.users]; // Trigger change detection
            }

            console.log('User updated successfully:', updatedUser);
            this.isEditModalOpen = false;
            this.selectedUser = null;
        }
    }
}
