import {Component, OnInit} from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormArray } from '@angular/forms';
import { LoginComponent } from '@modules/login/login.component';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  agent: any;
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
  constructor( private formBuilder: FormBuilder,){}

  ngOnInit(): void {

  }

  get number2(): FormArray{
    return this.agentEditForm.get('telephone') as FormArray;
  }
}
