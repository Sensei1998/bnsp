import { CaserneCreationForm } from '@/model/CaserneCreationForm.model';
import { CaserneUpdateForm } from '@/model/CaserneUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
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

  caserneUpdateForm:FormGroup = this.formBuilder.group({
    id: [[], Validators.required],
    idCaserneType: [[], Validators.required],
    idCaserneParent: [null],
    name: [[], Validators.required],
    city:[[], Validators.required],
    area: [[], Validators.required],
  });

  caserne;
  hideAffliation = 0;
  brigade;
  compagnie;
  edit;
  sort;
  sortaff;

  constructor(private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal){
    config.backdrop = 'static';
		config.keyboard = false;
    }

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

  getCaserneTypesById(id: number){
    return this.http.get(this.url + "/casernes/types/" + id).subscribe(
      caserne => {
        this.caserne = caserne
      }
    );
  }

  getCaserneAffliationById(id: number){
    return this.http.get(this.url + "/casernes/affiliation/" + id).subscribe(
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

  updateCaserne(caserne: CaserneUpdateForm){
    return this.http.put<CaserneUpdateForm>(this.url + "/casernes/update" , caserne).subscribe();
  }

  getCaserneById(id: number){
    return this.http.get(this.url + "/casernes/" + id).subscribe(
      edit =>{
        this.edit = edit;
      }
    );
  }

  deleteCaserne(id: number){
    return this.http.delete(this.url + "/casernes/" + id).subscribe(
      del =>{
        this.toastr.success('Caserne supprimée avec succès!');
        window.location.reload();
      }
    );
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
      this.toastr.success('Caserne crée avec succès!');
    } else{
      this.toastr.error('Formulaire invalide!');
    }
  }

  onSubmit2(){

    if(this.caserneUpdateForm.valid){
      let Caserne:CaserneUpdateForm = {
        id: this.caserneUpdateForm.get("id").value,
        idCaserneType: this.caserneUpdateForm.get("idCaserneType").value,
        idCaserneParent: this.caserneUpdateForm.get("idCaserneParent").value,
        name: this.caserneUpdateForm.get("name").value,
        city: this.caserneUpdateForm.get("city").value,
        area: this.caserneUpdateForm.get("area").value,
      }
      this.updateCaserne(Caserne)
      window.location.reload();
      this.toastr.success('Caserne mise à jour avec succès!');
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
