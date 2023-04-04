import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import {DateTime} from 'luxon';

export type Option = {
  id: string;
  name: string;
};

@Component({
  selector: 'app-sub-menu2',
  templateUrl: './sub-menu2.component.html',
  styleUrls: ['./sub-menu2.component.scss']
})
export class SubMenu2Component implements OnInit {
  date = DateTime.now();
  CompagnieForm: FormGroup;
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
  ];

  constructor(private fb: FormBuilder){}

  ngOnInit(): void {
    this.CompagnieForm = this.fb.group({
      compagnie: this.fb.array([
      ]),
  });
  setTimeout(() => {
    this.addCompagnie();
  this.addCompagnie();
  })

  }

  get compagnie(): FormArray{
    return this.CompagnieForm.get('compagnie') as FormArray;
  }

  deleteCompagnie(index: number){
    this.compagnie.removeAt(index);
    this.compagnie.markAsDirty()
  }

  addCompagnie(){
    this.compagnie.push(new FormControl());
  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd LLL yyyy');
}

formatHeure(heure) {
  return DateTime.fromISO(heure).toFormat('hh : mm');
}


}
