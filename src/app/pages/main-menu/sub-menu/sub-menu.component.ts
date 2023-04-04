import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {DateTime} from 'luxon';

export type Option = {
  id: string;
  name: string;
};
@Component({
  selector: 'app-sub-menu',
  templateUrl: './sub-menu.component.html',
  styleUrls: ['./sub-menu.component.scss']
})
export class SubMenuComponent implements OnInit {
  date = DateTime.now();
  model = 1;
  num = this.formatHeure(this.date);
  heure = this.formatHeure(this.date);
  variableName: Array<Option>= [
    {
      'id': '1',
      'name': 'Accident de la Route'
    },
    {
      'id': '2',
      'name': 'Incendit'
    }
  ]

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd LLL yyyy');
  }

  formatHeure(heure) {
    return DateTime.fromISO(heure).toFormat('hh : mm');
  }

  onSubmit(){
    this.router.navigate(['sub-menu2']);
  }


}
