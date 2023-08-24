import { CanDeactivateComponent } from '@/model/can-deactivate.interface';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanDeactivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CanDesactivateGuard implements CanDeactivate<CanDeactivateComponent> {
  canDeactivate(
    component: CanDeactivateComponent
  ): boolean | Observable<boolean> {
    return component.canDeactivate();
  }

}
