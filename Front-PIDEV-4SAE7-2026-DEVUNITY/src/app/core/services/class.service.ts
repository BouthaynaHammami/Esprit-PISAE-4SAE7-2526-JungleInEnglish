import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClassEntity } from '../models/class.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ClassService {

    private readonly BASE = `${environment.apiUrl}/activities/api/classes`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<ClassEntity[]> {
        return this.http.get<ClassEntity[]>(`${this.BASE}/all`);
    }

    getById(id: number): Observable<ClassEntity> {
        return this.http.get<ClassEntity>(`${this.BASE}/${id}`);
    }

    getByName(name: string): Observable<ClassEntity> {
        return this.http.get<ClassEntity>(`${this.BASE}/name/${encodeURIComponent(name)}`);
    }

    getByLevel(level: string): Observable<ClassEntity[]> {
        return this.http.get<ClassEntity[]>(`${this.BASE}/level/${level}`);
    }

    create(cls: ClassEntity): Observable<ClassEntity> {
        return this.http.post<ClassEntity>(`${this.BASE}/add`, cls);
    }

    update(id: number, cls: ClassEntity): Observable<ClassEntity> {
        return this.http.put<ClassEntity>(`${this.BASE}/update/${id}`, cls);
    }

    delete(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.BASE}/delete/${id}`);
    }

    assignClass(userId: number, classId: number): Observable<ClassEntity> {
        return this.http.put<ClassEntity>(`${this.BASE}/assign/${userId}/${classId}`, {});
    }
}