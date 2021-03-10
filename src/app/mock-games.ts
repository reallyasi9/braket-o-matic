import { Game } from './game';
import { generateTeam } from './mock-teams';
import { Team } from './team';

const PERIODS: string[] = ['Pregame', '1st', '2nd', 'OT', 'OT2', 'Final'];

export function generateGame(id: number, team1: Team | null, team2: Team | null): Game {

  const seconds = Math.floor(Math.random() * 60*20);
  const period = PERIODS[Math.floor(Math.random() * PERIODS.length)];
  const topScore = Math.floor(Math.random() * 100);
  const bottomScore = Math.floor(Math.random() * 100);
  const startDate = new Date();
  const extraTime = Math.random() * 60 * 60 - 60 * 60;
  startDate.setSeconds(startDate.getSeconds() + extraTime);
  const winner = (team1 && team2 && extraTime < 60 * 30) ? (topScore > bottomScore) ? 0 : 1 : -1;

  return {
    id: id,
    startDate: startDate,
    clockSeconds: seconds,
    period: period,
    topTeam: team1,
    bottomTeam: team2,
    topScore: Math.floor(Math.random() * 100),
    bottomScore: Math.floor(Math.random() * 100),
    winner: winner,
  };
}
