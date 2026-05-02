import { Injectable } from '@angular/core';
import { UserDTO } from '../../models/business-english.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

    private baseUrl = `${environment.apiUrl}/activities/api/users`;

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<UserDTO[]>(this.baseUrl);
  }

  getByEmail(email: string) {
    return this.http.get<UserDTO>(`${this.baseUrl}/email/${email}`);
  }

  getById(id:number) {
    return this.http.get<UserDTO>(`${this.baseUrl}/${id}`);
  }
  
}
