import { interventionCaserne } from '@/model/InterventionCaserne.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '@services/api.service';
import {DateTime} from 'luxon';
import { ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-sub-menu2',
  templateUrl: './sub-menu2.component.html',
  styleUrls: ['./sub-menu2.component.scss']
})
export class SubMenu2Component implements OnInit {
  date = DateTime.now();
  CompagnieForm: FormGroup = this.fb.group({
    compagnie: this.fb.array([
      this.fb.group({
        caserneId: [[], Validators.required],
        message:[[]]
      })
    ])
  });



  caserne;
  category;
  libelle;
  afficheCat;
  afficheLib;
  data = this.service.formData;


  constructor(private fb: FormBuilder,
    private http: HttpClient,
    public service: ApiService,
    private toastr: ToastrService,
    private router: Router){}

  ngOnInit(): void {


  this.getCaserne();
  this.getCategory();
  this.getLibelleBycategory(this.service.formData.incident.categoryId)

  }

  get compagnie(): FormArray{
    return this.CompagnieForm.get('compagnie') as FormArray;
  }

  getCaserne(){
    return this.service.getCaserne().subscribe(
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
    this.compagnie.push( this.fb.group({
      caserneId: [[], Validators.required],
      message:[[]]
    }));
  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd LLL yyyy');
}

formatHeure(heure) {
  return DateTime.fromISO(heure).toFormat('hh : mm');
}
getLibelleBycategory(id: number){
  return this.service.getLibelleBycategory(id).subscribe(
    data => {
      this.libelle = data;
      this.libelle = this.libelle.find(lib => lib.id == this.service.formData.incident.incidentTypeId);
    }
  )
}

getCategory(){
  return this.service.getCategory().subscribe(
    data => {
      this.category = data;
      this.category = this.category.find(category => category.id == this.service.formData.incident.categoryId)
    }
  )
}

addCaserneIntervention(Caserne: interventionCaserne){
  return this.service.addCaserneIntervention(Caserne).subscribe()
}

onSubmit(){
  if(this.CompagnieForm.valid){
    let compagnie: interventionCaserne = {
      id: this.service.id,
      casernes: this.CompagnieForm.getRawValue().compagnie
    }
    this.addCaserneIntervention(compagnie);
    this.toastr.success('Compagnie ajouter avec succès!');
    setTimeout(()=>{
      this.router.navigate(['listes-des-interventions']);
    }, 2000)

  }else {
    this.toastr.error('Erreur information incomplete');
  }
}



}
