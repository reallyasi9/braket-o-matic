import { NgModule } from '@angular/core';
import { UserLoginComponent } from './user-login/user-login.component';
import { UserRegisterComponent } from './user-register/user-register.component';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';
import { TournamentComponent } from './tournament/tournament.component';
import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';

const TOURNAMENT_PATH: string = 'tournament';
const PICKEM_PATH: string = 'pickem';
const LOGIN_PATH: string = 'login';
const REGISTER_PATH: string = 'register';

const routes: Routes = [
  { path: '', redirectTo: 'tournament', pathMatch: 'full' },
  { path: TOURNAMENT_PATH, component: TournamentComponent },
  { path: LOGIN_PATH, component: UserLoginComponent },
  { path: REGISTER_PATH, component: UserRegisterComponent },
  { path: PICKEM_PATH, component: PickemTournamentComponent },
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
