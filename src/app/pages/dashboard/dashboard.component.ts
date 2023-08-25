import { DatePipe } from '@angular/common';
import {Component, OnInit} from '@angular/core';
import { ApiService } from '@services/api.service';
import { map } from 'rxjs/operators';
import * as Highcharts from 'highcharts';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit{
  chartOptions: {};
  CharLine:{};
  joursDuMois = Array.from({ length: 31 }, (_, i) => (i + 1).toString());

  //pour la journée d'aujourd'hui
  total = 0;
  nonattribuer = 0;
  enattente = 0;
  encours = 0;
  terminer = 0;

  //pour une période précise
  total1 = 0;
  nonattribuer1 = 0;
  enattente1 = 0;
  encours1 = 0;
  terminer1 = 0;
  dateDebut;
  dateFin;
  tableauDeDonnees: any[] = [];


  constructor(private datePipe: DatePipe, private service: ApiService) { }

  ngOnInit() {
    setInterval(() =>{
      this.getTodayStat();
    },1000);

  }

  getTodayStat() {
    return this.service.getStatToday().subscribe(
      (result: any) => {
        this.nonattribuer = result["Non attribué"];
        this.enattente = result["En attente"];
        this.encours = result["En cours"];
        this.terminer = result["Termine"];
        this.total = (this.nonattribuer+this.enattente+this.encours+this.terminer);
      }
    )
  }

  getPeriodStat(start,end){
    start = this.datePipe.transform(start, 'dd-MM-yyyy');
    end = this.datePipe.transform(end, 'dd-MM-yyyy');
    return this.service.getStatByPeriod(start, end).pipe(
      map(data => {
        // Convertir la réponse JSON en tableau d'objets
        return Object.entries(data)
          .map(([date, valeur]) => ({ date, valeur }))
          .filter(item => item.date !== 'all'); // Filtrer la date 'all'
      })
    ).subscribe(tableauDeDonnees => {
      // Trier les dates par ordre croissant
      tableauDeDonnees.sort((a, b) => a.date.localeCompare(b.date));

      // Remplacez le contenu du tableau par le nouveau tableau
      setTimeout(()=>{
        this.chartOptions = {
          chart: {
            type: 'column'
          },
          title: {
            text: `Total d\'intervention pour la période du ${start} au ${end}`
          },
          xAxis: {
            categories: tableauDeDonnees.map(item => item.date),
            crosshair: true
          },
          yAxis: {
            title: {
              text: 'Nombre d\'Intervention'
            }
          },
          series: [{
            name: 'Nombre Total d\'Intervention',
            data: tableauDeDonnees.map(item => item.valeur),
          }]
        };

        Highcharts.chart('container', this.chartOptions);
      }, 2000)

    });


  }


}
