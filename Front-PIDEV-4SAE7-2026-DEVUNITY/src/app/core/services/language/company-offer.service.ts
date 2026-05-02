import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { CompanyOffer, CompanyOfferRequestDTO, EmployeeInvitation } from '../../models/business-english.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyOfferService {

  private baseUrl = `${environment.apiUrl}/languages/api/company-offers`;

  constructor(private http: HttpClient) { }


  add(companyOffer: CompanyOffer): Observable<CompanyOffer> {
    return this.http.post<CompanyOffer>(this.baseUrl, companyOffer);
  }


  update(id: number, companyOffer: CompanyOffer): Observable<CompanyOffer> {
    return this.http.put<CompanyOffer>(`${this.baseUrl}/${id}`, companyOffer);
  }

  getAll(): Observable<CompanyOffer[]> {
    return this.http.get<CompanyOffer[]>(this.baseUrl);
  }

  getById(id: number): Observable<CompanyOffer> {
    return this.http.get<CompanyOffer>(`${this.baseUrl}/${id}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /* ================= COMPANY ================= */

  requestOffer(
    companyId: number,
    offerId: number,
    emails: string[]
  ): Observable<EmployeeInvitation[]> {
    return this.http.post<EmployeeInvitation[]>(
      `${this.baseUrl}/company/${companyId}/offer/${offerId}/request`,
      emails
    );
  }

  /* ================= ADMIN ================= */

  getAllRequestsForAdmin(): Observable<CompanyOfferRequestDTO[]> {
    return this.http.get<CompanyOfferRequestDTO[]>(
      `${this.baseUrl}/admin/requests`
    );
  }

  updatePaymentStatus(id: number, status: string): Observable<any> {
    return this.http.put(
      `${this.baseUrl}/admin/${id}/payment?status=${status}`,
      {}
    );
  }

  /* ================= STUDENT ================= */

  getStudentOffers(studentId: number): Observable<CompanyOffer[]> {
    return this.http.get<CompanyOffer[]>(`${this.baseUrl}/student/${studentId}`);
  }
}