import { Agent } from '@/model/Agent.model';
import { AgentCreationForm } from '@/model/AgentCreationForm.model';
import { AgentUpdateForm } from '@/model/AgentUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalConfig } from '@ng-bootstrap/ng-bootstrap';
import {ToastrService} from 'ngx-toastr';



@Component({
  selector: 'app-creation-agent',
  templateUrl: './creation-agent.component.html',
  styleUrls: ['./creation-agent.component.scss']
})
export class CreationAgentComponent implements OnInit{
  agentForm:FormGroup = this.formBuilder.group({
    matricule: [[], Validators.required],
    firstname:   [[], Validators.required],
    lastname:  [[], Validators.required],
    password:   [[], Validators.required, ],
    confirmPassword: [[], Validators.required],
    caserneId:   [[], Validators.required],
    gradeId: [[], Validators.required],
    telephone:this.formBuilder.array(['']),
    email:[[], Validators.required],
    defaultFonction: [-1, Validators.required]
  });



  url ="http://localhost:8081/bnsp/api";

  caserne;
  grade;

  users: Agent;
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

  passwordControle;

  isPasswordValid = true;
  isPasswordMatch = true;

  compagnie;
  numero = this.formBuilder.array([]);

  role = localStorage.getItem('fonction');
  isAdmin: boolean;

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal) {
    config.backdrop = 'static';
		config.keyboard = false; }

  ngOnInit(): void {
    this.getCaserne();
    this.getGrade();
    this.getAgent();
    this.agentForm.get('password').valueChanges.subscribe(() => {
      this.checkPassword();
    });

    this.agentForm.get('confirmPassword').valueChanges.subscribe(() => {
      this.checkPasswordMatch();
    });


    if(this.role === "ROLE_SUPERVISOR" || this.role === "ROLE_ADMINISTRATEUR"){
       this.isAdmin = true;

    } else{
      this.isAdmin = false;
    ;
    }
  }


  get number(): FormArray{
    return this.agentForm.get('telephone') as FormArray;
  }

  addNumber(){
    this.number.push(this.formBuilder.control(''),);
  }
  deleteNumber(index: number){
    this.number.removeAt(index);
    this.number.markAsDirty()
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

  checkPassword() {
    const passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])[a-zA-Z\d@#$%]{8,}$/;
    const password = this.agentForm.get('password').value;
    this.isPasswordValid = passwordPattern.test(password);
  }

  checkPasswordMatch() {
    const password = this.agentForm.get('password').value;
    const confirmPassword = this.agentForm.get('confirmPassword').value;
    this.isPasswordMatch = password === confirmPassword;
  }

  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne;
      }
    );
  }

  getAgentByCaserne(id: number){
      return this.http.get(this.url + "/users/caserne/" + id).subscribe(
        (user: Agent) => {
          this.users = user
        }
      );


  }



  getGrade(){
   return this.http.get(this.url + "/users/grades").subscribe(
    grade => {
      this.grade = grade
    }
   );
  }

  createAgent(agent: AgentCreationForm){
    return this.http.post<AgentCreationForm>(this.url + "/users/create", agent).subscribe();
  }

  getAgent(){
    return this.http.get(this.url + "/users/").subscribe(
      (user: Agent) =>{
        this.users = user;

        // let split = this.users.phoneNumber.split(';');
        // this.users.phoneNumber = split;
      }
    );
  }

  updateAgent(agent: AgentUpdateForm){
    return this.http.put<AgentUpdateForm>(this.url+"/users/update", agent).subscribe();
  }

  getAgentById(id: number){
    return this.http.get(this.url + "/users/" + id).subscribe(
      agent =>{
        this.agent = agent;
        let split = this.agent.phoneNumber.split(';');

        split.forEach(phoneNumber => {
          this.numero.push(this.formBuilder.control(phoneNumber));
          this.agentEditForm.setControl('telephone', this.formBuilder.array(split));
        });
        console.log(this.agent);;
      }
    );
  }

  deleteAgent(id: number){
    return this.http.delete(this.url + "/users/" + id ).subscribe(
      supp => {
        this.toastr.success('Agent supprimé avec succès!');
        window.location.reload()
      }
    );
  }

  onSubmit(){


      if(this.agentForm.valid){
        let Agent:AgentCreationForm = {
          matricule: this.agentForm.get("matricule").value,
          firstname: this.agentForm.get("firstname").value,
          lastname: this.agentForm.get("lastname").value,
          password: this.agentForm.get("password").value,
          caserneId: this.agentForm.get("caserneId").value,
          gradeId: this.agentForm.get("gradeId").value,
          telephone: this.agentForm.getRawValue().telephone,
          email: this.agentForm.get("email").value,
          defaultFonction: this.agentForm.get("defaultFonction").value,
        };
        console.log(Agent);
        this.createAgent(Agent);
        this.toastr.success('Agent crée avec succès!');
        window.location.reload();
      }else if (this.agentForm.invalid){
        console.log(this.agentForm);
        this.toastr.error('Formulaire invalide!');
      } else {
        this.toastr.error('Formulaire invalide!');
      }


  }

  onEdit(){
    console.log(this.agentEditForm);
    if(this.agentEditForm.valid){
      let Agent:AgentUpdateForm = {
        id: this.agentEditForm.get("id").value,
        matricule: this.agentEditForm.get("matricule").value,
        firstname: this.agentEditForm.get("firstname").value,
        lastname: this.agentEditForm.get("lastname").value,
        caserneId: this.agentEditForm.get("caserneId").value,
        gradeId: this.agentEditForm.get("gradeId").value,
        telephone: this.agentEditForm.getRawValue().telephone,
        email: this.agentEditForm.get("email").value,
        defaultFonction: this.agentEditForm.get("defaultFonction").value,
      }
      this.updateAgent(Agent);
      this.toastr.success('Agent mise à jour avec succès!');
      window.location.reload();
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

  openRead(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

  openSupp(content) {
		this.modalService.open(content, {size: 'xl', centered: true });
	}

}
