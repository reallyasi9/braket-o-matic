import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';
// import { TournamentComponent } from './tournament/tournament.component';
// import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';
import { TeamEditorComponent } from './team-editor/team-editor.component';
import {
  AngularFireAuthGuard,
  // canActivate,
  customClaims,
  // redirectUnauthorizedTo,
} from '@angular/fire/compat/auth-guard';
import { pipe } from 'rxjs';
import { map } from 'rxjs/operators';
import { TournamentEditorComponent } from './tournament-editor/tournament-editor.component';
import { TournamentStepperComponent } from './tournament-stepper/tournament-stepper.component';

export const TOURNAMENT_PATH: string = 'tournament';
export const PICKEM_PATH: string = 'pickem';
export const TEAM_EDIT_PATH: string = 'team-edit';
export const TOURNAMENT_EDIT_PATH: string = 'tournament-edit';
export const TOURNAMENT_BUILDER_PATH: string = 'tournament-builder';

// const redirectUnauthorized = () => redirectUnauthorizedTo([TOURNAMENT_PATH]);
const adminOnly = () =>
  pipe(
    customClaims,
    map((claims) => (claims.role === 'admin' ? true : [TOURNAMENT_PATH]))
  );

const routes: Routes = [
  // { path: '', redirectTo: TOURNAMENT_PATH, pathMatch: 'full' },
  // { path: TOURNAMENT_PATH, component: TournamentComponent },
  // {
  //   path: PICKEM_PATH,
  //   component: PickemTournamentComponent,
  //   ...canActivate(redirectUnauthorized),
  // },
  {
    path: TOURNAMENT_BUILDER_PATH,
    component: TournamentStepperComponent,
    canActivate: [AngularFireAuthGuard],
    data: { authGuardPipe: adminOnly },
  },
  {
    path: TEAM_EDIT_PATH,
    component: TeamEditorComponent,
    canActivate: [AngularFireAuthGuard],
    data: { authGuardPipe: adminOnly },
  },
  {
    path: TOURNAMENT_EDIT_PATH,
    component: TournamentEditorComponent,
    canActivate: [AngularFireAuthGuard],
    data: { authGuardPipe: adminOnly },
  },
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(
      routes,
      { enableTracing: !environment.production } // <-- debugging purposes only
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
