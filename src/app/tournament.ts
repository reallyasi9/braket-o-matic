import { Game } from './game';

export interface Tournament {
  id: string;
  startDate: Date;
  complete: boolean;
  games: { [id: string]: Game };
  cartilage: { [from: string]: { to: string; bottom: boolean } };
  gridLocation: { [id: string]: { col: number; row: number } };
}
