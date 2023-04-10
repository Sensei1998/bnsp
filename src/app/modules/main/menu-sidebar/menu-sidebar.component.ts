import {AppState} from '@/store/state';
import {UiState} from '@/store/ui/state';
import {Component, HostBinding, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppService} from '@services/app.service';
import {Observable} from 'rxjs';

const BASE_CLASSES = 'main-sidebar elevation-4';
@Component({
    selector: 'app-menu-sidebar',
    templateUrl: './menu-sidebar.component.html',
    styleUrls: ['./menu-sidebar.component.scss']
})
export class MenuSidebarComponent implements OnInit {
    @HostBinding('class') classes: string = BASE_CLASSES;
    public ui: Observable<UiState>;
    public user;
    public menu = MENU;

    constructor(
        public appService: AppService,
        private store: Store<AppState>
    ) {}

    ngOnInit() {
        this.ui = this.store.select('ui');
        this.ui.subscribe((state: UiState) => {
            this.classes = `${BASE_CLASSES} ${state.sidebarSkin}`;
        });
        this.user = this.appService.user;
    }
}

export const MENU = [
    {
        name: 'Dashboard',
        iconClasses: 'fas fa-tachometer-alt',
        path: ['/']
    },
    {
        name: 'Listes des fiches',
        iconClasses: 'fas fa-folder',
        children: [
            {
                name: 'Créer une Intervention ',
                iconClasses: 'far fa-address-book',
                path: ['/sub-menu-1']
            },
            {
                name: 'Listes des interventions',
                iconClasses: 'fas fa-file',
                path: ['/sub-menu-2']
            },
            {
              name: 'Ajouter une Compagnie',
              iconClasses: 'fas fa-list',
              path: ['/sub-menu2']
            },
            {
              name: 'suivie',
              iconClasses: 'fas fa-forward',
              path: ['/suivi']
          }
        ]
    },
    {
      name: 'Admin Fonction',
      iconClasses: 'fas fa-folder',
      children: [
          {
              name: 'Agent ',
              iconClasses: 'far fa-address-book',
              path: ['/c_agent']
          },
          {
              name: 'Caserne',
              iconClasses: 'fas fa-home',
              path: ['/c_caserne']
          },
          {
            name: 'Engin',
            iconClasses: 'fas fa-truck',
            path: ['/c_engin']
          },
          {
            name: 'Equipe',
            iconClasses: 'fas fa-users',
            path: ['/c_equipe']
        }
      ]
  }
];
