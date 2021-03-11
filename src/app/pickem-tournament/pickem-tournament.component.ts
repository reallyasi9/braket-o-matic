import { Component, OnInit } from '@angular/core';
import { generateTournament } from '../mock-tournament';
import { Tournament } from '../tournament';

@Component({
  selector: 'app-pickem-tournament',
  templateUrl: './pickem-tournament.component.html',
  styleUrls: ['./pickem-tournament.component.css']
})
export class PickemTournamentComponent implements OnInit {
  // TODO: differentiate this from TournamentComponent
  tournament: Tournament = generateTournament(2021);
  rounds: number = 5;
  gameColumns: Array<number[]> = [
    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
    [32, 33, 34, 35, 36, 37, 38, 39],
    [48, 49, 50, 51],
    [56, 57],
    [60, 62, 61],
    [58, 59],
    [52, 53, 54, 55],
    [40, 41, 42, 43, 44, 45, 46, 47],
    [16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
  ];

  constructor() {}

  ngOnInit(): void {
    // this.tournament = generateTournament(2021);
  }

  columnToRound(col: number): number {
    return Math.min(col, this.gameColumns.length - col - 1);
  }

  // Return a style for the game container based on the round number.
  gridLocation(column: number, row: number) {
    const midCol = Math.floor(this.gameColumns.length / 2);
    const round = this.columnToRound(column);
    const startRow = (column == midCol) ? 2 ** (round - 1) - 1 : 2 ** round - 1;
    const rowStride = (column == midCol) ? 2 ** (round - 1) : 2 ** (round + 1);
    return {
      gridColumn: `${column + 1} / span 1`,
      gridRow: `${startRow + row * rowStride + 1} / span 2`,
    };
  }

}
