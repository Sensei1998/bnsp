import { Agent } from '@/model/Agent.model';
import { AgentCreationForm } from '@/model/AgentCreationForm.model';
import { AgentUpdateForm } from '@/model/AgentUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal, NgbModalConfig, NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '@services/api.service';
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


  caserne;
  grade;

  users;
  listUser:any[]=[];
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
  idCaserne = Number(localStorage.getItem('idCaserne'));
  NomCaserne = localStorage.getItem('Caserne');

  role = localStorage.getItem('fonction');
  isAdmin = false;
  supervisor= false;
  page = 1; // Page actuelle
  pageSize = 5; // Nombre d'éléments par page
  collectionSize: number; // Taille totale de la collection

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal,
    private service: ApiService) {
    config.backdrop = 'static';
		config.keyboard = false;
     }

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

    if( this.role === "ROLE_SUPERVISOR"){
      this.supervisor = true;

   } else{
     this.supervisor = false;

   }

    if( this.role === "ROLE_ADMINISTRATEUR"){
       this.isAdmin = true;

    } else{
      this.isAdmin = false;
    ;
    }

    setTimeout(() => {
      this.collectionSize = this.listUser.length;
    }, 2000)


  }


  get donneesPaginees() {
    if (this.listUser === null) {
      return this.listUser=[];
    }
    const totalPages = Math.ceil(this.listUser.length / this.pageSize);
    let startPage = Math.max(1, this.page - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.listUser.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }


  // get donneesPaginees() {
  //   if (this.listUser === null) {
  //     return [];
  //   }

  //   const startIndex = (this.page - 1) * this.pageSize;
  //   const endIndex = startIndex + this.pageSize;
  //   return this.listUser.slice(startIndex, endIndex);
  // }


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
    return this.service.getCaserne().subscribe(
      caserne => {
        this.caserne = caserne;
      }
    );
  }

  getAgentByCaserne(id: number){
      return this.service.getAgentByCaserne(id).subscribe(
        (user: Agent) => {
          this.users = user
        }
      );


  }



  getGrade(){
   return this.service.getGrade().subscribe(
    grade => {
      this.grade = grade
    }
   );
  }


  createAgent(agent: AgentCreationForm){
    return this.service.createAgent(agent).subscribe();
  }

  getAgent(){
    return this.service.getAgent().subscribe(
      (user: Agent) =>{
        this.users = user;
        this.listUser.push(...this.users);
      }
    );
  }

  onPageChange(page: number) {
    // Mettre à jour la page actuelle

  }

  updateAgent(agent: AgentUpdateForm){
    return this.service.updateAgent(agent).subscribe();
  }

  getAgentById(id: number){
    return this.service.getAgentById(id).subscribe(
      agent =>{
        this.agent = agent;
        let split = this.agent.phoneNumber.split(';');

        split.forEach(phoneNumber => {
          this.numero.push(this.formBuilder.control(phoneNumber));
          this.agentEditForm.setControl('telephone', this.formBuilder.array(split));
        });
      }
    );
  }

  deleteAgent(id: number){
    return this.service.deleteAgent(id).subscribe(
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
        this.createAgent(Agent);
        this.toastr.success('Agent crée avec succès!');
        window.location.reload();
      }else if (this.agentForm.invalid){
        this.toastr.error('Formulaire invalide!');
      } else {
        this.toastr.error('Formulaire invalide!');
      }


  }

  onEdit(){
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
