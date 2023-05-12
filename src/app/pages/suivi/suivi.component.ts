import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {DateTime} from 'luxon';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '@services/api.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { interventionSheet } from '@/model/interventionSheet.model';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-suivi',
  templateUrl: './suivi.component.html',
  styleUrls: ['./suivi.component.scss'],
  providers: [NgbModalConfig, NgbModal],
})
export class SuiviComponent implements OnInit {
  date = DateTime.now();
  time = null;
  url = "http://localhost:8081/bnsp/api/"
  category;
  caserne;
  libelle;
  id = Number(localStorage.getItem('idCaserne'));
  id2;
  program;
  t;
  equipeForm: FormGroup = this.formBuilder.group({

    departure: ['00:00:00'],
    presentation: ['00:00:00'],
    available: ['00:00:00'],
    checkIn: ['00:00:00']
  });
  constructor(private router: Router, config: NgbModalConfig, private modalService: NgbModal
    , public service: ApiService, private http: HttpClient, private route: ActivatedRoute, private toastr: ToastrService,
    private formBuilder: FormBuilder) {
    config.backdrop = 'static';
		config.keyboard = false;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(param =>{
      if(param.get('id')){
        this.id2 = +param.get('id')!;
      this.getInterventionDetailsById(this.id2)
      }
    });

    this.getProgram(this.formatDate(this.date));
  }


  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd-MM-yyyy');
  }

  formatDate2(date) {
    return DateTime.fromISO(date).toFormat('yyyy-MM-dd');
  }


  formatHeure(heure) {
    return DateTime.fromISO(heure).toFormat('hh : mm');
  }

  updateEquipe(equipe: interventionSheet){
    return this.http.put<interventionSheet>("http://localhost:8081/bnsp/api/intervention/sheet/update", equipe).subscribe(
      data => console.log(data)
    );
  }

  onSubmit(id: number) {
    if (this.equipeForm.valid) {
      const equipe: interventionSheet = {
        interventionId: this.id2,
        caserneId: this.id,
        interventionSheet: [{
          equipeId:id,
          departure:this.formatDate2(this.date) + 'T' +  this.equipeForm.get('departure').value,
          presentation: this.formatDate2(this.date) + 'T' +  this.equipeForm.get('presentation').value,
          available:this.formatDate2(this.date)+ 'T' +  this.equipeForm.get('available').value,
          checkIn:this.formatDate2(this.date)+ 'T' +  this.equipeForm.get('checkIn').value,
        }]
      };
      console.log(equipe)
     this.updateEquipe(equipe);
    } else {
      this.toastr.error('Veuillez renseigner tous les champs obligatoires !');
    }
  }

  open(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

  getInterventionDetailsById(id: number){
    return this.http.get("http://localhost:8081/bnsp/api/intervention?id="+id).subscribe(
      (data : any) =>{
        if(data !== null){
          this.service.formData = data;
          // this.router.navigate(['/suivi']);
          console.log(data);
        }
      }
    )
  }

  getProgram(date){
    return this.http.get("http://localhost:8081/bnsp/api/programs/search?date=" +date).subscribe(
      (program:any) =>{
        if(program === null){
          this.http.post("http://localhost:8081/bnsp/api/programs/create/default", '').subscribe(
            (program:any) => {
              this.program = program.teams;

            }
          )
        } else{
          this.program = program.teams;
        }
      }
    )
}

transferSelectedItems() {
  const selectedItems = this.program.filter(item => item.selected);
  let update = {
    interventionId: this.service.formData.interventionId,
    caserneId: this.id,
    interventionSheet: selectedItems.map(item => ({ equipeId: item.teamId }))
  }
  return this.http.put("http://localhost:8081/bnsp/api/intervention/sheet/update", update).subscribe(
    data =>{
      window.location.reload();
    }
  )
}




}
