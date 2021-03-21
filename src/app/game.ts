import { DocumentReference } from '@angular/fire/firestore';
import { Team } from './team';

export interface Game {
  id: string;
  startDate?: Date;
  clockSeconds: number;
  period: string;
  topTeam?: DocumentReference<Team>;
  bottomTeam?: DocumentReference<Team>;
  topScore: number;
  bottomScore: number;
  winner?: number;
}
