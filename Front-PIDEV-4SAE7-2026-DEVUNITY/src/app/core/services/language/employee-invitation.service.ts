import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { EmployeeInvitation } from '../../models/business-english.model';
import { ActivationResponseDTO } from '../../models/business-english.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EmployeeInvitationService {
  private base = `${environment.apiUrl}/languages/api/invitations`;

  constructor(private http: HttpClient) { }

  getAll() { return this.http.get<EmployeeInvitation[]>(this.base); }
  getById(id: number) { return this.http.get<EmployeeInvitation>(`${this.base}/${id}`); }
  create(body: EmployeeInvitation) { return this.http.post<EmployeeInvitation>(this.base, body); }
  update(id: number, body: EmployeeInvitation) { return this.http.put<EmployeeInvitation>(`${this.base}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(this.base + '/' + id); }

  /* ============= ADMIN ============= */
  getAllPending(): Observable<EmployeeInvitation[]> {
    return this.http.get<EmployeeInvitation[]>(`${this.base}/admin/pending`);
  }

  getPendingByCompanyOffer(companyOfferId: number): Observable<EmployeeInvitation[]> {
    return this.http.get<EmployeeInvitation[]>(
      `${this.base}/admin/company-offer/${companyOfferId}/pending`
    );
  }

  approve(companyOfferId: number): Observable<EmployeeInvitation[]> {
    return this.http.post<EmployeeInvitation[]>(
      `${this.base}/admin/company-offer/${companyOfferId}/approve`,
      {}
    );
  }

  reject(companyOfferId: number): Observable<EmployeeInvitation[]> {
    return this.http.post<EmployeeInvitation[]>(
      `${this.base}/admin/company-offer/${companyOfferId}/reject`,
      {}
    );
  }

  updateEmails(companyOfferId: number, emails: string[]) {
    return this.http.put(
      `${this.base}/admin/company-offer/${companyOfferId}/emails`,
      emails
    );
  }

  // compatibility aliases (components expect these names)
  approveCompanyOffer(companyOfferId: number): Observable<EmployeeInvitation[]> {
    return this.approve(companyOfferId);
  }

  rejectCompanyOffer(companyOfferId: number): Observable<EmployeeInvitation[]> {
    return this.reject(companyOfferId);
  }

  /* ============= STUDENT ============= */

  activate(email: string, code: string): Observable<ActivationResponseDTO> {
    const params = new HttpParams()
      .set('email', email)
      .set('code', code);

    return this.http.post<ActivationResponseDTO>(
      `${this.base}/activate`,
      {},
      { params }
    );
  }
}