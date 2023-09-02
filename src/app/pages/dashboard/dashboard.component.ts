import { DatePipe } from '@angular/common';
import {Component, OnInit} from '@angular/core';
import { ApiService } from '@services/api.service';
import { map } from 'rxjs/operators';
import * as Highcharts from 'highcharts';
import {DateTime} from 'luxon';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit{
  chartOptions: {};
  CharLine:{};
  date = DateTime.now();
  date2 = this.formatDate(this.date);
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
  periodecategorie:boolean = false;
  categorie;
  categorietableau = 1;
  categorieTotal = 1;


  constructor(private datePipe: DatePipe, private service: ApiService, private toast: ToastrService) { }


  ngOnInit() {
    this.getCategory();
    setInterval(() =>{
      this.getTodayStat();
    },1000);

  }

  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd-MM-yyyy');
  }

  chargeCategorie(id: number){
    this.categorietableau = Number(id);
  }

  chargeCategorieTotal(id: number){
    this.categorieTotal = Number(id);
  }


  changeperiodecategorie(){
    if(this.periodecategorie === false){
      this.periodecategorie = true;
      console.log(this.periodecategorie)
    } else {
      this.periodecategorie = false;
      console.log(this.periodecategorie)
    }

  }

  getCategory(){
    return this.service.getCategory().subscribe(
      data => {
        this.categorie = data;
      }
    )
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

  getStatByCategorie(id: number) {
    return this.service.getStatByCategorie(id).subscribe(
      (result: any) => {
        this.nonattribuer1 = result["Non attribué"];
        this.enattente1 = result["En attente"];
        this.encours1 = result["En cours"];
        this.terminer1 = result["Termine"];
        this.total1 = (this.nonattribuer1+this.enattente1+this.encours1+this.terminer1);
      }
    )
  }


  getPeriodStat(start,end){
    start = this.datePipe.transform(start, 'dd-MM-yyyy');
    end = this.datePipe.transform(end, 'dd-MM-yyyy');
    if(start !== null && end !== null){
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
    }else {
      this.toast.error("Veuillez verifier les dates s'il vous plait!!!!")
    }

  }


  getPeriodStatWithFiltre(categorie,start,end){
    start = this.datePipe.transform(start, 'dd-MM-yyyy');
    end = this.datePipe.transform(end, 'dd-MM-yyyy');
    let foundItem = this.categorie.find(item => item.id === this.categorietableau);
    if(start !== null && end !== null && categorie !== null){
      return this.service.getStatByPeriodAndCategories(categorie,start, end).pipe(
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
              text: `Total d\'intervention pour la période du ${start} au ${end} pour la categorie ${foundItem.category}`
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
    }else {
      this.toast.error("Veuillez verifier les dates s'il vous plait!!!!")
    }

  }
}
