import { DocumentReference } from '@angular/fire/firestore';
import { Game } from './game';

export interface Tournament {
  id: string;
  startDate: Date;
  complete: boolean;
  cartilage: { [from: string]: { to: string; bottom: boolean } };
  gridLocation: { [id: string]: { col: number; row: number } };
}
