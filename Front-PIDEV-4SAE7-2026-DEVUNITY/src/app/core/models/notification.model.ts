export type NotificationType = 
  | 'APPLICANT_CREATED'
  | 'APPLICANT_UPDATED'
  | 'APPLICANT_STATUS_CHANGED'
  | 'INTERVIEW_SCHEDULED'
  | 'INTERVIEW_UPDATED'
  | 'INTERVIEW_CANCELLED'
  | 'INTERVIEW_REMINDER'
  | 'RECRUITMENT_CREATED'
  | 'RECRUITMENT_CLOSED'
  | 'RECRUITMENT_STATUS_CHANGED'
  | 'EMPLOYEE_APPROVED'
  | 'APPLICANT_ACCEPTED'
  | 'GENERAL';

export interface Notification {
  id?: number;
  title: string;
  message: string;
  type: NotificationType;
  recipientEmail: string;
  userId?: number;
  isRead: boolean;
  status?: string;
  createdAt: string | Date;
  readAt?: string | Date;
  relatedEntityId?: number;
  relatedEntityType?: string;
}
