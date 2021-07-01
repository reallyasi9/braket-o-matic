import { Team } from './team';

const STATES: string[] = [
  'Alabama',
  'Alaska',
  'Arizona',
  'Arkansas',
  'California',
  'Colorado',
  'Connecticut',
  'Delaware',
  'Florida',
  'Georgia',
  'Hawaii',
  'Idaho',
  'Illinois',
  'Indiana',
  'Iowa',
  'Kansas',
  'Kentucky',
  'Louisiana',
  'Maine',
  'Maryland',
  'Massachusetts',
  'Michigan',
  'Minnesota',
  'Mississippi',
  'Missouri',
  'Montana',
  'Nebraska',
  'Nevada',
  'New Hampshire',
  'New Jersey',
  'New Mexico',
  'New York',
  'Carolina',
  'Dakota',
  'Ohio',
  'Oklahoma',
  'Oregon',
  'Pennsylvania',
  'Rhode Island',
  'Tennessee',
  'Texas',
  'Utah',
  'Vermont',
  'Virginia',
  'Washington',
  'West Virginia',
  'Wisconsin',
  'Wyoming',
];

const PREFIXES: string[] = ['Northern', 'Eastern', 'Western', 'Southern', ''];

const SUFFIXES: string[] = ['State', 'Tech', 'A&M', 'University', ''];

export function randomTeamName(): string {
  const state = STATES[Math.floor(Math.random() * STATES.length)];
  const prefix = PREFIXES[Math.floor(Math.random() * PREFIXES.length)];
  const suffix = SUFFIXES[Math.floor(Math.random() * SUFFIXES.length)];
  return (prefix + ' ' + state + ' ' + suffix).trim()
}

export function randomColors(): string[] {
  const color1 = (((1 << 24) * Math.random()) | 0);
  const color2 = 0xffffff - color1;
  return ['#' + color1.toString(16).padStart(6, '0'), '#' + color2.toString(16).padStart(6, '0')];
}

export function generateTeam(id?: string): Team {
  const teamId = (!!id) ? id : Math.random().toString();
  const name = randomTeamName();
  const colors = randomColors();
  const primary = colors[0];
  const accent = colors[1];
  const seed = Math.ceil(Math.random() * 16);
  return {
    id: teamId,
    name: name,
    primaryColor: primary,
    accentColor: accent,
    active: true,
    seed: seed,
  };
}
