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
  selectedFile?: File;
  previewUrl: string | undefined;


  constructor(private eventService: EventService, private enumService: EnumService) {}

  ngOnInit(): void {
    this.event.date = '2025-03-20';
    this.event.time = '22:00';
    this.event.category = 'OTHER'
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

  // Handle file selection
  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewUrl = e.target.result; // Set the preview URL
      };
      reader.readAsDataURL(file);
    } else {
      this.selectedFile = undefined; // Clear file if no file
      this.previewUrl = undefined; // Clear preview if no file
    }
  }


  onSubmit() {
    // if(this.selectedFile == null) {
    //   this.selectedFile = null
    // }
    this.eventService.create(this.event, this.selectedFile)
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
    this.event.date = '2025-03-20';
    this.event.time = '22:00';
    this.event.category = 'OTHER'
  }

}
