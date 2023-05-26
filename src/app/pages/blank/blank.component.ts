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
  pageSize = 10; // Nombre d'éléments par page
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
    this.getIntervention();
    if (this.role === "ROLE_SUPERVISOR" || this.role === "ROLE_ADMINISTRATEUR") {
      this.isAdmin = true;

    } else {
      this.isAdmin = false;

    }
    if (this.role === "ROLE_BCTO") {
      this.bcot = true;

    } else {
      this.bcot = false;

    }
    if (this.role === "ROLE_CCOT") {
      this.ccot = true;

    } else {
      this.ccot = false;

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

  get donneesPaginees() {
    return this.enAttente
      .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }
  get donneesPaginees2() {
    return this.enCours
      .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }
  get donneesPaginees3() {
    return this.terminer
      .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }

  getInterventionByCaserne(id: number){
    const url =`${this.url}intervention/sheet/caserne/${id}`;
    return this.http.get(url).subscribe(
      (data : any) =>{
        if(data !== null){
         this.listeIntervention = data;
        this.listeIntervention.forEach((element) => {
          this.key = element.key;
          this.intervention = this.key.intervention;
          this.incident = this.intervention.incident;
        });
        for (let i = 0; i < this.listeIntervention.length; i++) {
          this.message = this.listeIntervention[i].message
          console.log(this.message);
        }
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
          this.terminer.push(...this.listeIntervention.filter(intervention => intervention.status === 'Terminer'));
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



}
