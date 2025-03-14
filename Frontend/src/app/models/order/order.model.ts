import {User} from "../user/user.model";
import {Ticket} from "../ticket/ticket.model";

export class Order {
  id?: number;
  user?: User;
  tickets?: Ticket[];
  totalPrice?: number;
  status?: 'PENDING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';
  orderDate?: string;
}
