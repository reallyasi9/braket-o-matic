import { DocumentReference } from '@angular/fire/firestore';
import { Game } from './game';

export interface Tournament {
  id: string;
  name: string;
  startDate: Date;
  complete: boolean;
  roundValues: number[];
  payouts: number[];
  cartilage: { [from: string]: { to: string; bottom: boolean } };
  gridLocation: { [id: string]: { col: number; row: number } };
}
