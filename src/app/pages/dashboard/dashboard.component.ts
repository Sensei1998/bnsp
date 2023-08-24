import {Component, OnInit} from '@angular/core';
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

  constructor() { }

  ngOnInit() {
    this.chartOptions = {
      chart: {
        type: 'column'
      },
      title: {
        text: 'Exemple de graphique en barres'
      },
      xAxis: {
        categories: this.joursDuMois,
      crosshair: true
      },
      yAxis: {
        title: {
          text: 'Valeurs'
        }
      },
      series: [{
        name: 'Série 1',
        data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,194.1, 95.6, 54.4,48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0,
          83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5,106.6, 92.3]
      }]
    };

    Highcharts.chart('container', this.chartOptions);

    this.CharLine = {
      chart: {
        type: 'line'
      },
      title: {
        text: 'Exemple de graphique en barres'
      },
      xAxis: {
        categories: [
          'Jan',
          'Feb',
          'Mar',
          'Apr',
          'May',
          'Jun',
          'Jul',
          'Aug',
          'Sep',
          'Oct',
          'Nov',
          'Dec'
      ],
      crosshair: true
      },
      yAxis: {
        title: {
          text: 'Valeurs'
        }
      },
      series: [{
        name: 'Série 1',
        data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,194.1, 95.6, 54.4]
      }]
    };

    Highcharts.chart('container2', this.CharLine);
  }



}
