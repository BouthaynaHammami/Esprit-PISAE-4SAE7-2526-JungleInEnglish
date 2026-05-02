import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UnreadMessagesService {
  private unreadCounts = new BehaviorSubject<Map<number, number>>(new Map());
  
  public unreadCounts$ = this.unreadCounts.asObservable();

  constructor() {}

  incrementUnread(topicId: number): void {
    const currentCounts = this.unreadCounts.value;
    const currentCount = currentCounts.get(topicId) || 0;
    currentCounts.set(topicId, currentCount + 1);
    this.unreadCounts.next(new Map(currentCounts));
    console.log(`📬 Unread count for topic ${topicId}: ${currentCount + 1}`);
  }

  resetUnread(topicId: number): void {
    const currentCounts = this.unreadCounts.value;
    currentCounts.set(topicId, 0);
    this.unreadCounts.next(new Map(currentCounts));
    console.log(`✅ Reset unread count for topic ${topicId}`);
  }

  getUnreadCount(topicId: number): number {
    return this.unreadCounts.value.get(topicId) || 0;
  }

  getAllUnreadCounts(): Map<number, number> {
    return this.unreadCounts.value;
  }
}
