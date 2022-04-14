import { NgModule } from '@angular/core';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule, USE_EMULATOR as USE_AUTH_EMULATOR } from '@angular/fire/compat/auth';
import { USE_EMULATOR as USE_FIRESTORE_EMULATOR } from '@angular/fire/compat/firestore'
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { firebase, firebaseui, FirebaseUIModule } from 'firebaseui-angular';
import { environment } from 'src/environments/environment';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
// import { GameComponent } from './game/game.component';
import { GameAdderComponent } from './game-adder/game-adder.component';
import { GameEditorComponent } from './game-editor/game-editor.component';
// import { PickemGameComponent } from './pickem-game/pickem-game.component';
// import { PickemTeamComponent } from './pickem-team/pickem-team.component';
// import { PickemTournamentComponent } from './pickem-tournament/pickem-tournament.component';
// import { TeamComponent } from './team/team.component';
import { TeamAdderComponent } from './team-adder/team-adder.component';
import { TeamEditorComponent } from './team-editor/team-editor.component';
// import { TournamentComponent } from './tournament/tournament.component';
import { TournamentAdderComponent } from './tournament-adder/tournament-adder.component';
import { TournamentEditorComponent } from './tournament-editor/tournament-editor.component';
import { TournamentStepperComponent } from './tournament-stepper/tournament-stepper.component';

import { OrderByPipe } from './utilities';

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
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import {
  MatSnackBarModule,
  MAT_SNACK_BAR_DEFAULT_OPTIONS,
} from '@angular/material/snack-bar';

const firebaseUiAuthConfig: firebaseui.auth.Config = {
  signInFlow: 'redirect',
  signInOptions: [
    firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    {
      requireDisplayName: true,
      provider: firebase.auth.EmailAuthProvider.PROVIDER_ID
    }
  ],
  tosUrl: "tos",
  privacyPolicyUrl: "privacy",
  credentialHelper: firebaseui.auth.CredentialHelper.GOOGLE_YOLO
};

@NgModule({
  declarations: [
    AppComponent,
    // GameComponent,
    GameAdderComponent,
    GameEditorComponent,
    // PickemGameComponent,
    // PickemTeamComponent,
    // PickemTournamentComponent,
    // TeamComponent,
    TeamAdderComponent,
    TeamEditorComponent,
    // TournamentComponent,
    TournamentAdderComponent,
    TournamentEditorComponent,
    TournamentStepperComponent,
    OrderByPipe,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireAuthModule,
    FirebaseUIModule.forRoot(firebaseUiAuthConfig),
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
  ],
  providers: [
    {
      provide: USE_AUTH_EMULATOR,
      useValue: !environment.production ? ['localhost', 9099] : undefined
    },
    {
      provide: USE_FIRESTORE_EMULATOR,
      useValue: !environment.production ? ['localhost', 8080] : undefined,
    },
    { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: { duration: 2500 } },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
