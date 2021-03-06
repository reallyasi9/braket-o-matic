// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  firebaseConfig: {
    apiKey: 'add-me-later',
    authDomain: 'braket-o-matic-2.firebaseapp.com',
    projectId: 'braket-o-matic-2',
    storageBucket: 'braket-o-matic-2.appspot.com',
    messagingSenderId: '143706616878',
    appId: '1:143706616878:web:8979316344c377deab1bbf',
    measurementId: 'G-J695GJ13R9',

    authGuardFallback: '/tournament',
    authGuardLoggedInURL: '/tournament',
  },
  tournamentId: '2021-ncaam-div1',
  nGames: 63,
  nTeams: 64,
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
