import {
    Component,
    OnInit,
    OnDestroy,
    Renderer2,
    HostBinding
} from '@angular/core';
import {UntypedFormGroup, UntypedFormControl, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AppService} from '@services/app.service';
import { HttpClient } from '@angular/common/http';
import { Login } from '@/model/login.model';
import { Router } from '@angular/router';
import { Caserne } from '@/model/Caserne.model';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
    @HostBinding('class') class = 'login-box';
    public loginForm: UntypedFormGroup;
    public isAuthLoading = false;
    public isGoogleLoading = false;
    public isFacebookLoading = false;


    constructor(
        private renderer: Renderer2,
        private toastr: ToastrService,
        private appService: AppService,
        private http: HttpClient,
        private router: Router
    ) {}

    ngOnInit() {
        this.renderer.addClass(
            document.querySelector('app-root'),
            'login-page'
        );
        this.loginForm = new UntypedFormGroup({
            email: new UntypedFormControl(null, [Validators.required, Validators.email]),
            password: new UntypedFormControl(null, [Validators.required, Validators.minLength(8)])
        });
    }

    async loginByAuth() {
        if (this.loginForm.valid) {
            this.isAuthLoading = true;
            let login = {
              identifier: this.loginForm.get('email').value,
              password: this.loginForm.get('password').value
            }
            this.http.post("http://localhost:8081/bnsp/api/users/login", login).subscribe(
              (data: Login) =>{
                this.saveToken(data.token);
                this.saveFonction(data.agent.currentFunction);
                this.saveCaserne(data.agent.caserneId);
                this.saveEmail(data.agent.email);

                this.router.navigate(['/']);
                this.toastr.success('Login success');
              }
            )
            //await this.appService.loginByAuth(this.loginForm.value);
            this.isAuthLoading = false;
        } else {
          window.location.reload();
            this.toastr.error('Email ou Mot de passe incorrect!');
        }
    }

    // async loginByGoogle() {
    //     this.isGoogleLoading = true;
    //     await this.appService.loginByGoogle();
    //     this.isGoogleLoading = false;
    // }

    // async loginByFacebook() {
    //     this.isFacebookLoading = true;
    //     await this.appService.loginByFacebook();
    //     this.isFacebookLoading = false;
    // }

    async saveToken(token: string){
      localStorage.setItem('token', token);
    }

    async saveFonction(fonction: string){
      localStorage.setItem('fonction', fonction);
    }

    async saveCaserne(id: number){
      this.http.get("http://localhost:8081/bnsp/api" + "/casernes/" + id).subscribe(
        (data: Caserne) => {
          localStorage.setItem('Caserne', data.name);
          localStorage.setItem('idCaserne', (data.id).toString());
        }
      );


    }
    async saveEmail(email: string){
      localStorage.setItem('email', email);
    }



    ngOnDestroy() {
        this.renderer.removeClass(
            document.querySelector('app-root'),
            'login-page'
        );
    }
}
