import { CaserneCreationForm } from '@/model/CaserneCreationForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { element } from 'protractor';

@Component({
  selector: 'app-creation-caserne',
  templateUrl: './creation-caserne.component.html',
  styleUrls: ['./creation-caserne.component.scss']
})
export class CreationCaserneComponent implements OnInit {
  url =" http://localhost:8081/bnsp/api";

  caserneForm:FormGroup = this.formBuilder.group({
    idCaserneType: [[], Validators.required],
    idCaserneParent: [null],
    name: [[], Validators.required],
    city:[[], Validators.required],
    area: [[], Validators.required],
  });

  caserne;
  hideAffliation = 0;
  brigade;
  compagnie

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient){}

  ngOnInit(): void {
    this.getCaserne();
  }

  createCaserne(caserne: CaserneCreationForm){
    return this.http.post<CaserneCreationForm>(this.url + "/casernes/create" , caserne).subscribe();
  }

  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne
      }
    );
  }

  sortByBrigarde(){
    let temp = this.caserne.filter(element => element.caserneType.id === 1   );
    this.brigade = temp;
    console.log(this.brigade);
  }
  sortByCompagnie(){
    let temp = this.caserne.filter(element => element.caserneType.id === 2   );
    this.compagnie = temp;
    console.log(this.compagnie);
  }

  onSubmit(){

    if(this.caserneForm.valid){
      let Caserne:CaserneCreationForm = {
        idCaserneType: this.caserneForm.get("idCaserneType").value,
        idCaserneParent: this.caserneForm.get("idCaserneParent").value,
        name: this.caserneForm.get("name").value,
        city: this.caserneForm.get("city").value,
        area: this.caserneForm.get("area").value,
      }
      this.createCaserne(Caserne)
      window.location.reload();
      this.toastr.success('Caserne crée avec success!');
    } else{
      this.toastr.error('Formulaire invalid!');
    }
  }
}
