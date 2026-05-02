import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
export interface AnswerDTO {
  questionId: number;
  selectedAnswer: string;
}

export interface TestResultDTO {
  score: number;
  earnedPoints: number;
  totalPoints: number;
  passed: boolean;
  level?: string;
  message?: string;
  qrCode?: string;
  certificateNumber?: string;
}

@Injectable({ providedIn: 'root' })
export class CertificationService {

private api = 'http://localhost:8081/academics/api/certification';
  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}


  private getEmail(): string {

    const email = this.authService.getUserEmail();

    if (!email) {
      throw new Error('User email not found');
    }

    return email;
  }

  
  //  ADMIN
  

  getQuestions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/questions/all`);
  }

  addQuestion(q: any): Observable<any> {
    return this.http.post<any>(`${this.api}/questions/add`, q);
  }

  updateQuestion(id: number, q: any): Observable<any> {
    return this.http.put<any>(`${this.api}/questions/update/${id}`, q);
  }

  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/questions/delete/${id}`);
  }

  getAllSessions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/sessions/all`);
  }

  getSuspiciousSessions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/sessions/suspicious`);
  }

  getAllCertificates(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/certificates/all`);
  }

  // =====================================================
  // ===================== STUDENT =======================
  // =====================================================

  getStudentQuestions(): Observable<any[]> {

    const email = this.getEmail();

    return this.http.get<any[]>(
      `${this.api}/student/questions?email=${email}`
    );
  }

  getStudentCertificates(): Observable<any[]> {

    const email = this.getEmail();

    return this.http.get<any[]>(
      `${this.api}/student/certificates?email=${email}`
    );
  }

  submitStudentExam(payload: AnswerDTO[]): Observable<TestResultDTO> {

    const email = this.getEmail();

    return this.http.post<TestResultDTO>(
      `${this.api}/student/submit?email=${email}`,
      payload
    );
  }

  reportTabViolation(): Observable<void> {

    const email = this.getEmail();

    return this.http.post<void>(
      `${this.api}/student/tab-violation?email=${email}`,
      {}
    );
  }

  getExamStatus(): Observable<boolean> {

    const email = this.getEmail();

    return this.http.get<boolean>(
      `${this.api}/student/status?email=${email}`
    );
  }
}