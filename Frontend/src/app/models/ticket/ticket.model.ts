import {User} from "../user/user.model";
import {Event_} from "../event/event_.model";
import {Order} from "../order/order.model";

export class Ticket {
  id?: number;
  event?: Event_;
  user?: User;
  order?: Order;
  status?: 'BOOKED' | 'CANCELED' ;
}
