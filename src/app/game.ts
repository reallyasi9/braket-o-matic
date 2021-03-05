import { Team } from "./team";

export interface Game {
    id: number;
    startDate: Date;
    teams: Team[];
    scores: number[];
    winner: number;
}
