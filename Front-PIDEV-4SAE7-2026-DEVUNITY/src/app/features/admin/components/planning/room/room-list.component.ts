import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Room } from '../../../../../core/models/room.model';
import { RoomService } from '../../../../../core/services/room.service';
import { RoomFormComponent } from './room-form.component';
import { RoomDetailComponent } from './room-detail.component';

@Component({
    selector: 'app-room-list',
    standalone: true,
    imports: [CommonModule, RoomFormComponent, RoomDetailComponent],
    templateUrl: './room-list.component.html'
})
export class RoomListComponent implements OnInit {
    rooms: Room[] = [];
    isLoading = true;
    error: string | null = null;
    successMessage: string | null = null;
    showForm = false;
    editingRoom: Room | null = null;
    viewingRoom: Room | null = null;

    constructor(private roomService: RoomService) { }

    ngOnInit(): void { this.load(); }

    load(): void {
        this.isLoading = true;
        this.error = null;
        this.roomService.getAll().subscribe({
            next: data => { this.rooms = data; this.isLoading = false; },
            error: () => { this.error = 'Failed to load rooms.'; this.isLoading = false; }
        });
    }

    openAdd(): void {
        this.editingRoom = null;
        this.viewingRoom = null;
        this.showForm = true;
    }

    openEdit(room: Room): void {
        this.viewingRoom = null;
        this.editingRoom = room;
        this.showForm = true;
    }

    openDetail(room: Room): void {
        this.showForm = false;
        this.viewingRoom = room;
        this.editingRoom = null;
    }

    onSaved(): void {
        this.showForm = false;
        this.editingRoom = null;
        this.load();
        this.flash('Room saved successfully!');
    }

    onCancelled(): void {
        this.showForm = false;
        this.editingRoom = null;
    }

    delete(room: Room): void {
        if (!confirm(`Delete Room ${room.name}? This cannot be undone.`)) return;
        this.roomService.delete(room.roomId!).subscribe({
            next: () => {
                this.rooms = this.rooms.filter(r => r.roomId !== room.roomId);
                if (this.viewingRoom?.roomId === room.roomId) this.viewingRoom = null;
                this.flash('Room deleted.');
            },
            error: () => { this.error = 'Failed to delete room.'; }
        });
    }

    private flash(msg: string): void {
        this.successMessage = msg;
        setTimeout(() => this.successMessage = null, 3000);
    }
}
