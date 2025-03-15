import {Component, OnInit} from '@angular/core';
import { Event_ } from '../../../models/event/event_.model';
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {EventService} from "../../../services/event/event.service";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-events-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './events-list.component.html',
  styleUrl: './events-list.component.css'
})
export class EventsListComponent{
  events? : Event_[];

  constructor(private eventService: EventService) {}
  ngOnInit() {
    this.getEventsList();
  }

  getEventsList() {
    this.eventService.getAll().subscribe({
      next: (data) => {
        this.events = data;
        console.log("Events List", this.events);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  



}
