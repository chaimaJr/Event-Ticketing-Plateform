import { Injectable } from '@angular/core';
import {API_URL} from "../../../main";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Event_} from "../../models/event/event_.model";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = `${API_URL}/events`;

  constructor(private http: HttpClient) { }

  getAll() : Observable<Event_[]> {
    return this.http.get<Event_[]>(this.baseUrl);
  }

  getById(id: number) : Observable<Event_> {
    return this.http.get<Event_>(`${this.baseUrl}/${id}`);
  }

  create(event: Event_): Observable<Event_> {
    return this.http.post<Event_>(this.baseUrl, event);
  }
  // createEvent(event: Event_, banner?: File): Observable<Event_> {
  //   const formData = new FormData();
  //   formData.append('event', new Blob([JSON.stringify(event)], { type: 'application/json' }));
  //   if (banner) {
  //     formData.append('banner', banner, banner.name);
  //   }

  //   return this.http.post<Event_>(this.baseUrl, formData);
  // }




  update(id:number, event: Event_): Observable<Event_> {
    return this.http.put<Event_>(`${this.baseUrl}/${id}`, event);
  }

  cancel(id: number): Observable<Event_> {
    return this.http.put<Event_>(`${this.baseUrl}/${id}/cancel`, null);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

// -------- GET --------

  getSoldTicketsNb(id: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/${id}/sold-tickets`);
  }

  getEvent_sByLocation(location: string): Observable<Event_[]> {
    const params = new HttpParams().set('location', location);
    return this.http.get<Event_[]>(`${this.baseUrl}/search`, { params });
  }

  getAvailableEvents(): Observable<Event_[]> {
    return this.http.get<Event_[]>(`${this.baseUrl}/available`);
  }

//    ---------- Tickets ----------

  addSoldTicket(id: number): Observable<Event_> {
    return this.http.put<Event_>(`${this.baseUrl}/${id}/add-ticket`, null);
  }

  removeSoldTicket(id: number): Observable<Event_> {
    return this.http.put<Event_>(`${this.baseUrl}/${id}/remove-ticket`, null);
  }


}
