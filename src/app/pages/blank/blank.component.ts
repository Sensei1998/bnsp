import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
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
  constructor(private http: HttpClient){}

  ngOnInit(): void {
    this.getInterventionByCaserne(this.idCaserne);
  }

  getInterventionByCaserne(id: number){
    const url =`${this.url}intervention/sheet/caserne/${id}`;
    return this.http.get(url).subscribe(
      (data : any) =>{
        this.listeIntervention = data;
        this.listeIntervention.forEach((element) => {
          this.key = element.key;
          this.intervention = this.key.intervention;
          this.incident = this.intervention.incident;
          console.log(this.intervention);
          console.log(this.incident);
        });
        for (let i = 0; i < this.listeIntervention.length; i++) {
          this.message = this.listeIntervention[i].message
          console.log(this.message);
        }
    }
    )
  }

}
