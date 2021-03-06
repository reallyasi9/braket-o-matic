import { NgModule } from '@angular/core';
import { USE_EMULATOR as USE_AUTH_EMULATOR } from '@angular/fire/auth';
import { USE_EMULATOR as USE_FIRESTORE_EMULATOR } from '@angular/fire/firestore';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatStepperModule } from '@angular/material/stepper';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxAuthFirebaseUIModule } from 'ngx-auth-firebaseui';
import { environment } from 'src/environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
// import { GameComponent } from './game/game.component';
// import { PickemGameComponent } from './pickem-game/pickem-game.component';
// import { PickemTeamComponent } from './pickem-team/pickem-team.component';
// import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';
// import { TeamComponent } from './team/team.component';
// import { TournamentComponent } from './tournament/tournament.component';
import { UserButtonComponent } from './user-button/user-button.component';
import { UserLoginComponent } from './user-login/user-login.component';
import { TeamEditorComponent } from './team-editor/team-editor.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { TournamentEditorComponent } from './tournament-editor/tournament-editor.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TournamentStepperComponent } from './tournament-stepper/tournament-stepper.component';
import { FormsModule } from '@angular/forms';
import { GameEditorComponent } from './game-editor/game-editor.component';
import { MatNativeDateModule } from '@angular/material/core';
import { TeamAdderComponent } from './team-adder/team-adder.component';
import {
  MatSnackBarModule,
  MAT_SNACK_BAR_DEFAULT_OPTIONS,
} from '@angular/material/snack-bar';
import { GameAdderComponent } from './game-adder/game-adder.component';
import { OrderByPipe } from './utilities';
import { TournamentAdderComponent } from './tournament-adder/tournament-adder.component';

@NgModule({
  declarations: [
    AppComponent,
    UserButtonComponent,
    UserLoginComponent,
    // TeamComponent,
    // GameComponent,
    // TournamentComponent,
    // PickemGameComponent,
    // PickemTeamComponent,
    // PickemTournamentComponent,
    TeamEditorComponent,
    TournamentEditorComponent,
    TournamentStepperComponent,
    GameEditorComponent,
    TeamAdderComponent,
    GameAdderComponent,
    OrderByPipe,
    TournamentAdderComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    NgxAuthFirebaseUIModule.forRoot(environment.firebaseConfig),
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatCheckboxModule,
    MatStepperModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatRadioModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatNativeDateModule,
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
    { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 2500 } },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
