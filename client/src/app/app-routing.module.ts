import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './auth/guard/auth-guard';
import { ResourceComponent } from 'src/app/resource/resource.component';
import { ManageComponent } from 'src/app/manage/manage.component';
import { ResourceGuard } from './auth/guard/resource-guard';
import { ManageGuard } from './auth/guard/manage-guard';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'resource', component: ResourceComponent, canActivate: [ResourceGuard] },
  { path: 'manage', component: ManageComponent, canActivate: [ManageGuard] },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
