// src/app/core/services/recruitment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Recruitment, Interview, Applicant, UserDTO } from '../models/recruitment.model';

@Injectable({ providedIn: 'root' })
export class RecruitmentService {

  private readonly recruitmentsUrl = `${environment.apiUrl}/activities/api/api/recruitments`;
  private readonly interviewsUrl   = `${environment.apiUrl}/activities/api/api/interviews`;
  private readonly applicantsUrl   = `${environment.apiUrl}/activities/api/api/applicants`;

  constructor(private http: HttpClient) {}

  // ─── Recruitments ─────────────────────────────────────
  getAllRecruitments(): Observable<Recruitment[]> {
    return this.http.get<Recruitment[]>(this.recruitmentsUrl);
  }

  getRecruitmentById(id: number): Observable<Recruitment> {
    return this.http.get<Recruitment>(`${this.recruitmentsUrl}/${id}`);
  }

  createRecruitment(r: Recruitment): Observable<Recruitment> {
    return this.http.post<Recruitment>(this.recruitmentsUrl, r);
  }

  updateRecruitment(id: number, r: Recruitment): Observable<Recruitment> {
    return this.http.put<Recruitment>(`${this.recruitmentsUrl}/${id}`, r);
  }

  deleteRecruitment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.recruitmentsUrl}/${id}`);
  }

  // ─── Interviews ───────────────────────────────────────
  getAllInterviews(): Observable<Interview[]> {
    return this.http.get<Interview[]>(this.interviewsUrl);
  }

  getInterviewById(id: number): Observable<Interview> {
    return this.http.get<Interview>(`${this.interviewsUrl}/${id}`);
  }

  getInterviewsByRecruitment(recruitmentId: number): Observable<Interview[]> {
    return this.http.get<Interview[]>(`${this.interviewsUrl}/recruitment/${recruitmentId}`);
  }

  createInterview(i: Interview): Observable<Interview> {
    return this.http.post<Interview>(this.interviewsUrl, i);
  }

  updateInterview(id: number, i: Interview): Observable<Interview> {
    return this.http.put<Interview>(`${this.interviewsUrl}/${id}`, i);
  }

  deleteInterview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.interviewsUrl}/${id}`);
  }

  // ─── Applicants ───────────────────────────────────────
  getAllApplicants(): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(this.applicantsUrl);
  }

  getApplicantById(id: number): Observable<Applicant> {
    return this.http.get<Applicant>(`${this.applicantsUrl}/${id}`);
  }

  getApplicantsByRecruitment(recruitmentId: number): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(`${this.applicantsUrl}/recruitment/${recruitmentId}`);
  }

  getApplicantsByUser(userId: number): Observable<Applicant[]> {
    return this.http.get<Applicant[]>(`${this.applicantsUrl}/user/${userId}`);
  }

  getUserOfApplicant(applicantId: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.applicantsUrl}/${applicantId}/user`);
  }

  createApplicant(a: Applicant): Observable<Applicant> {
    return this.http.post<Applicant>(this.applicantsUrl, a);
  }

  updateApplicant(id: number, a: Applicant): Observable<Applicant> {
    return this.http.put<Applicant>(`${this.applicantsUrl}/${id}`, a);
  }

  deleteApplicant(id: number): Observable<void> {
    return this.http.delete<void>(`${this.applicantsUrl}/${id}`);
  }

  // ─── AI Analysis ──────────────────────────────────────
  analyzeCV(applicantId: number, recruitmentId?: number): Observable<any> {
    const options = recruitmentId 
      ? { params: { recruitmentId: recruitmentId.toString() } }
      : {};
    return this.http.post<any>(`${this.applicantsUrl}/${applicantId}/analyze`, null, options);
  }

  analyzeCVWithUrl(applicantId: number, recruitmentId: number, cvUrl: string): Observable<any> {
    const body = { cvUrl };
    const options = { params: { recruitmentId: recruitmentId.toString() } };
    return this.http.post<any>(`${this.applicantsUrl}/${applicantId}/analyze`, body, options);
  }
}
