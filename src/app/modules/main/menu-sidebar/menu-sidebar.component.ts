import { Agent } from '@/model/AgentLongin.model';
import {AppState} from '@/store/state';
import {UiState} from '@/store/ui/state';
import { HttpClient } from '@angular/common/http';
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
    email: string;
    public menu = MENU;
    public menu2 = MENU2;
    public menu3 = MENU3;


    id = Number(localStorage.getItem('id'));
    role = localStorage.getItem('fonction');
    isAdmin: boolean;
    bcot: boolean;
    supervisor: boolean;
    constructor(
        public appService: AppService,
        private store: Store<AppState>,
        private http: HttpClient
    ) {}

    ngOnInit() {
        this.ui = this.store.select('ui');
        this.ui.subscribe((state: UiState) => {
            this.classes = `${BASE_CLASSES} ${state.sidebarSkin}`;
        });
       this.email = localStorage.getItem('email');
       if( this.role === "ROLE_SUPERVISOR"){
        this.supervisor = true;

     } else{
       this.supervisor = false;
     ;
     }

       if( this.role === "ROLE_ADMINISTRATEUR"){
        this.isAdmin = true;

     } else{
       this.isAdmin = false;
     ;
     }
     if(this.role === "ROLE_BCOT"){
      this.bcot = true;

   } else{
     this.bcot = false;
   ;
   }
    }

}

export const MENU = [
    // {
    //     name: 'Tableau de Bord',
    //     iconClasses: 'fas fa-tachometer-alt',
    //     path: ['/']
    // },
    {
        name: 'Listes des fiches',
        iconClasses: 'fas fa-folder',
        children: [
            {
                name: 'Créer une Intervention ',
                iconClasses: 'far fa-address-book',
                path: ['/crée-une-intervention']
            },
            {
                name: 'Listes des interventions',
                iconClasses: 'fas fa-file',
                path: ['/listes-des-interventions']
            },
            // {
            //   name: 'Ajouter une Compagnie',
            //   iconClasses: 'fas fa-list',
            //   path: ['/sub-menu2']
            // },
          //   {
          //     name: 'suivie',
          //     iconClasses: 'fas fa-forward',
          //     path: ['/suivi']
          // }
        ]
    },
    {
      name: 'Admin Fonction',
      iconClasses: 'fas fa-folder',
      children: [
        {
              name: 'Caserne',
              iconClasses: 'fas fa-home',
              path: ['/c_caserne']
          },
          {
              name: 'Agent ',
              iconClasses: 'far fa-address-book',
              path: ['/c_agent']
          },

          {
            name: 'Engin',
            iconClasses: 'fas fa-truck',
            path: ['/c_engin']
          },
          {
            name: 'Fiche de Garde',
            iconClasses: 'fas fa-clipboard',
            path: ['/c_program']
          },
        //   {
        //     name: 'Equipe',
        //     iconClasses: 'fas fa-users',
        //     path: ['/c_equipe']
        // }
      ]
  }
];



export const MENU2 = [
  // {
  //     name: 'Tableau de Bord',
  //     iconClasses: 'fas fa-tachometer-alt',
  //     path: ['/']
  // },
  {
      name: 'Listes des fiches',
      iconClasses: 'fas fa-folder',
      children: [
          {
              name: 'Créer une Intervention ',
              iconClasses: 'far fa-address-book',
              path: ['/crée-une-intervention']
          },
          {
              name: 'Listes des interventions',
              iconClasses: 'fas fa-file',
              path: ['/listes-des-interventions']
          },
          // {
          //   name: 'Ajouter une Compagnie',
          //   iconClasses: 'fas fa-list',
          //   path: ['/sub-menu2']
          // },
        //   {
        //     name: 'suivie',
        //     iconClasses: 'fas fa-forward',
        //     path: ['/suivi']
        // }
      ]
  }
];


export const MENU3 = [
  // {
  //     name: 'Tableau de Bord',
  //     iconClasses: 'fas fa-tachometer-alt',
  //     path: ['/']
  // },
  {
      name: 'Listes des fiches',
      iconClasses: 'fas fa-folder',
      children: [
          // {
          //     name: 'Créer une Intervention ',
          //     iconClasses: 'far fa-address-book',
          //     path: ['/crée-une-intervention']
          // },
          {
              name: 'Listes des interventions',
              iconClasses: 'fas fa-file',
              path: ['/listes-des-interventions']
          },
          // {
          //   name: 'Ajouter une Compagnie',
          //   iconClasses: 'fas fa-list',
          //   path: ['/sub-menu2']
          // },
        //   {
        //     name: 'suivie',
        //     iconClasses: 'fas fa-forward',
        //     path: ['/suivi']
        // }
      ]
  }
];



export const MENU4 = [
  // {
  //     name: 'Tableau de Bord',
  //     iconClasses: 'fas fa-tachometer-alt',
  //     path: ['/']
  // },
  {
      name: 'Listes des fiches',
      iconClasses: 'fas fa-folder',
      children: [
          {
              name: 'Créer une Intervention ',
              iconClasses: 'far fa-address-book',
              path: ['/crée-une-intervention']
          },
          {
              name: 'Listes des interventions',
              iconClasses: 'fas fa-file',
              path: ['/listes-des-interventions']
          },
          // {
          //   name: 'Ajouter une Compagnie',
          //   iconClasses: 'fas fa-list',
          //   path: ['/sub-menu2']
          // },
        //   {
        //     name: 'suivie',
        //     iconClasses: 'fas fa-forward',
        //     path: ['/suivi']
        // }
      ]
  },
  {
    name: 'Admin Fonction',
    iconClasses: 'fas fa-folder',
    children: [
      // {
      //       name: 'Caserne',
      //       iconClasses: 'fas fa-home',
      //       path: ['/c_caserne']
      //   },
        {
            name: 'Agent ',
            iconClasses: 'far fa-address-book',
            path: ['/c_agent']
        },

        {
          name: 'Engin',
          iconClasses: 'fas fa-truck',
          path: ['/c_engin']
        },
        {
          name: 'Fiche de Garde',
          iconClasses: 'fas fa-clipboard',
          path: ['/c_program']
        },
      //   {
      //     name: 'Equipe',
      //     iconClasses: 'fas fa-users',
      //     path: ['/c_equipe']
      // }
    ]
}
];
