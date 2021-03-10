import { Game } from "./game";

export interface Tournament {
    id: number;
    startDate: Date;
    complete: boolean;
    games: Game[];
}
