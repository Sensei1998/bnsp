import { Agent } from '@/model/Agent.model';
import { DailyProgramCreationForm } from '@/model/DailyProgramCreationForm.model';
import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { EnginUpdateForm } from '@/model/EnginUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormArray } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import {DateTime} from 'luxon';
import { ActivatedRoute } from '@angular/router';
import { error } from 'protractor';
import { DailyProgramAddEquipeForm } from '@/model/DailyProgramAddEquipeForm.model';

@Component({
  selector: 'app-creation-program',
  templateUrl: './creation-program.component.html',
  styleUrls: ['./creation-program.component.scss']
})
export class CreationProgramComponent implements OnInit{

  url ="http://localhost:8081/bnsp/api";

  ficheForm: FormGroup = this.formBuilder.group({
    date: [[], Validators.required],
  })

   enginForm:FormGroup = this.formBuilder.group({
    caserneId:[[], Validators.required],
    enginTypeId: [[], Validators.required],
    immatriculation: [[], Validators.required],
    description: [[], Validators.required],
  });

  enginUdapteForm:FormGroup = this.formBuilder.group({
    enginId: [[], Validators.required],
    caserneId:[[], Validators.required],
    enginTypeId: [[], Validators.required],
    immatriculation: [[], Validators.required],
    description: [[], Validators.required],
  });

  caserne;
  engin;
  edit;
  sort;
  users;
  usersCaserne;
  filtreAgent;
  i = 0

  program: any;
  date = DateTime.now();
  t;

  idCaserne = Number(localStorage.getItem('idCaserne'));
  role = localStorage.getItem('fonction');
  isAdmin:boolean;

  vehicule;

  CaporalForm: FormGroup = this.formBuilder.group({
    principal: [[]],
    remplacant: [[]],
  });

  sergentForm: FormGroup = this.formBuilder.group({
    principal: [[]],
    remplacant: [[]],
  })

  addEquipe: FormGroup = this.formBuilder.group({

    typeId: [[], Validators.required],
    designation: [[], Validators.required],
    members: this.formBuilder.array([
      this.formBuilder.group({
        principalId: [],
        remplacantId: [],
        fonctionId: []
      })
    ])
  })

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal,
    private route: ActivatedRoute){
    config.backdrop = 'static';
		config.keyboard = false;
    }

  ngOnInit(): void {
    this.getAgent();
    this.getEnginByCaserne(this.idCaserne);
    this.getProgram(this.formatDate(this.date));
    this.getAgentByCaserne(this.idCaserne);

    if(this.role === "ROLE_SUPERVISOR"){
      this.isAdmin = true;
   } else{
     this.isAdmin = false;
   }
  }

  get member(): FormArray{
    return this.addEquipe.get('members') as FormArray;
  }

  addmembers(){
    this.member.push(this.formBuilder.group({
      principalId: [],
      remplacantId: [],
      fonctionId: []
    }));
  }
  deletemembers(index: number){
    this.member.removeAt(index);
    this.member.markAsDirty()
  }


  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd-MM-yyyy');
  }

  // get members(): FormArray{
  //   return this.equipesForm.get('members') as FormArray;
  // }e

  // addMembers(){
  //  return this.members.push((this.formBuilder.group({
  //   agentId: [[], Validators.required],
  //   fonctionId: [[], Validators.required],
  //   })));
  // }
  // deleteMembers(index: number){
  //   this.members.removeAt(index);
  //   this.members.removeAt(index-1);
  //   this.members.markAsDirty();
  // }



  // get fonctionId(): FormArray{
  //   return this.membersForm.get('fonctionId') as FormArray;
  // }

  // addFonction(){
  //   this.fonctionId.push(this.formBuilder.control(''));
  // }
  // deleteFonction(index: number){
  //   this.fonctionId.removeAt(index);
  //   this.fonctionId.markAsDirty()
  // }


  getAgent(){
    return this.http.get(this.url + "/users/").subscribe(
      user =>{
        this.users = user;
      }
    );
  }

  getCaserne(){
    return this.http.get(this.url + "/casernes").subscribe(
      caserne => {
        this.caserne = caserne;
      }
    );
  }

  getEngin(){
    return this.http.get(this.url + "/engins").subscribe(
      engin => {
        this.engin = engin;
      }
    );
  }



  getAgentByCaserne(id: number){
    return this.http.get(this.url + "/users/caserne/" + id).subscribe(
      user =>{
        this.usersCaserne = user
      }
    );
  }

  createEngins(engin: EnginCreationForm){
    return this.http.post<EnginCreationForm>(this.url + "/engins/create", engin).subscribe();
  }

  updateEngin(engin:  EnginUpdateForm){
    return this.http.put<EnginUpdateForm>(this.url + "/engins/update", engin).subscribe();
  }

  getEnginById(id: number){
    return this.http.get(this.url + "/engins/" + id).subscribe(
      edit =>{
        this.edit = edit;
        console.log(this.edit.id);
      }
    );
  }

  deleteEngin(id: number){
    return this.http.delete(this.url + "/engins/" + id).subscribe(
      del => {
        this.toastr.success('Engin supprimé avec succès!');
        window.location.reload();
      }
    );
  }

  getEnginByCaserne(id: number){
    return this.http.get(this.url + "/engins/caserne/" + id).subscribe(
      vehicule => {
        this.vehicule = vehicule
      });
  }

  getTeamName(type: string, role: string): [string, string] {

    let principal: string = '';
    let secondaire: string= '';
    let team = this.t.find(t => t.teamType === type );
    if (team) {

      for (const agent of team.agents) {
            principal = agent.principalFirstname;
            secondaire =agent.secondFirstname;
          }
    }
    return [principal, secondaire];
  }

