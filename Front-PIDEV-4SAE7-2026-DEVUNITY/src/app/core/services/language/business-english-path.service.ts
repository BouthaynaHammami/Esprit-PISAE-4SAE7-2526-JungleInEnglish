import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BusinessEnglishPath } from '../../models/business-english.model';
import { environment } from '../../../../environments/environment';


@Injectable({ providedIn: 'root' })
export class BusinessEnglishPathService {
  private base = `${environment.apiUrl}/languages/api/paths`;

  constructor(private http: HttpClient) { }

  getAll() { return this.http.get<BusinessEnglishPath[]>(this.base); }
  getById(id: number) { return this.http.get<BusinessEnglishPath>(`${this.base}/${id}`); }
  create(body: BusinessEnglishPath) { return this.http.post<BusinessEnglishPath>(this.base, body); }
  update(id: number, body: BusinessEnglishPath) { return this.http.put<BusinessEnglishPath>(`${this.base}/${id}`, body); }
  delete(id: number) { return this.http.delete<void>(`${this.base}/${id}`); }
}