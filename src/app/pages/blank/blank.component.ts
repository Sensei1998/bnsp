import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
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
  constructor(private http: HttpClient,private toastr: ToastrService, private service : ApiService,
    private router: Router){}

  ngOnInit(): void {
    this.getIntervention();
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
          console.log(data)
         this.listeIntervention = data;
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



}
