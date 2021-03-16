import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';
import { TournamentComponent } from './tournament/tournament.component';
import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';
import { LoggedInGuard } from 'ngx-auth-firebaseui';
import { TeamEditorComponent } from './team-editor/team-editor.component';
import { canActivate, hasCustomClaim } from '@angular/fire/auth-guard';

export const TOURNAMENT_PATH: string = 'tournament';
export const PICKEM_PATH: string = 'pickem';
export const TEAM_EDIT_PATH: string = 'teamedit';

const adminOnly = () => hasCustomClaim('admin');

const routes: Routes = [
  { path: '', redirectTo: 'tournament', pathMatch: 'full' },
  { path: TOURNAMENT_PATH, component: TournamentComponent },
  { path: PICKEM_PATH, component: PickemTournamentComponent, canActivate: [LoggedInGuard] },
  { path: TEAM_EDIT_PATH, component: TeamEditorComponent, ...canActivate(adminOnly) },
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
