import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
@Component({
    selector: 'app-notifications',
    templateUrl: './notifications.component.html',
    styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit{
  count: number = 0;
  save: number = 0;
  role = localStorage.getItem('fonction');
  Caserne = Number(localStorage.getItem('idCaserne'));
  interventions : any[] ;
  constructor(private http: HttpClient){}

  ngOnInit(): void {
      setInterval(()=> {
        if( this.role === "ROLE_SUPERVISOR"){
          this.getInterventionCCot();
          if(this.save !== this.count){
            this.save = this.count;
            this.playSound();
          }
       } else if(this.role === "ROLE_CCOT"){
        this.getInterventionCCot();
        if(this.save !== this.count){
          this.save = this.count;
          this.playSound();
        }
       } else if(this.role === "ROLE_BCOT"){
        setTimeout(() => {
          this.getInterventionBCot();
        if(this.save !== this.count){
          this.save = this.count;
          this.playSound();
        }
        }, 1000);

       } else if (this.role === "ROLE_ADMINISTRATEUR"){
        setTimeout(() => {
          this.getInterventionBCot();
        if(this.save !== this.count){
          this.save = this.count;
          this.playSound();
        }
        }, 1000);
      };
      }, 1000);

  }

  getIntervention(){
    return this.http.get("http://localhost:8081/bnsp/api/intervention").subscribe(
      (data : any) =>{
        if(data === null){
          this.count = 0;
        }else{
           this.count = data.length;
          this.interventions = data;
        }

    }
    )
  }

  getInterventionCCot() {
    return this.http.get("http://localhost:8081/bnsp/api/intervention").subscribe(
      (data: any) => {
        if (data === null) {
          this.count = 0;
        } else {
          const filteredData = data.filter((item: any) => {
            return item.status === 'En attente' || item.status === 'Non attribué';
          });
          this.count = filteredData.length;
          this.interventions = filteredData
        }
      }
    );
  }

  getInterventionBCot() {
    return this.http.get(`http://localhost:8081/bnsp/api/intervention/sheet/caserne/${this.Caserne}`).subscribe(
      (data: any) => {
        if (data === null) {
          this.count = 0;
        } else {
          const filteredData = data.filter((item: any) => {
            return item.status === 'En attente';
          });
          this.count = filteredData.length;
          this.interventions=filteredData

        }
      }
    );
  }


  playSound(){
    let audio = new Audio();
    audio.src = "assets/sound/notification.m4a";
    audio.load();
    audio.play();
  }


}
