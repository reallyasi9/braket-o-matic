import { Team } from "./team";

export interface Game {
    id: number;
    startDate: Date;
    clockSeconds: number;
    period: string;
    teams: Team[];
    scores: number[];
    winner: number;
}
