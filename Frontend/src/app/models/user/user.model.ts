export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  role: 'USER' | 'ORGANIZER' | 'ADMIN';
}
