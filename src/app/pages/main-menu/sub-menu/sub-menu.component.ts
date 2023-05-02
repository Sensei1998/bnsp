import { IncidentPartial } from '@/model/IncidentPartial.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '@services/api.service';
import {DateTime} from 'luxon';
import { ToastrService } from 'ngx-toastr';

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

  interventionForm: FormGroup = this.form.group({
    date: [[''], Validators.required],
    time:[[''], Validators.required],
    provenance : [[''], Validators.required],
    phoneNumber : [[''], [Validators.required, Validators.minLength(8)]],
    name : [[''], Validators.required],
    address : [['']],
    longitude: [['']],
    latitude: [['']],
    precision: [['']],
    categoryId:[[''], Validators.required],
    incidentTypeId:[[''], Validators.required],
    comments:[['']]
  })
  category;
  libelle;
  idCategory: number;
  profil;

  constructor(private router: Router, private form: FormBuilder, private http : HttpClient,
    private toastr: ToastrService,private service: ApiService) { }

  ngOnInit(): void {
    this.getCategory();
    this.getProfil();
  }

  getCategory(){
    return this.http.get("http://localhost:8081/bnsp/api/intervention/types/category").subscribe(
      data => {
        this.category = data;
      }
    )
  }

  getLibelleBycategory(id: number){
    const url = `http://localhost:8081/bnsp/api/intervention/types?category=${id}`;
    return this.http.get(url).subscribe(
      data => {
        this.libelle = data;
      }
    )
  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('yyyy-MM-dd');
  }

  formatHeure(heure) {
    return DateTime.fromISO(heure).toFormat('hh:mm:ss');
  }

  getProfil(){
    return this.http.get("http://localhost:8081/bnsp/api/users/profil").subscribe(
      data => {
        this.profil = data;
      }
    )
  }

  createPartialIntervention(Incident : IncidentPartial){
    return this.http.post<IncidentPartial>("http://localhost:8081/bnsp/api/intervention/create/partial", Incident).subscribe();
  }

  onSubmit(){
     if(this.interventionForm.valid){
      let intervention: IncidentPartial ={
        cctoId: this.profil.id,
        date: this.formatDate(this.date),
        time: this.interventionForm.get('time').value,
        provenance: this.interventionForm.get('provenance').value,
        phoneNumber: this.interventionForm.get('phoneNumber').value,
        name: this.interventionForm.get('name').value,
        address: this.interventionForm.get('address').value,
        longitude: this.interventionForm.get('longitude').value,
        latitude: this.interventionForm.get('latitude').value,
        precision: this.interventionForm.get('precision').value,
        incident: {
          categoryId: this.interventionForm.get('categoryId').value,
          incidentTypeId: this.interventionForm.get('incidentTypeId').value,
          comments: this.interventionForm.get('comments').value,
        }
      }
      this.service.formData = intervention;
      this.createPartialIntervention(intervention);
      this.router.navigate(['/sub-menu2']);
    } else {
        this.toastr.error('Erreur lors de la création de l \'intervention Formulaire invalide ou incomplet');
    }
  }


}
