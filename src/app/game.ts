import { Team } from './team';

export interface Game {
  id: number;
  startDate: Date;
  clockSeconds: number;
  period: string;
  topTeam: Team | null;
  bottomTeam: Team | null;
  topScore: number;
  bottomScore: number;
  winner: number;
}
