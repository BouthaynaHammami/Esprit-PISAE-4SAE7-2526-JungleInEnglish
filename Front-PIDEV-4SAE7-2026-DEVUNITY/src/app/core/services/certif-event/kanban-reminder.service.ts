import { Injectable, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { ReminderNotification } from '../../models/kanban-task.model';
import { environment } from '../../../../environments/environment';
declare var SockJS: any;
declare var Stomp: any;

const MAX_RETRIES = 3;

@Injectable({ providedIn: 'root' })
export class KanbanReminderService implements OnDestroy {

  private academicClient: any = null;
  private communityClient: any = null;
  private academicRetries = 0;
  private communityRetries = 0;

  /** Emits every reminder notification received from either service */
  readonly reminder$ = new Subject<ReminderNotification>();

  /**
   * Connect to both WebSocket endpoints and subscribe to the user's reminder topic.
   * Call this once when the user logs in or when the kanban board initializes.
   */
  connect(userId: number): void {
    if (!userId || userId === 0) {
      console.warn('[KanbanReminderService] Invalid userId, skipping WS connection');
      return;
    }
    console.log('[KanbanReminderService] Connecting for userId:', userId);
    this.academicRetries = 0;
    this.communityRetries = 0;
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

    const socket = new SockJS(environment.academicWsUrl);
    this.academicClient = Stomp.over(socket);
    this.academicClient.debug = null; // silence debug logs

    this.academicClient.connect({}, () => {
      this.academicRetries = 0;
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
      this.academicClient = null;
      this.academicRetries++;
      if (this.academicRetries > MAX_RETRIES) {
        console.warn(`[KanbanReminderService] Academic WS unavailable after ${MAX_RETRIES} attempts. Stopped retrying.`);
        return;
      }
      const delay = 5000 * Math.pow(2, this.academicRetries - 1);
      console.log(`[KanbanReminderService] Academic WS retry ${this.academicRetries}/${MAX_RETRIES} in ${delay / 1000}s`);
      setTimeout(() => this.connectAcademic(userId), delay);
    });
  }

  private connectCommunity(userId: number): void {
    if (this.communityClient) return;

    const socket = new SockJS(environment.communityWsUrl);
    this.communityClient = Stomp.over(socket);
    this.communityClient.debug = null; // silence debug logs

    this.communityClient.connect({}, () => {
      this.communityRetries = 0;
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
      this.communityClient = null;
      this.communityRetries++;
      if (this.communityRetries > MAX_RETRIES) {
        console.warn(`[KanbanReminderService] Community WS unavailable after ${MAX_RETRIES} attempts. Stopped retrying.`);
        return;
      }
      const delay = 5000 * Math.pow(2, this.communityRetries - 1);
      console.log(`[KanbanReminderService] Community WS retry ${this.communityRetries}/${MAX_RETRIES} in ${delay / 1000}s`);
      setTimeout(() => this.connectCommunity(userId), delay);
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
