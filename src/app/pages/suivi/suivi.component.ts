import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {DateTime} from 'luxon';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';

export type Option = {
  id: string;
  name: string;
};

@Component({
  selector: 'app-suivi',
  templateUrl: './suivi.component.html',
  styleUrls: ['./suivi.component.scss'],
  providers: [NgbModalConfig, NgbModal],
})
export class SuiviComponent implements OnInit {
  date = DateTime.now();
  time = null;
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

  constructor(private router: Router, config: NgbModalConfig, private modalService: NgbModal) {
    config.backdrop = 'static';
		config.keyboard = false;
  }

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

  open(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

}
