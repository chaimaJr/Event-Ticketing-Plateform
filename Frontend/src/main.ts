import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

export const API_URL = 'http://localhost:8080/api';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
