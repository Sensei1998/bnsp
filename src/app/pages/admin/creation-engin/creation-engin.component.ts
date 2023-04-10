import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-creation-engin',
  templateUrl: './creation-engin.component.html',
  styleUrls: ['./creation-engin.component.scss']
})
export class CreationEnginComponent implements OnInit{
  url =" http://localhost:8081/bnsp/api";

  enginForm:FormGroup = this.formBuilder.group({
    caserneId:[[], Validators.required],
    enginTypeId: [[], Validators.required],
    immatriculation: [[], Validators.required],
    description: [[], Validators.required],
  });

  caserne;


  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient){}

  ngOnInit(): void {
    this.getCaserne();
  }
  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne
        this.caserne = this.caserne.filter(element => element.caserneType.id === 2   );
      }
    );
  }

  createEngins(engin: EnginCreationForm){
    return this.http.post<EnginCreationForm>(this.url + "/engins/create", engin).subscribe();
  }


  onSubmit(){

    if(this.enginForm.valid){
      let Engin:EnginCreationForm = {
        caserneId: this.enginForm.get("caserneId").value,
        enginTypeId: this.enginForm.get("enginTypeId").value,
        immatriculation: this.enginForm.get("immatriculation").value,
        description: this.enginForm.get("description").value
      }
      this.createEngins(Engin);
      window.location.reload();
      this.toastr.success('Engin crée avec success!');
    } else{
      this.toastr.error('Formulaire invalid!');
    }
  }

}
