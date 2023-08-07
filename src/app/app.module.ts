import { NgModule } from '@angular/core';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireAuthModule, USE_EMULATOR as USE_AUTH_EMULATOR } from '@angular/fire/compat/auth';
import { USE_EMULATOR as USE_FIRESTORE_EMULATOR } from '@angular/fire/compat/firestore'
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
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

import { MatLegacyButtonModule as MatButtonModule } from '@angular/material/legacy-button';
import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatLegacyListModule as MatListModule } from '@angular/material/legacy-list';
import { MatLegacyProgressSpinnerModule as MatProgressSpinnerModule } from '@angular/material/legacy-progress-spinner';
import { MatStepperModule } from '@angular/material/stepper';
import { MatLegacySelectModule as MatSelectModule } from '@angular/material/legacy-select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatLegacySlideToggleModule as MatSlideToggleModule } from '@angular/material/legacy-slide-toggle';
import { MatLegacyRadioModule as MatRadioModule } from '@angular/material/legacy-radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatLegacyFormFieldModule as MatFormFieldModule } from '@angular/material/legacy-form-field';
import { MatLegacyInputModule as MatInputModule } from '@angular/material/legacy-input';
import { MatIconModule } from '@angular/material/icon';
import { MatLegacyCheckboxModule as MatCheckboxModule } from '@angular/material/legacy-checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import {
  MatLegacySnackBarModule as MatSnackBarModule,
  MAT_LEGACY_SNACK_BAR_DEFAULT_OPTIONS as MAT_SNACK_BAR_DEFAULT_OPTIONS,
} from '@angular/material/legacy-snack-bar';


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
