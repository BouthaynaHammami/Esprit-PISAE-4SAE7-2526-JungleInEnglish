export interface ChatMessage {
  sender: string;
  content: string;
  type: 'JOIN' | 'CHAT' | 'LEAVE' | 'REACTION';
  timestamp?: Date;
  topicId?: number;
  reactions?: MessageReaction[];
  messageId?: string; // Unique identifier for the message
}

export interface MessageReaction {
  emoji: string;
  user: string;
}

export interface ChatUser {
  username: string;
  connected: boolean;
}
