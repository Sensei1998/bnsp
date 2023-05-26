import { interventionCaserne } from '@/model/InterventionCaserne.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '@services/api.service';
import {DateTime} from 'luxon';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-ajout-compagnie',
  templateUrl: './ajout-compagnie.component.html',
  styleUrls: ['./ajout-compagnie.component.scss']
})
export class AjoutCompagnieComponent implements OnInit {
  date = DateTime.now();
  CompagnieForm: FormGroup = this.fb.group({
    compagnie: this.fb.array([  ])
  });



  url ="http://localhost:8081/bnsp/api";
  caserne;
  category;
  libelle;
  afficheCat;
  afficheLib;
  data = this.service.formData;
  numero = this.fb.array([]);

  constructor(private fb: FormBuilder,
    private http: HttpClient,
    public service: ApiService,
    private toastr: ToastrService,
    private router: Router){}

  ngOnInit(): void {


  this.getCaserne();
  this.getCategory();
  // let split = this.service.formData.casernes;
  // console.log(split)
  // split.forEach(compagnie => {
  //   this.numero.push(this.fb.control(compagnie));
  //   this.CompagnieForm.setControl('compagnie', this.fb.array(split));
  // });
  const casernes = this.service.formData.casernes;
const compagnieArray = this.CompagnieForm.get('compagnie') as FormArray;

if (casernes.length === 0) {
  this.addCompagnie();
} else {
  casernes.forEach((caserne: any) => {
    const compagnieGroup = this.fb.group({
      caserneId: [caserne.caserneId, Validators.required],
      message: [caserne.message]
    });
    compagnieArray.push(compagnieGroup);
  });
}
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
  const url = `http://localhost:8081/bnsp/api/intervention/types?category=${id}`;
  return this.http.get(url).subscribe(
    data => {
      this.libelle = data;
      this.libelle = this.libelle.find(lib => lib.id == this.service.formData.incident.incidentTypeId);
    }
  )
}

getCategory(){
  return this.http.get("http://localhost:8081/bnsp/api/intervention/types/category").subscribe(
    data => {
      this.category = data;
      this.category = this.category.find(category => category.id == this.service.formData.incident.categoryId)
    }
  )
}

addCaserneIntervention(Caserne: interventionCaserne){
  return this.http.put<interventionCaserne>("http://localhost:8081/bnsp/api/intervention/update/info", Caserne).subscribe(
    data => console.log(data)
  )
}

onSubmit(){
  if(this.CompagnieForm.valid){
    let compagnie: interventionCaserne = {
      id: this.service.formData.interventionId,
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
