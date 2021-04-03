import { DocumentReference } from '@angular/fire/firestore';
import { Game } from './game';
import { Team } from './team';

const PERIODS: string[] = ['Pregame', '1st', '2nd', 'OT', 'OT2', 'Final'];

export function generateGame(id: string, top?: DocumentReference<Team>, bottom?: DocumentReference<Team>): Game {

  const seconds = Math.floor(Math.random() * 60*20);
  const period = PERIODS[Math.floor(Math.random() * PERIODS.length)];
  const topScore = Math.floor(Math.random() * 100);
  const bottomScore = Math.floor(Math.random() * 100);
  const startDate = new Date();
  const extraTime = Math.random() * 60 * 60 - 60 * 60;
  startDate.setSeconds(startDate.getSeconds() + extraTime);
  const winner = (top && bottom && extraTime < 60 * 30) ? (topScore > bottomScore) ? 0 : 1 : -1;

  return {
    id: id,
    round: 0,
    startDate: startDate,
    clockSeconds: seconds,
    period: period,
    topTeam: top,
    bottomTeam: bottom,
    topScore: Math.floor(Math.random() * 100),
    bottomScore: Math.floor(Math.random() * 100),
    winner: winner,
  };
}
