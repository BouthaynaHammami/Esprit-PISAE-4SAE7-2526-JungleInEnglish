import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Room } from '../../../../../core/models/room.model';

@Component({
    selector: 'app-room-detail',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './room-detail.component.html'
})
export class RoomDetailComponent {
    @Input() room!: Room;
    @Output() closed = new EventEmitter<void>();
    @Output() edit = new EventEmitter<Room>();
}
