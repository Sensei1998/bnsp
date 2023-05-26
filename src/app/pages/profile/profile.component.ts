import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormArray } from '@angular/forms';
import { LoginComponent } from '@modules/login/login.component';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  agent;
  agentEditForm:FormGroup = this.formBuilder.group({
    id: [[], Validators.required],
    matricule: [[], Validators.required],
    firstname:   [[], Validators.required],
    lastname:  [[], Validators.required],
    caserneId:   [[], Validators.required],
    gradeId: [[], Validators.required],
    telephone:this.formBuilder.array(['']),
    email:[[], Validators.required],
    defaultFonction: [-1, Validators.required]
  });
  numero = this.formBuilder.array([]);
  constructor( private formBuilder: FormBuilder,private http: HttpClient){}

  ngOnInit(): void {
    this.getProfile();
  }

  get number2(): FormArray{
    return this.agentEditForm.get('telephone') as FormArray;
  }

  addNumber2(){
    this.number2.push(this.formBuilder.control(''),);
  }
  deleteNumber2(index: number){
    this.number2.removeAt(index);
    this.number2.markAsDirty()
  }


  getProfile(){
    return this.http.get("http://localhost:8081/bnsp/api/users/profil").subscribe(
      data => {
        this.agent = data;
        let split = this.agent.phoneNumber.split(';');

        split.forEach(phoneNumber => {
          this.numero.push(this.formBuilder.control(phoneNumber));
          this.agentEditForm.setControl('telephone', this.formBuilder.array(split));
        });
      }
    )
  }
}
