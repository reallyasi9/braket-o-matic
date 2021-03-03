import { Team } from "./team";

export interface Game {
    id: number;
    startDate: Date;
    teams: Team[];
    scores: number[];
    complete: boolean;
    winner: Team | undefined;
    loser: Team | undefined;
}
