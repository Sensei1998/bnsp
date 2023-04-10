import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MainComponent} from '@modules/main/main.component';
import {BlankComponent} from '@pages/blank/blank.component';
import {LoginComponent} from '@modules/login/login.component';
import {ProfileComponent} from '@pages/profile/profile.component';
import {RegisterComponent} from '@modules/register/register.component';
import {DashboardComponent} from '@pages/dashboard/dashboard.component';
import {AuthGuard} from '@guards/auth.guard';
import {NonAuthGuard} from '@guards/non-auth.guard';
import {ForgotPasswordComponent} from '@modules/forgot-password/forgot-password.component';
import {RecoverPasswordComponent} from '@modules/recover-password/recover-password.component';
import {MainMenuComponent} from '@pages/main-menu/main-menu.component';
import {SubMenuComponent} from '@pages/main-menu/sub-menu/sub-menu.component';
import { SubMenu2Component } from '@pages/main-menu/sub-menu2/sub-menu2.component';
import { SuiviComponent } from '@pages/suivi/suivi.component';
import { CreationAgentComponent } from '@pages/admin/creation-agent/creation-agent.component';
import { CreationCaserneComponent } from '@pages/admin/creation-caserne/creation-caserne.component';
import { CreationEnginComponent } from '@pages/admin/creation-engin/creation-engin.component';
import { CreationEquipeComponent } from '@pages/admin/creation-equipe/creation-equipe.component';

const routes: Routes = [
    {
        path: '',
        component: MainComponent,
        canActivate: [AuthGuard],
        canActivateChild: [AuthGuard],
        children: [
            {
                path: 'profile',
                component: ProfileComponent
            },
            {
                path: 'blank',
                component: BlankComponent
            },
            {
                path: 'sub-menu-1',
                component: SubMenuComponent
            },
            {
                path: 'sub-menu-2',
                component: BlankComponent
            },
            {
              path: 'sub-menu2',
              component: SubMenu2Component
            },
            {
              path: 'suivi',
              component: SuiviComponent
            },
            {
              path: 'c_agent',
              component: CreationAgentComponent
            },
            {
              path: 'c_caserne',
              component: CreationCaserneComponent
            },
            {
              path: 'c_engin',
              component: CreationEnginComponent
            },
            {
              path: 'c_equipe',
              component: CreationEquipeComponent
            },
            {
                path: '',
                component: DashboardComponent
            }
        ]
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'register',
        component: RegisterComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'forgot-password',
        component: ForgotPasswordComponent,
        canActivate: [NonAuthGuard]
    },
    {
        path: 'recover-password',
        component: RecoverPasswordComponent,
        canActivate: [NonAuthGuard]
    },
    {path: '**', redirectTo: ''}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {})],
    exports: [RouterModule]
})
export class AppRoutingModule {}
