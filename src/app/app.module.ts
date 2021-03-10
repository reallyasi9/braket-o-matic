import { NgModule } from '@angular/core';
import { USE_EMULATOR as USE_AUTH_EMULATOR } from '@angular/fire/auth';
import { USE_EMULATOR as USE_FIRESTORE_EMULATOR } from '@angular/fire/firestore';
import { BrowserModule } from '@angular/platform-browser';
import { NgxAuthFirebaseUIModule } from 'ngx-auth-firebaseui';
import { environment } from 'src/environments/environment';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';

import { AppComponent } from './app.component';
import { UserButtonComponent } from './user-button/user-button.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserRegisterComponent } from './user-register/user-register.component';
import { AppRoutingModule } from './app-routing.module';
import { TeamComponent } from './team/team.component';
import { GameComponent } from './game/game.component';
import { TournamentComponent } from './tournament/tournament.component';



@NgModule({
  declarations: [
    AppComponent,
    UserButtonComponent,
    UserLoginComponent,
    UserRegisterComponent,
    TeamComponent,
    GameComponent,
    TournamentComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    NgxAuthFirebaseUIModule.forRoot(environment.firebaseConfig),
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatListModule,
    AppRoutingModule,
  ],
  providers: [
    {
      provide: USE_AUTH_EMULATOR,
      useValue: !environment.production ? ['localhost', 9099] : undefined,
    },
    {
      provide: USE_FIRESTORE_EMULATOR,
      useValue: !environment.production ? ['localhost', 8080] : undefined,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
