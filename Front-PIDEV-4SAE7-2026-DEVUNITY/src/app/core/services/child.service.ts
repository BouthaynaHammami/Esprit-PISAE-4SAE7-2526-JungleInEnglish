import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Child } from '../models/child.model';

@Injectable({
  providedIn: 'root'
})
export class ChildService {
  private apiUrl = 'http://localhost:8081/languages/api/children';

  constructor(private http: HttpClient) {}

  getAllChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(this.apiUrl);
  }

  getChildById(id: number): Observable<Child> {
    return this.http.get<Child>(`${this.apiUrl}/${id}`);
  }

  addChild(child: Child): Observable<Child> {
    return this.http.post<Child>(`${this.apiUrl}/add`, child);
  }

  updateChild(child: Child): Observable<Child> {
    return this.http.put<Child>(`${this.apiUrl}/update`, child);
  }

  deleteChild(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getChildrenByParent(parentId: number): Observable<Child[]> {
    return this.http.get<Child[]>(`${this.apiUrl}/parent/${parentId}`);
  }

  getChildrenByLevel(levelId: number): Observable<Child[]> {
    return this.http.get<Child[]>(`${this.apiUrl}/level/${levelId}`);
  }
}
