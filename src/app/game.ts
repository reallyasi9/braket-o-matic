import { Team } from './team';

export interface Game {
  id: string;
  startDate?: Date;
  clockSeconds: number;
  period: string;
  topTeam?: Team;
  bottomTeam?: Team;
  topScore: number;
  bottomScore: number;
  winner?: number;
}
