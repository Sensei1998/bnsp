import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { EquipeCreationForm } from '@/model/EquipeCreationForm.model';
import { EquipeUpdateForm } from '@/model/EquipeUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  equipeUpdateForm:FormGroup = this.formBuilder.group({
    equipeId: [[], Validators.required],
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
  equipeid;
  equipe;

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal){
    config.backdrop = 'static';
		config.keyboard = false;
    }

  ngOnInit(): void {
    this.getCaserne();
    this.getEngin();
    this.getEquipe();
  }
  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne
        this.caserne = this.caserne.filter(element => element.caserneType.id === 2   );
      }
    );
  }

  getEquipe(){
    return this.http.get(this.url + "/teams").subscribe(
      equipe =>
      {this.equipe = equipe;}
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

  updateEquipe(equipe: EquipeUpdateForm){
    return this.http.put<EquipeUpdateForm>(this.url + "/teams/update", equipe).subscribe();
  }

  getEquipeById(id: number){
    return this.http.get(this.url + "/teams/" + id).subscribe(
      equipe =>{
        this.equipeid = equipe;
        console.log(this.equipeid);
        if(this.equipeid.engin === null){
          this.avecVehicule= false;
          this.equipeid ={
            id: this.equipeid.id,
            caserne: this.equipeid.caserne.id,
            equipeType: this.equipeid.equipeType.id,
            engin: -1,
            designation: this.equipeid.designation  ,
          }
        } else {
          this.avecVehicule= true;
          this.getEnginByCaserne(this.equipeid.caserne.id);
          this.equipeid ={
            id: this.equipeid.id,
            caserne: this.equipeid.caserne,
            equipeType: this.equipeid.equipeType,
            engin: this.equipeid.engin,
            designation: this.equipeid.designation  ,
          }
        }
      }
    );
  }

  deleteEquipe(id: number){
    return this.http.delete(this.url + "/teams/" + id).subscribe(
      del => {
        this.toastr.success('Equipe supprimeéé avec succès!');
        window.location.reload();
      }
    );
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
      this.toastr.success('Equipe crée avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
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
      this.toastr.success('Equipe crée avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
    }
  }

  onSubmit3(){
    if(this.equipeUpdateForm.valid){
      let Equipe:EquipeUpdateForm = {
        equipeId:  this.equipeUpdateForm.get("equipeId").value,
        caserneId: this.equipeUpdateForm.get("caserneId").value,
        equipeTypeId:this.equipeUpdateForm.get("equipeTypeId").value,
        enginId: this.equipeUpdateForm.get("enginId").value,
        designation: this.equipeUpdateForm.get("designation").value,
      }
      this.updateEquipe(Equipe);
      window.location.reload();
      this.toastr.success('Equipe mise à jour avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
    }
  }

  openCreate(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}
  openEdit(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}
}
