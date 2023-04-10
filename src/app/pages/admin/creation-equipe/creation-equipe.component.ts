import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { EquipeCreationForm } from '@/model/EquipeCreationForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-creation-equipe',
  templateUrl: './creation-equipe.component.html',
  styleUrls: ['./creation-equipe.component.scss']
})
export class CreationEquipeComponent implements OnInit {
  url =" http://localhost:8081/bnsp/api";

  equipeForm:FormGroup = this.formBuilder.group({
    caserneId: [[], Validators.required],
    equipeTypeId: [[], Validators.required],
    enginId: [-1],
    designation: [[], Validators.required]
  });

  caserne;
  vehicule;
  avecVehicule;
  chargerVehicule= false;
  idCaserne = 0;

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient){}

  ngOnInit(): void {
    this.getCaserne();
    this.getEngin();
  }
  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne
        this.caserne = this.caserne.filter(element => element.caserneType.id === 2   );
      }
    );
  }

  getEnginByCaserne(id: number){
    this.chargerVehicule = true;
    this.toastr.success('Vehicule Charger!');
    return this.http.get(this.url + "/engins/caserne/" + id).subscribe(
      vehicule => {
        this.vehicule = vehicule
      });
  }
  getEngin(){
    return this.http.get(this.url + "/engins").subscribe(
      vehicule => {
        this.vehicule = vehicule
      }
    );
  }


  createEquipe(equipe: EquipeCreationForm){
    return this.http.post<EquipeCreationForm>(this.url + "/teams/create" , equipe).subscribe();
  }

  activeVehicule(){
    return this.avecVehicule = true;
  }

  withoutVehicule(){
    return this.avecVehicule = false;
  }

  onSubmit(){
    if(this.equipeForm.valid){
      let Equipe:EquipeCreationForm = {
        caserneId: this.equipeForm.get("caserneId").value,
        equipeTypeId:this.equipeForm.get("equipeTypeId").value,
        enginId: this.equipeForm.get("enginId").value,
        designation: this.equipeForm.get("designation").value,
      }
      this.createEquipe(Equipe);
      window.location.reload();
      this.toastr.success('Engin crée avec success!');
    } else{
      this.toastr.error('Formulaire invalid!');
    }
  }
  onSubmit2(){

    if(this.equipeForm.valid){
      let Equipe:EquipeCreationForm = {
        caserneId: this.equipeForm.get("caserneId").value,
        equipeTypeId:this.equipeForm.get("equipeTypeId").value,
        enginId: this.equipeForm.get("enginId").value,
        designation: this.equipeForm.get("designation").value,
      }
      this.createEquipe(Equipe);
      window.location.reload();
      this.toastr.success('Engin crée avec success!');
    } else{
      this.toastr.error('Formulaire invalid!');
    }
  }
}
