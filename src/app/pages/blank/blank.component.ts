import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { NgbPaginationConfig } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '@services/api.service';
import { error } from 'console';
import { ToastrService } from 'ngx-toastr';
@Component({
    selector: 'app-blank',
    templateUrl: './blank.component.html',
    styleUrls: ['./blank.component.scss']
})
export class BlankComponent implements OnInit {
  listeIntervention;
  url = "http://localhost:8081/bnsp/api/"
  idCaserne = Number(localStorage.getItem('idCaserne'));
  intervention;
  incident;
  key;
  message;
  role = localStorage.getItem('fonction');
  isAdmin: boolean;
  bcot: boolean;
  ccot:boolean;
  page = 1; // Page actuelle
  pageSize = 5; // Nombre d'éléments par page
  page2 = 1; // Page actuelle
  pageSize2 = 5; // Nombre d'éléments par page
  page3 = 1; // Page actuelle
  pageSize3 = 5; // Nombre d'éléments par page
  collectionSize: number; // Taille totale de la collection
  collectionSize2: number; // Taille totale de la collection
  collectionSize3: number; // Taille totale de la collection
  enCours: any[]=[];
  enAttente:any[]=[];
  terminer:any[]=[];

  constructor(private http: HttpClient,private toastr: ToastrService, private service : ApiService,
    private router: Router, ){

    }

  ngOnInit(): void {

    if (this.role === "ROLE_SUPERVISOR" || this.role === "ROLE_ADMINISTRATEUR") {
      this.isAdmin = true;

    } else {
      this.isAdmin = false;

    }
    if (this.role === "ROLE_BCOT") {
      this.bcot = true;

    } else {
      this.bcot = false;

    }
    if (this.role === "ROLE_CCOT") {
      this.ccot = true;

    } else {
      this.ccot = false;

    }
    if(this.role === "ROLE_BCOT"){
      this.getInterventionByCaserne();
    }else{
      this.getIntervention();
    }

    setTimeout(() => {
      this.collectionSize = this.enAttente.length;
    }, 2000)
    setTimeout(() => {
      this.collectionSize2 = this.enCours.length;
    }, 2000)
    setTimeout(() => {
      this.collectionSize3 = this.terminer.length;
    }, 2000)
  }

  // get donneesPaginees() {
  //   return this.enAttente
  //     .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  // }
  // get donneesPaginees2() {
  //   return this.enCours
  //     .slice((this.page2 - 1) * this.pageSize2, (this.page2 - 1) * this.pageSize2 + this.pageSize2);
  // }
  // get donneesPaginees3() {
  //   return this.terminer
  //     .slice((this.page3 - 1) * this.pageSize3, (this.page3 - 1) * this.pageSize3 + this.pageSize3);
  // }

  get donneesPaginees() {
    if (this.enAttente === null) {
      return [];
    }

    const totalPages = Math.ceil(this.enAttente.length / this.pageSize);
    let startPage = Math.max(1, this.page - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.enAttente.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }

  get donneesPaginees2() {
    if (this.enCours === null) {
      return [];
    }

    const totalPages = Math.ceil(this.enCours.length / this.pageSize2);
    let startPage = Math.max(1, this.page2 - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.enCours.slice((this.page2 - 1) * this.pageSize2, (this.page2 - 1) * this.pageSize2 + this.pageSize2);
  }

  get donneesPaginees3() {
    if (this.terminer === null) {
      return [];
    }

    const totalPages = Math.ceil(this.terminer.length / this.pageSize3);
    let startPage = Math.max(1, this.page3 - 1);
    let endPage = Math.min(startPage + 2, totalPages);

    if (endPage - startPage < 2) {
      startPage = Math.max(1, endPage - 2);
    }

    return this.terminer.slice((this.page3 - 1) * this.pageSize3, (this.page3 - 1) * this.pageSize3 + this.pageSize3);
  }


  // getInterventionByCaserne(){
  //   const url =`${this.url}intervention/sheet/caserne/${this.idCaserne}`;
  //   return this.http.get(url).subscribe(
  //     (data : any) =>{
  //       if(data !== null){
  //        this.listeIntervention = data;
  //       this.listeIntervention.forEach((element) => {
  //         this.key = element.key;
  //         this.intervention = this.key.intervention;
  //         this.incident = this.intervention.incident;
  //       });
  //       for (let i = 0; i < this.listeIntervention.length; i++) {
  //         this.message = this.listeIntervention[i].message
  //         console.log(this.intervention);
  //       }
  //       }else{
  //         this.toastr.error('Aucune intervention pour le moment!');
  //       }
  //   }
  //   )
  // }
  getInterventionByCaserne(){
    const url =`${this.url}intervention/sheet/caserne/${this.idCaserne}`;
    return this.http.get(url).subscribe(
      (data : any) =>{
        if(data !== null){
          this.listeIntervention = data;
          this.enAttente.push(...this.listeIntervention.filter(intervention => intervention.status === 'En attente' || intervention.status === 'Non attribué'));
          this.enCours.push(...this.listeIntervention.filter(intervention => intervention.status === 'En cours' ));
          this.terminer.push(...this.listeIntervention.filter(intervention => intervention.status === 'Termine'));
        }else{
          this.toastr.error('Aucune intervention pour le moment!');
        }
    }
    )
  }

  getIntervention(){
    return this.http.get("http://localhost:8081/bnsp/api/intervention").subscribe(
      (data : any) =>{
        if(data !== null){
          this.listeIntervention = data;
          this.enAttente.push(...this.listeIntervention.filter(intervention => intervention.status === 'En attente' || intervention.status === 'Non attribué'));
          this.enCours.push(...this.listeIntervention.filter(intervention => intervention.status === 'En cours' ));
          this.terminer.push(...this.listeIntervention.filter(intervention => intervention.status === 'Termine'));
        }else{
          this.toastr.error('Aucune intervention pour le moment!');
        }
    }
    )
  }

  getInterventionDetailsById(id: number){
    return this.http.get("http://localhost:8081/bnsp/api/intervention?id="+id).subscribe(
      (data : any) =>{
        if(data !== null){
          this.service.formData = data;
          this.router.navigate(['/suivi']);
        }
      }
    )
  }

  ajouterUneCaserne(intervention){
    this.service.formData = intervention
    this.router.navigate(['/ajouter-compagnie']);
  }

  closeIntervention(id: number) {
    return this.http.put("http://localhost:8081/bnsp/api/intervention/update/close/" + id, '').subscribe(
      (data: any) => {
        window.location.reload();
      },
      (error) => {
        this.toastr.error("Tous les véhicules de toutes les casernes actifs sur l'intervention  ne sont pas encore disponibles.");
      }
    );
  }

}
