import { CaserneCreationForm } from '@/model/CaserneCreationForm.model';
import { CaserneUpdateForm } from '@/model/CaserneUpdateForm.model';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '@services/api.service';
import { ToastrService } from 'ngx-toastr';
import { element } from 'protractor';

@Component({
  selector: 'app-creation-caserne',
  templateUrl: './creation-caserne.component.html',
  styleUrls: ['./creation-caserne.component.scss']
})
export class CreationCaserneComponent implements OnInit {

  caserneForm:FormGroup = this.formBuilder.group({
    idCaserneType: [[], Validators.required],
    idCaserneParent: [null],
    name: [[], Validators.required],
    city:[[], Validators.required],
    area: [[], Validators.required],
    telephone:this.formBuilder.array(['']),
    email:[[], Validators.required],
  });

  caserneUpdateForm:FormGroup = this.formBuilder.group({
    id: [[], Validators.required],
    idCaserneType: [[], Validators.required],
    idCaserneParent: [null],
    name: [[], Validators.required],
    city:[[], Validators.required],
    area: [[], Validators.required],
    telephone:this.formBuilder.array(['']),
    email:[[], Validators.required],
  });

  caserne;
  hideAffliation = 0;
  brigade;
  compagnie;
  edit;
  sort;
  sortaff;
  numero = this.formBuilder.array([]);
  page = 1; // Page actuelle
  pageSize = 5; // Nombre d'éléments par page
  collectionSize: number; // Taille totale de la collection
  listCaserne:any[]=[];
  zone;
  constructor(private formBuilder: FormBuilder,
    private service: ApiService,
    private toastr: ToastrService,
    private http: HttpClient,
    config: NgbModalConfig, private modalService: NgbModal){
    config.backdrop = 'static';
		config.keyboard = false;
    }

  ngOnInit(): void {
    this.getCaserne();
    this.getZone();
    setTimeout(() => {
      this.collectionSize = this.listCaserne.length;
    }, 2000)
  }

  // get donneesPaginees() {
  //   if (this.listCaserne === null) {
  //     return [];
  //   }

  //   const startIndex = (this.page - 1) * this.pageSize;
  //   const endIndex = startIndex + this.pageSize;
  //   return this.listCaserne.slice(startIndex, endIndex);
  // }

  get donneesPaginees() {
    if (this.listCaserne === null) {
      return this.listCaserne = [];
    }
    const totalPages = Math.ceil(this.listCaserne.length / this.pageSize);
    let startPage = Math.max(1, this.page - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.listCaserne.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }


  get number(): FormArray{
    return this.caserneForm.get('telephone') as FormArray;
  }

  addNumber(){
    this.number.push(this.formBuilder.control(''),);
  }
  deleteNumber(index: number){
    this.number.removeAt(index);
    this.number.markAsDirty()
  }

  get number2(): FormArray{
    return this.caserneUpdateForm.get('telephone') as FormArray;
  }

  addNumber2(){
    this.number2.push(this.formBuilder.control(''),);
  }
  deleteNumber2(index: number){
    this.number2.removeAt(index);
    this.number2.markAsDirty()
  }

  createCaserne(caserne: CaserneCreationForm){
    return this.service.createCaserne(caserne).subscribe();
  }

  getZone(){
    return this.service.getZone().subscribe(
      zone => {
        this.zone = zone;
      }
    )
  }

  getCaserne(){
    return this.service.getCaserne().subscribe(
      caserne => {
        this.caserne = caserne;
        this.listCaserne.push(...this.caserne)
      }
    );
  }

  getCaserneTypesById(id: number){
    return this.service.getCaserneTypesById(id).subscribe(
      caserne => {
        this.caserne = caserne;
        this.listCaserne.push(...this.caserne);
      }
    );
  }

  getCaserneAffliationById(id: number){
    return this.service.getCaserneAffliationById(id).subscribe(
      caserne => {
        this.caserne = caserne;
        this.listCaserne.push(...this.caserne);
      }
    );
  }

  sortByBrigarde(){
    let temp = this.caserne.filter(element => element.caserneType.id === 1   );
    this.brigade = temp;

  }
  sortByCompagnie(){
    let temp = this.caserne.filter(element => element.caserneType.id === 2   );
    this.compagnie = temp;

  }

  updateCaserne(caserne: CaserneUpdateForm){
    return this.service.updateCaserne(caserne).subscribe();
  }

  getCaserneById(id: number){
    return this.service.getCaserneById(id).subscribe(
      edit =>{
        this.edit = edit;
        let split = this.edit.phoneNumber.split(';');

        split.forEach(phoneNumber => {
          this.numero.push(this.formBuilder.control(phoneNumber));
          this.caserneUpdateForm.setControl('telephone', this.formBuilder.array(split));
        });

      }
    );
  }

  deleteCaserne(id: number){
    return this.service.deleteCaserne(id).subscribe(
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
        telephone: this.caserneForm.getRawValue().telephone,
        email: this.caserneForm.get("email").value,
      }
      this.createCaserne(Caserne);
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
        telephone: this.caserneUpdateForm.getRawValue().telephone,
        email: this.caserneUpdateForm.get("email").value,
      }

      this.updateCaserne(Caserne);
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
