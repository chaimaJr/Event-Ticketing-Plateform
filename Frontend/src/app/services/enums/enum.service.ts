import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../../../main';

@Injectable({
  providedIn: 'root'
})
export class EnumService {
  private baseUrl = `${API_URL}/enums`;

  constructor(private http: HttpClient) { }

  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/categories`);
  }
}
