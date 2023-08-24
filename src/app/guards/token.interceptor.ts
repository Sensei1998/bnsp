import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AppService } from '@services/app.service';
import { ToastrService } from 'ngx-toastr';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private service: AppService, private toastr: ToastrService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.service.getToken();

    if(token !== null){
      let clone = request.clone({
        headers:  request.headers.set('Authorization', 'Bearer ' + token)
      })
      return next.handle(clone).pipe(
        catchError(error =>{
          console.log(error);
          if(error.status === 500){
            this.service.logoutToken();
          }
          return throwError('La session a expiré. Veuillez vous reconnecter!!!!!!')
        })
      )
    }

    return next.handle(request);
  }
}

export const TokenInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: TokenInterceptor,
  multi: true
}

