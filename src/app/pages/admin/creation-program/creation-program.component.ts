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

  agree;
  agree2;

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
      // this.formBuilder.group({
      //   principalId: [],
      //   remplacantId: [],
      //   fonctionId: []
      // })
    ])
  })
  teamId;
  idTeamSupp;
  dateprogram;
  program2;
  listeProgram:any[]=[];
  page = 1; // Page actuelle
  pageSize = 5; // Nombre d'éléments par page
  collectionSize: number; // Taille totale de la collection
  idprogram = 0;

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
    this.getTeam();
    //this.getProgram(this.formatDate(this.date));
    this.getAgentByCaserne(this.idCaserne);
    this.getProgramByCaserne(this.idCaserne);

    if(this.role === "ROLE_SUPERVISOR"){
      this.isAdmin = true;
   } else{
     this.isAdmin = false;
   }

   setTimeout(() => {
    this.collectionSize = this.listeProgram.length;
  }, 2000)
  }

  // get donneesPaginees() {
  //   if (this.listeProgram === null) {
  //     return [];
  //   }

  //   const sortedList = this.listeProgram.sort((a, b) => {
  //     // Tri par ordre décroissant en utilisant la propriété "date"
  //     return new Date(b.date).getTime() - new Date(a.date).getTime();
  //   });

  //   const startIndex = (this.page - 1) * this.pageSize;
  //   const endIndex = startIndex + this.pageSize;
  //   return sortedList.slice(startIndex, endIndex);
  // }

  get donneesPaginees() {
    if (this.listeProgram === null) {
      return this.listeProgram = [];
    }

    const sortedList = this.listeProgram.sort((a, b) => {
      // Tri par ordre décroissant en utilisant la propriété "date"
      return new Date(b.date).getTime() - new Date(a.date).getTime();
    });

    const startIndex = (this.page - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    const paginatedList = sortedList.slice(startIndex, endIndex);

    const totalPages = Math.ceil(sortedList.length / this.pageSize);
    let startPage = Math.max(1, this.page - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return paginatedList;
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

  addmembers2(selectedIndex: number) {
    const v = this.agree[selectedIndex];
    const nbrMembre = v.nbrMembers;

    this.member.clear(); // Réinitialiser le tableau avant d'effectuer la boucle

    let i;
    for (i = 0; i < nbrMembre; i++) {
      this.member.push(this.formBuilder.group({
        principalId: [],
        remplacantId: [],
        fonctionId: []
      }));
    }
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



  getTeam() {
    return this.http.get(this.url + "/programs/team/types").subscribe(
      (agree: any) => {
        this.agree = agree.filter(item => item.equipeType !== "Sgt" && item.equipeType !== "Caporal");
        this.agree2 = agree;
      }
    );
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

  getProgramByCaserne(id:number){
    return this.http.get("http://localhost:8081/bnsp/api/programs/caserne/"+id).subscribe(
      (program:any) =>{
        console.log(program);
        try{
          this.program2 = program;
          this.listeProgram = program
        } catch{
          this.toastr.error('Aucune fiche actuellement')
        }}
    )
  }

getProgram(date){
  const dateObj = new Date(date);
  const jour = dateObj.getDate();
  const mois = dateObj.getMonth() + 1; // Les mois sont indexés à partir de 0, donc on ajoute 1
  const annee = dateObj.getFullYear();

  // Formater la date avec le format "dd-MM-yyyy"
  const dateFormatee = `${jour.toString().padStart(2, '0')}-${mois.toString().padStart(2, '0')}-${annee}`;

    return this.http.get("http://localhost:8081/bnsp/api/programs/search?date=" +dateFormatee).subscribe(
      (program:any) =>{
        console.log(program)
        try{
          this.edit = program;
        } catch{
          this.toastr.error('Aucune fiche actuellement')
        }
      }
    )
}


groupByTeamType() {
  const groupedData = {};

  this.edit.teams.forEach((team) => {
    if (!groupedData[team.teamType]) {
      groupedData[team.teamType] = [];
    }

    groupedData[team.teamType].push(team);
  });

  return groupedData;
}



loadMembersByTeamId(teamId) {
  let members = this.edit.teams.find(team => team.teamId === Number(teamId));
  const memberFormArray = this.addEquipe.get('members') as FormArray;
  memberFormArray.clear(); // Supprime les membres existants du formArray
  this.teamId = teamId;
  members.agents.forEach((member) => {
    const memberGroup = this.formBuilder.group({
      principalId: [member.principalId],
      remplacantId: [member.secondId],
      fonctionId: [member.fonctionId]
    });

    memberFormArray.push(memberGroup); // Ajoute le membre au formArray
  });
}



createProgram(program: DailyProgramCreationForm){
  return this.http.post<DailyProgramCreationForm>("http://localhost:8081/bnsp/api/programs/create", program).subscribe();
}

addEquipeOnFiche(equipe: DailyProgramAddEquipeForm){
  return this.http.put<DailyProgramAddEquipeForm>("http://localhost:8081/bnsp/api/programs/team/add" , equipe).subscribe()
}

UpdateEquipe(equipe){
  return this.http.put("http://localhost:8081/bnsp/api/programs/team/update", equipe).subscribe()
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
        programId : this.idprogram,
        typeId: this.addEquipe.get('typeId').value,
        designation: this.addEquipe.get('designation').value,
        members: this.addEquipe.getRawValue().members
      }
      try {
        this.addEquipeOnFiche(equipe);
        window.location.reload();
        this.toastr.success('Equipe crée avec succès!');
      } catch {
        this.toastr.error('Formulaire invalide!');
      }
    }
  }

  changeidprogram(id: number){
    this.idprogram = id
    console.log(id)
  }

  onSubmitUpdate(){
    if(this.addEquipe.valid){
      let equipe: any = {
        teamId : this.teamId,
        typeId: this.addEquipe.get('typeId').value,
        designation: this.addEquipe.get('designation').value,
        members: this.addEquipe.getRawValue().members
      }
      try {
        this.UpdateEquipe(equipe);
        window.location.reload();
        this.toastr.success('Equipe mis a jour avec succès!');
      } catch {
        this.toastr.error('Formulaire invalide!');
      }
    }
  }

  saveIdSupp(id , date){
    this.idTeamSupp = id;
    this.dateprogram = date;
    console.log(this.idTeamSupp, this.dateprogram);
  }

  supprimerTeam(){
    this.http.delete("http://localhost:8081/bnsp/api/programs/team/"+ this.idTeamSupp).subscribe();
    setTimeout(() => {
      this.getProgram(this.dateprogram);
    },1000)
  }


  print(){
    window.print();
  }

  openCreate(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}
  openEdit(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

  openRead(content) {
		this.modalService.open(content, { size: 'xl', scrollable: true });
	}


  openSupp(content) {
		this.modalService.open(content, {size: 'xl', centered: true });
	}

}
