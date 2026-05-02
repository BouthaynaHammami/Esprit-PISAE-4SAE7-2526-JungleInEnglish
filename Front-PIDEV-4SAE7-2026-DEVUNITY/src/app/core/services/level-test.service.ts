// src/app/core/services/level-test.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Subject } from '../models/subject.model';
import { TestTentative } from '../models/test-tentative.model';

@Injectable({ providedIn: 'root' })
export class LevelTestService {

  private readonly subjectsUrl    = `${environment.apiUrl}/learners/api/api/subjects`;
  private readonly tentativesUrl  = `${environment.apiUrl}/learners/api/api/test-tentatives`;

  constructor(private http: HttpClient) {}

  // ─── Subjects ─────────────────────────────────────────
  getAllSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(this.subjectsUrl);
  }

  getSubjectById(id: number): Observable<Subject> {
    return this.http.get<Subject>(`${this.subjectsUrl}/${id}`);
  }

  createSubject(subject: Subject): Observable<Subject> {
    return this.http.post<Subject>(this.subjectsUrl, subject);
  }

  updateSubject(id: number, subject: Subject): Observable<Subject> {
    return this.http.put<Subject>(`${this.subjectsUrl}/${id}`, subject);
  }

  deleteSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.subjectsUrl}/${id}`);
  }

  // ─── Test Tentatives ───────────────────────────────────
  getAllTentatives(): Observable<TestTentative[]> {
    return this.http.get<TestTentative[]>(this.tentativesUrl);
  }

  getTentativeById(id: number): Observable<TestTentative> {
    return this.http.get<TestTentative>(`${this.tentativesUrl}/${id}`);
  }

  /** Returns submissions waiting for tutor correction */
  getSubmissionsForCorrection(): Observable<TestTentative[]> {
    return this.http.get<TestTentative[]>(`${this.tentativesUrl}/corrections`);
  }

  createTentative(t: TestTentative): Observable<TestTentative> {
    return this.http.post<TestTentative>(this.tentativesUrl, t);
  }

  updateTentative(id: number, t: TestTentative): Observable<TestTentative> {
    return this.http.put<TestTentative>(`${this.tentativesUrl}/${id}`, t);
  }

  deleteTentative(id: number): Observable<void> {
    return this.http.delete<void>(`${this.tentativesUrl}/${id}`);
  }

  // ─── Complete Test (Written + Oral) ─────────────────────
  saveCompleteTest(testData: any): Observable<any> {
    return this.http.post<any>(`${this.tentativesUrl}/complete`, testData);
  }

  saveOralTestSession(session: any): Observable<any> {
    return this.http.post<any>(`${this.tentativesUrl}/oral-session`, session);
  }
}
