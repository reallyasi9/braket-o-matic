import { NgModule } from '@angular/core';
import { UserLoginComponent } from './user-login/user-login.component';
import { UserRegisterComponent } from './user-register/user-register.component';
import { RouterModule, Routes } from '@angular/router';
import { environment } from 'src/environments/environment';

const LOGIN_PATH: string = 'login';
const REGISTER_PATH: string = 'register';

const routes: Routes = [
  { path: LOGIN_PATH, component: UserLoginComponent },
  { path: REGISTER_PATH, component: UserRegisterComponent },
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(
      routes,
      { enableTracing: !environment.production } // <-- debugging purposes only
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
