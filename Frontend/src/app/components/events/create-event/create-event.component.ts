import { EnumService } from './../../../services/enums/enum.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Event_ } from '../../../models/event/event_.model';
import { EventService } from '../../../services/event/event.service';

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './create-event.component.html',
  styleUrl: './create-event.component.css'
})
export class CreateEventComponent {
  event: Event_ = new Event_();
  categories: string[] = [];
  loading: boolean = false;
  error: string | null = null;

  constructor(private eventService: EventService, private enumService: EnumService) {}

  ngOnInit(): void {
    this.fetchCategories();
  }

  fetchCategories(): void {
    this.loading = true;
    this.enumService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load categories';
        this.loading = false;
        console.error('Error fetching categories:', err);
      }
    });
  }

  onSubmit() {
    this.eventService.create(this.event)
      .subscribe({
        next: (createdEvent) => {
          console.log('Event created:', createdEvent);
          alert('Event created successfully!');
          this.resetForm();
        },
        error: (err) => {
          console.error('Error creating event:', err);
          alert('Failed to create event: ' + err.message);
        }
      });
  }

  resetForm() {
    this.event = new Event_();
  }

}
