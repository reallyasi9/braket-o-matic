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

export function generateTeam(id: number): Team {
  const state = STATES[Math.floor(Math.random() * STATES.length)];
  const prefix = PREFIXES[Math.floor(Math.random() * PREFIXES.length)];
  const suffix = SUFFIXES[Math.floor(Math.random() * SUFFIXES.length)];
  const primary =
    '#' + (((1 << 24) * Math.random()) | 0).toString(16).padStart(6, '0');
  const accent =
    '#' + (((1 << 24) * Math.random()) | 0).toString(16).padStart(6, '0');
  return {
    id: id,
    name: (prefix + ' ' + state + ' ' + suffix).trim(),
    primaryColor: primary,
    accentColor: accent,
  };
}
