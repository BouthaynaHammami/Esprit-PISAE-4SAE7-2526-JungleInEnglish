import { Injectable, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { ReminderNotification } from '../../models/kanban-task.model';
declare var SockJS: any;
declare var Stomp: any;

@Injectable({ providedIn: 'root' })
export class KanbanReminderService implements OnDestroy {

  private academicClient: any = null;
  private communityClient: any = null;

  /** Emits every reminder notification received from either service */
  readonly reminder$ = new Subject<ReminderNotification>();

  /**
   * Connect to both WebSocket endpoints and subscribe to the user's reminder topic.
   * Call this once when the user logs in or when the kanban board initializes.
   */
  connect(userId: number): void {
    console.log('[KanbanReminderService] Connecting for userId:', userId);
    this.connectAcademic(userId);
    this.connectCommunity(userId);
  }

  /** Disconnect all WebSocket connections. */
  disconnect(): void {
    this.disconnectClient(this.academicClient);
    this.disconnectClient(this.communityClient);
    this.academicClient = null;
    this.communityClient = null;
  }

  ngOnDestroy(): void {
    this.disconnect();
  }

  // ── Private helpers ────────────────────────────

  private connectAcademic(userId: number): void {
    if (this.academicClient) return;

    // Connect directly to Academic service (port 8086) — bypasses gateway WS issues
    const socket = new SockJS('http://localhost:8086/academics/api/ws-reminders');
    this.academicClient = Stomp.over(socket);
    this.academicClient.debug = null; // silence debug logs

    this.academicClient.connect({}, () => {
      this.academicClient.subscribe(
        `/topic/reminders/${userId}`,
        (message: any) => {
          if (message.body) {
            const notification: ReminderNotification = JSON.parse(message.body);
            this.reminder$.next(notification);
          }
        }
      );
    }, (error: any) => {
      console.error('[KanbanReminderService] Academic WS error:', error);
      // Reconnect after 5s
      setTimeout(() => {
        this.academicClient = null;
        this.connectAcademic(userId);
      }, 5000);
    });
  }

  private connectCommunity(userId: number): void {
    if (this.communityClient) return;

    // Connect directly to Community service (port 8084) — bypasses gateway WS issues
    const socket = new SockJS('http://localhost:8084/communities/api/ws-reminders');
    this.communityClient = Stomp.over(socket);
    this.communityClient.debug = null; // silence debug logs

    this.communityClient.connect({}, () => {
      console.log('[KanbanReminderService] Community WS connected for user', userId);
      this.communityClient.subscribe(
        `/topic/reminders/${userId}`,
        (message: any) => {
          console.log('[KanbanReminderService] Received reminder:', message.body);
          if (message.body) {
            const notification: ReminderNotification = JSON.parse(message.body);
            this.reminder$.next(notification);
          }
        }
      );
    }, (error: any) => {
      console.error('[KanbanReminderService] Community WS error:', error);
      // Reconnect after 5s
      setTimeout(() => {
        this.communityClient = null;
        this.connectCommunity(userId);
      }, 5000);
    });
  }

  private disconnectClient(client: any): void {
    if (client && client.connected) {
      try {
        client.disconnect();
      } catch (e) {
        // ignore
      }
    }
  }
}
