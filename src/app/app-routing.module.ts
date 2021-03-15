import { NgModule } from '@angular/core';
import { UserLoginComponent } from './user-login/user-login.component';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';
import { TournamentComponent } from './tournament/tournament.component';
import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';
import { LoggedInGuard } from 'ngx-auth-firebaseui';

export const TOURNAMENT_PATH: string = 'tournament';
export const PICKEM_PATH: string = 'pickem';

const routes: Routes = [
  { path: '', redirectTo: 'tournament', pathMatch: 'full' },
  { path: TOURNAMENT_PATH, component: TournamentComponent },
  { path: PICKEM_PATH, component: PickemTournamentComponent, canActivate: [LoggedInGuard] },
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
