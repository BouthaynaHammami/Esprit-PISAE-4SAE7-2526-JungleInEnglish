import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { Child } from '../../models/english-kids.model';

@Injectable({
  providedIn: 'root'
})
export class ChildService {
  private readonly apiUrl = `${environment.apiUrl}/language/api/children`;
  private currentChildSubject = new BehaviorSubject<Child | null>(null);
  public currentChild$ = this.currentChildSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAllChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(this.apiUrl);
  }

  getChildById(id: number): Observable<Child> {
    return this.http.get<Child>(`${this.apiUrl}/${id}`);
  }

  createChild(child: Child): Observable<Child> {
    return this.http.post<Child>(`${this.apiUrl}/add`, child);
  }

  updateChild(id: number, child: Child): Observable<Child> {
    return this.http.put<Child>(`${this.apiUrl}/${id}`, child);
  }

  deleteChild(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  setCurrentChild(child: Child): void {
    this.currentChildSubject.next(child);
    localStorage.setItem('currentChild', JSON.stringify(child));
  }

  getCurrentChild(): Child | null {
    const stored = localStorage.getItem('currentChild');
    if (stored) {
      const child = JSON.parse(stored);
      this.currentChildSubject.next(child);
      return child;
    }
    return this.currentChildSubject.value;
  }

  clearCurrentChild(): void {
    this.currentChildSubject.next(null);
    localStorage.removeItem('currentChild');
  }

  updateChildXp(childId: number, xpToAdd: number): Observable<Child> {
    return this.http.patch<Child>(`${this.apiUrl}/${childId}/xp`, { xp: xpToAdd }).pipe(
      tap(updatedChild => {
        const current = this.getCurrentChild();
        if (current && current.childId === childId) {
          this.setCurrentChild(updatedChild);
        }
      })
    );
  }
}
