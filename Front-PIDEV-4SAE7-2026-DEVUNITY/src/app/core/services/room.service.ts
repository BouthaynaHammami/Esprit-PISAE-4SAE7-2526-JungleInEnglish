import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Room } from '../models/room.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class RoomService {

    private readonly BASE = `${environment.apiUrl}/activities/api/rooms`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Room[]> {
        return this.http.get<Room[]>(`${this.BASE}/all`);
    }

    getById(id: number): Observable<Room> {
        return this.http.get<Room>(`${this.BASE}/${id}`);
    }

    getByName(name: string): Observable<Room> {
        return this.http.get<Room>(`${this.BASE}/name/${encodeURIComponent(name)}`);
    }

    getAvailable(): Observable<Room[]> {
        return this.http.get<Room[]>(`${this.BASE}/available`);
    }

    getByLevel(level: number): Observable<Room[]> {
        return this.http.get<Room[]>(`${this.BASE}/level/${level}`);
    }

    getByMinCapacity(min: number): Observable<Room[]> {
        return this.http.get<Room[]>(`${this.BASE}/capacity/${min}`);
    }

    create(room: Room): Observable<Room> {
        return this.http.post<Room>(`${this.BASE}/add`, room);
    }

    update(id: number, room: Room): Observable<Room> {
        return this.http.put<Room>(`${this.BASE}/update/${id}`, room);
    }

    delete(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
    }
}
