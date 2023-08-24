import { IncidentPartial } from '@/model/IncidentPartial.model';
import { CanDeactivateComponent } from '@/model/can-deactivate.interface';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '@services/api.service';
import {DateTime} from 'luxon';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-sub-menu',
  templateUrl: './sub-menu.component.html',
  styleUrls: ['./sub-menu.component.scss']
})
export class SubMenuComponent implements OnInit, CanDeactivateComponent {
  date = DateTime.now();
  model = 1;
  date2 = this.formatDate(this.date);
  heure = this.formatHeure(this.date);

  urlCarto="http://192.168.2.30:8000/carto/appelant/";
  longitude=0;
  latitude=0;
  precision = 'AML';
  interventionForm: FormGroup = this.form.group({
    date: [[''], Validators.required],
    time:[[''], Validators.required],
    provenance : [[''], Validators.required],
    phoneNumber : [[''], [Validators.required, Validators.minLength(8)]],
    name : [[''], Validators.required],
    address : [['']],
    longitude: [],
    latitude: [],
    precision: [],
    categoryId:[[''], Validators.required],
    incidentTypeId:[[''], Validators.required],
    comments:[['']]
  })
  category;
  libelle;
  idCategory: number;
  profil;
  numTel = "";
  constructor(private router: Router, private form: FormBuilder, private http : HttpClient,
    private toastr: ToastrService,private service: ApiService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.getCategory();
    this.getProfil();
    this.route.paramMap.subscribe(param =>{
      this.numTel = param.get('numtel')!;
    });
    setTimeout(()=> {
      this.getCoordonnee();
    },2000)
  }

  getCoordonnee(){
    return this.http.get(this.urlCarto + this.numTel).subscribe(
      (result: any) => {
        if(result.sms_longitude !== null && result.sms_latitude !== null) {
         this.longitude = result.sms_longitude;
        this.latitude = result.sms_latitude;
        } else {
          this.longitude=0;
          this.latitude = 0;
        }

      }
    )
  }

  getCategory(){
    return this.service.getCategory().subscribe(
      data => {
        this.category = data;
      }
    )
  }

  getLibelleBycategory(id: number){
    return this.service.getLibelleBycategory(id).subscribe(
      data => {
        this.libelle = data;
      }
    )
  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('yyyy-MM-dd');
  }

  formatHeure(heure) {
    return DateTime.fromISO(heure).toFormat('HH:mm:ss');
  }

  getProfil(){
    return this.service.getProfil().subscribe(
      data => {
        this.profil = data;
      }
    )
  }

  createPartialIntervention(Incident : IncidentPartial){
    return this.service.createPartialIntervention(Incident).subscribe(
      (data: any) => {
        this.service.id = data.id;
      }
    );
  }

  canDeactivate(): boolean | Observable<boolean> {
    if (this.interventionForm.valid) {
      return true;
    } else {
      // Si le formulaire n'est pas valide, affichez un message à l'utilisateur si nécessaire.
      // Vous pouvez également vérifier s'il y a des données non sauvegardées ici.
      const confirmExit = window.confirm(
        'Des données non sauvegardées existent. Êtes-vous sûr de vouloir quitter la page ?'
      );
      return confirmExit;
    }
  }
  onSubmit(){
     if(this.interventionForm.valid){
      let intervention: IncidentPartial ={
        cctoId: this.profil.id,
        date: this.interventionForm.get('date').value,
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
      console.log(intervention)
      this.service.formData = intervention;
      this.createPartialIntervention(intervention);
      this.toastr.success('Information Enregistrer avec succès!');
      this.router.navigate(['/affecter-companie']);
    } else {
         this.toastr.error('Erreur lors de la création de l \'intervention Formulaire invalide ou incomplet');
     }
  }


}
