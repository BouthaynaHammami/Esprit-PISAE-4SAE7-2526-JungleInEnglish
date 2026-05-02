export type EventStatus =
  | 'SCHEDULED'
  | 'REGISTRATION_OPEN'
  | 'CLOSED'
  | 'CANCELED'      // ← était 'CANCELLED' (2L), maintenant 1L
  | 'COMPLETED';

export interface EventPayload {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  location: string;
  capacity: number;
  status: EventStatus;
}