import { Room } from './room.model';
import { Schedule } from './schedule.model';

export interface RoomScheduleComplaint {
    complaintId?: number;
    subject: string;
    description: string;
    status: boolean;
    answer?: string;
    complainantEmail?: string;
    complainantRole?: string;
    room?: Room;
    schedule?: Schedule;
}

export interface RoomScheduleComplaintCreateRequest {
    subject: string;
    description: string;
    status?: boolean;
    complainantEmail?: string;
    complainantRole?: string;
}
