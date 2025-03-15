import { Routes } from '@angular/router';
import {EventsListComponent} from "./components/events/events-list/events-list.component";
import { EventDetailsComponent } from './components/events/event-details/event-details.component';
import { CreateEventComponent } from './components/events/create-event/create-event.component';

export const routes: Routes = [
  { path: 'events', component: EventsListComponent},
  { path: 'events/:id', component: EventDetailsComponent},
  { path: 'add-event', component: CreateEventComponent},
  { path: 'categories', component: EventsListComponent }, // Placeholder
];
