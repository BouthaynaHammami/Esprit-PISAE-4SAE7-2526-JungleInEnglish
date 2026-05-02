export interface Message {
  messageId?: number;
  content: string;
  sentAt?: Date;
  isRead?: boolean;
  senderId?: number;
  receiverId?: number;
  sender?: any;
  receiver?: any;
}
