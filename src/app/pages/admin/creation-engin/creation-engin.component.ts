import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { EnginUpdateForm } from '@/model/EnginUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  listeEngin:any[]=[];
  page = 1; // Page actuelle
  pageSize = 5; // Nombre d'éléments par page
  collectionSize: number; // Taille totale de la collection


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
    setTimeout(() => {
      this.collectionSize = this.listeEngin.length;
    }, 2000)
  }

  // get donneesPaginees() {
  //   if (this.listeEngin === null) {
  //     return [];
  //   }

  //   const startIndex = (this.page - 1) * this.pageSize;
  //   const endIndex = startIndex + this.pageSize;
  //   return this.listeEngin.slice(startIndex, endIndex);
  // }
  get donneesPaginees() {
    if (this.listeEngin === null) {
      return this.listeEngin = [];
    }
    const totalPages = Math.ceil(this.listeEngin.length / this.pageSize);
    let startPage = Math.max(1, this.page - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.listeEngin.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
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
        this.listeEngin.push(...this.engin);
      }
    );
  }

  getEnginByCaserne(id: number){
    return this.http.get(this.url + "/engins/caserne/" + id).subscribe(
      engin => {
        this.engin = engin;
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
      this.toastr.success('Engin crée avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
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
