import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Gatekeeper} from 'gatekeeper-client-sdk';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AppService {
    public user: any = null;
    id = Number(localStorage.getItem('id'));
    constructor(private router: Router, private toastr: ToastrService, private http: HttpClient) {}

    async loginByAuth({email, password}) {
        try {
            const token = await Gatekeeper.loginByAuth(email, password);
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Login success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async registerByAuth({email, password}) {
        try {
            const token = await Gatekeeper.registerByAuth(email, password);
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Register success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async loginByGoogle() {
        try {
            const token = await Gatekeeper.loginByGoogle();
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Login success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async registerByGoogle() {
        try {
            const token = await Gatekeeper.registerByGoogle();
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Register success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async loginByFacebook() {
        try {
            const token = await Gatekeeper.loginByFacebook();
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Login success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async registerByFacebook() {
        try {
            const token = await Gatekeeper.registerByFacebook();
            localStorage.setItem('token', token);
            await this.getProfile();
            this.router.navigate(['/']);
            this.toastr.success('Register success');
        } catch (error) {
            this.toastr.error(error.message);
        }
    }

    async getProfile() {
        try {
            this.http.get("http://localhost:8081/bnsp/api/users/" + this.id).subscribe(
                agent =>{
                  this.user = agent;
                  localStorage.removeItem('id');
                  console.log(this.user);
                }
              );

        } catch (error) {
            this.logout();
            throw error;
        }
    }

    getToken(): string | null{
      return localStorage.getItem('token');
    }

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('email');
        localStorage.removeItem('fonction');
        localStorage.removeItem('Caserne');
        localStorage.removeItem('idCaserne');
        setTimeout(() => {
          this.router.navigate(['/login']);
         // window.location.reload();
        }, 1000);

    }

    logoutToken() {
      localStorage.removeItem('token');
      localStorage.removeItem('email');
      localStorage.removeItem('fonction');
      localStorage.removeItem('Caserne');
      localStorage.removeItem('idCaserne');
      setTimeout(() => {
        this.router.navigate(['/login']);
        this.toastr.error('La session a expiré. Veuillez vous reconnecter!!!!!!')
      }, 1000);

  }
}
