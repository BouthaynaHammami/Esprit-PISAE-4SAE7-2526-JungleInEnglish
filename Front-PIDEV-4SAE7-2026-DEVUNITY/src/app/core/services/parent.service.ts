import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parent } from '../models/parent.model';

@Injectable({
  providedIn: 'root'
})
export class ParentService {
  private apiUrl = 'http://localhost:8087/language/api/parents';

  constructor(private http: HttpClient) {}

  getAllParents(): Observable<Parent[]> {
    return this.http.get<Parent[]>(this.apiUrl);
  }

  getParentById(id: number): Observable<Parent> {
    return this.http.get<Parent>(`${this.apiUrl}/${id}`);
  }

  addParent(parent: Parent): Observable<Parent> {
    return this.http.post<Parent>(`${this.apiUrl}/add`, parent);
  }

  updateParent(parent: Parent): Observable<Parent> {
    return this.http.put<Parent>(`${this.apiUrl}/update`, parent);
  }

  deleteParent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getParentByUserId(userId: number): Observable<Parent> {
    return this.http.get<Parent>(`${this.apiUrl}/user/${userId}`);
  }
}