getProgram(date){
    return this.http.get("http://localhost:8081/bnsp/api/programs/search?date=" +date).subscribe(
      (program:any) =>{
        try{
          this.program = program;
        this.t = this.program.teams;
        // for(const team of this.t){
        //   for (const agent of team.agents) {
        //     this.t = agent;
        //     console.log(this.t);
        //   }
        // }
        } catch{
          this.toastr.error('Aucune fiche actuellement')
        }
      }
    )
}

createProgram(program: DailyProgramCreationForm){
  return this.http.post<DailyProgramCreationForm>("http://localhost:8081/bnsp/api/programs/create", program).subscribe();
}

addEquipeOnFiche(equipe: DailyProgramAddEquipeForm){
  return this.http.put<DailyProgramAddEquipeForm>("http://localhost:8081/bnsp/api/programs/team/add" , equipe).subscribe()
}


  onSubmit(){


    if(this.ficheForm.valid){
      let program: DailyProgramCreationForm = {
        date: this.ficheForm.get('date').value,
        caporal:{
          principal: this.CaporalForm.get('principal').value,
          remplacant: this.CaporalForm.get('remplacant').value
        },
        sergent:{
          principal: this.sergentForm.get('principal').value,
          remplacant: this.sergentForm.get('remplacant').value
        }
        }
        //console.log(program)
        this.createProgram(program);
      window.location.reload();
      this.toastr.success('Programme crée avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
    }
  }

  onSubmitEquipe(){
    if(this.addEquipe.valid){
      let equipe: DailyProgramAddEquipeForm = {
        programId : this.program.id,
        typeId: this.addEquipe.get('typeId').value,
        designation: this.addEquipe.get('designation').value,
        members: this.addEquipe.getRawValue().members
      }
      try {
        this.addEquipeOnFiche(equipe);
        window.location.reload();
        this.toastr.success('Programme crée avec succès!');
      } catch {
        this.toastr.error('Formulaire invalide!');
      }
    }
  }

  onSubmit2(){
    console.log(this.enginUdapteForm);
    if(this.enginUdapteForm.valid){
      let Engin:EnginUpdateForm = {
        enginId: this.enginUdapteForm.get("enginId").value,
        caserneId: this.enginUdapteForm.get("caserneId").value,
        enginTypeId: this.enginUdapteForm.get("enginTypeId").value,
        immatriculation: this.enginUdapteForm.get("immatriculation").value,
        description: this.enginUdapteForm.get("description").value
      }
      this.updateEngin(Engin);
      window.location.reload();
      this.toastr.success('Engin mise à jour avec succès!');
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
