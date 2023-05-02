import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '@services/api.service';
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

  url ="http://localhost:8081/bnsp/api";
  caserne;
  category;
  libelle;
  afficheCat;
  afficheLib;
  data = this.service.formData;


  interventionForm: FormGroup = this.fb.group({
    date: [[], Validators.required],
    time:[[], Validators.required],
    provenance : [[], Validators.required],
    phoneNumber : [[], [Validators.required, Validators.minLength(8)]],
    name : [[], Validators.required],
    address : [[]],
    longitude: [[]],
    latitude: [[]],
    precision: [[]],
    categoryId:[[], Validators.required],
    incidentTypeId:[[], Validators.required],
    comments:[[]]
  })


  constructor(private fb: FormBuilder,
    private http: HttpClient,
    public service: ApiService){}

  ngOnInit(): void {
    this.CompagnieForm = this.fb.group({
      compagnie: this.fb.array([
      ]),
  });
  setTimeout(() => {
    this.addCompagnie();
  this.addCompagnie();
  })

  this.getCaserne();
  this.getCategory();
  this.getLibelleBycategory(this.service.formData.incident.categoryId)

  }

  get compagnie(): FormArray{
    return this.CompagnieForm.get('compagnie') as FormArray;
  }

  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne;
      }
    );
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
getLibelleBycategory(id: number){
  const url = `http://localhost:8081/bnsp/api/intervention/types?category=${id}`;
  return this.http.get(url).subscribe(
    data => {
      this.libelle = data;
      this.libelle = this.libelle.find(lib => lib.id == this.service.formData.incident.incidentTypeId)
      console.log(this.libelle)
    }
  )
}

getCategory(){
  return this.http.get("http://localhost:8081/bnsp/api/intervention/types/category").subscribe(
    data => {
      this.category = data;
      this.category = this.category.find(category => category.id == this.service.formData.incident.categoryId)
      console.log(this.category)
    }
  )
}



}
