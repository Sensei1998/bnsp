import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppService } from '@services/app.service';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private service: AppService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.service.getToken();

    if(token !== null){
      let clone = request.clone({
        headers:  request.headers.set('Authorization', 'Bearer ' + token)
      })
      return next.handle(clone)
    }

    return next.handle(request);
  }
}

export const TokenInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: TokenInterceptor,
  multi: true
}

