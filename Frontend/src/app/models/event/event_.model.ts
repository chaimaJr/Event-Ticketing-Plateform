export class Event_ {
  id?: number;
  name?: string;
  description?: string;
  category?: string;
  status?: 'ACTIVE' | 'CANCELED' | 'SOLD_OUT';
  date?: string;
  time?: string;
  location?: string;
  bannerUrl?: string;
  price?: number;
  totalTickets?: number;
  ticketsSold?: number;
}
