import { AgentCreationForm } from '@/model/AgentCreationForm.model';
import { AgentUpdateForm } from '@/model/AgentUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
    password:   [[], Validators.required],
    caserneId:   [[], Validators.required],
    gradeId: [[], Validators.required],
  });



  url ="http://localhost:8081/bnsp/api";

  caserne;
  grade;

  users;
  agent;

  agentEditForm:FormGroup = this.formBuilder.group({
    id: [[], Validators.required],
    matricule: [[], Validators.required],
    firstname:   [[], Validators.required],
    lastname:  [[], Validators.required],
    caserneId:   [[], Validators.required],
    gradeId: [[], Validators.required],
  });

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
  }


  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne;
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
      user =>{
        this.users = user;
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
      }
    );
  }

  deleteAgent(id: number){
    return this.http.delete(this.url + "/users/" + id ).subscribe(
      supp => {
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
      }
      this.createAgent(Agent);
      this.toastr.success('Agent crée avec success!');
      window.location.reload();
    } else{
      this.toastr.error('Formulaire invalid!');
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
      }
      this.updateAgent(Agent);
      this.toastr.success('Agent mise à jour avec success!');
      window.location.reload();
    } else{
      this.toastr.error('Formulaire invalid!');
    }
  }

  openCreate(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

  openEdit(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

}
