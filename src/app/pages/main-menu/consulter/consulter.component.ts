import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {DateTime} from 'luxon';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '@services/api.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { interventionSheet } from '@/model/interventionSheet.model';
import { DatePipe } from '@angular/common';
import { Message } from '@/model/message.model';

interface Time {
  hours: number;
  minutes: number;
  seconds: number;
}


@Component({
  selector: 'app-consulter',
  templateUrl: './consulter.component.html',
  styleUrls: ['./consulter.component.scss']
})
export class ConsulterComponent implements OnInit{
  date = DateTime.now();
  time = null;
  category;
  caserne;
  libelle;
  id = Number(localStorage.getItem('idCaserne'));
  id2;
  program;
  t;
  enginSupp: number;
  equipeForm: FormGroup = this.formBuilder.group({

    departure: [''],
    presentation: [''],
    available: [''],
    checkIn: ['']
  });

  messageForm: FormGroup = this.formBuilder.group({
    equipeId:[[], Validators.required],
    message: [[],Validators.required]
  })

  interventionTeamId;
  type;
  departure;
  presentation;
  available;
  checkIn;
  Team;
  message;
  idMessage;
  role = localStorage.getItem('fonction');
  isAdmin: boolean;
  bcot: boolean;
  ccot:boolean;
  constructor(private router: Router, config: NgbModalConfig, private modalService: NgbModal
    , public service: ApiService, private http: HttpClient, private route: ActivatedRoute, private toastr: ToastrService,
    private formBuilder: FormBuilder,private datePipe: DatePipe) {
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
    setTimeout(() => {
      this.getMessage(this.service.formData.interventionId,this.id);
    }, 2000)
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
  }


  formatDate(date) {
    return DateTime.fromISO(date).toFormat('dd-MM-yyyy');
  }

  formatDate2(date) {
    return DateTime.fromISO(date).toFormat('yyyy-MM-dd');
  }

  formatDate3(date) {
    return DateTime.fromISO(date).toFormat('yyyy-MM-dd T:ss');
  }


  formatHeure(heure) {
    return DateTime.fromISO(heure).toFormat('hh:mm:ss');
  }

  updateEquipe(equipe: interventionSheet){
    return this.service.updateEquipIntervention(equipe).subscribe(
      data => window.location.reload()
    );
  }

  onSubmit(id: number) {
    let equipeId: number = id;
let departure: string = this.equipeForm.get('departure').value;
let presentation: string = this.equipeForm.get('presentation').value;
let available: string = this.equipeForm.get('available').value;
let checkIn: string = this.equipeForm.get('checkIn').value;

let interventionSheet: interventionSheet = {
  // interventionId: this.id2,
  // caserneId: this.id,
  // interventionSheet: [
  //   {
  //     interventionTeamId: equipeId,
  //     departure: '',
  //     presentation: '',
  //     available: '',
  //     checkIn: '',
  //   }
  // ],
  interventionTeamId: equipeId,
  departure: '',
  presentation: '',
  available: '',
  checkIn: '',
};


/* if (departure === null) {
  interventionSheet.departure = null;
  interventionSheet.presentation = null;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (presentation === null) {
  interventionSheet.departure = this.formatDate2(this.date) + 'T' + departure;
  interventionSheet.presentation = null;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (available === null) {
  interventionSheet.departure = this.formatDate2(this.date) + 'T' + departure;
  interventionSheet.presentation = this.formatDate2(this.date) + 'T' + presentation;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (checkIn === null) {
  interventionSheet.departure = this.formatDate2(this.date) + 'T' + departure;
  interventionSheet.presentation = this.formatDate2(this.date) + 'T' + presentation;
  interventionSheet.available = this.formatDate2(this.date) + 'T' + available;
  interventionSheet.checkIn = null;
} else {
  interventionSheet.departure = this.formatDate2(this.date) + 'T' + departure;
  interventionSheet.presentation = this.formatDate2(this.date) + 'T' + presentation;
  interventionSheet.available = this.formatDate2(this.date) + 'T' + available;
  interventionSheet.checkIn = this.formatDate2(this.date) + 'T' + checkIn;
} */
if (departure === null) {
  interventionSheet.departure = null;
  interventionSheet.presentation = null;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (presentation === null) {
  interventionSheet.departure =  departure;
  interventionSheet.presentation = null;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (available === null) {
  interventionSheet.departure =  departure;
  interventionSheet.presentation =  presentation;
  interventionSheet.available = null;
  interventionSheet.checkIn = null;
} else if (checkIn === null) {
  interventionSheet.departure =  departure;
  interventionSheet.presentation =  presentation;
  interventionSheet.available =  available;
  interventionSheet.checkIn = null;
} else {
  interventionSheet.departure =  departure;
  interventionSheet.presentation =  presentation;
  interventionSheet.available =  available;
  interventionSheet.checkIn =  checkIn;
}

if (departure === null) {
  this.toastr.error('Veuillez renseigner tous les champs par ordre en commençant par le départ !');
} else if (this.equipeForm.valid) {
  const equipe: {
    // interventionId: number;
    // caserneId: number;
    interventionTeamId: typeof interventionSheet.interventionTeamId,
    departure: typeof interventionSheet.departure,
    presentation: typeof interventionSheet.presentation,
    available: typeof interventionSheet.available,
    checkIn: typeof interventionSheet.checkIn,
  } = {
    // interventionId: interventionSheet.interventionId,
    // caserneId: interventionSheet.caserneId,
    interventionTeamId:  interventionSheet.interventionTeamId,
    departure:  interventionSheet.departure,
    presentation:  interventionSheet.presentation,
    available:  interventionSheet.available,
    checkIn:  interventionSheet.checkIn,
  };
  this.updateEquipe(equipe);
}else {
      this.toastr.error('Veuillez renseigner tous les champs obligatoires !');
    }
  }

  edit(id: number,type, departure, presentation, available, checkIn){
    this.interventionTeamId = id;
    this.type = type;
    this.departure = departure;
    this.presentation = presentation;
    this.available = available;
    this.checkIn = checkIn;
  }

  teamMessage(team){
    this.Team = team;
  }

  open(content) {
		this.modalService.open(content, { size: 'xl', centered: true, scrollable: true });
	}

  open2(content) {
		this.modalService.open(content, { size: 'xl', centered: true});
	}

  getInterventionDetailsById(id: number){
    return this.service.getInterventionDetailsById(id).subscribe(
      (data : any) =>{
        if(data !== null){
          this.service.formData = data;
          // this.router.navigate(['/suivi']);

        }
      }
    )
  }

 convertToTime(timeString: string): Date {
  if (timeString !== null) {
    const formattedDate = this.datePipe.transform(timeString, 'dd-MM-yyyyTHH:mm:ss');
    // can i have an exemple of usage of Datepipe?

    return new Date(formattedDate);
  } else {
    // Gérez le cas où timeString est null

    return null;
  }
}



  formatTime(date: string): string {
    if (!date) return '';
    const time = date.substr(11, 8);
    const timeParts = time.split(':');
    const hours = parseInt(timeParts[0], 10);
    const minutes = parseInt(timeParts[1], 10);
    const seconds = parseInt(timeParts[2], 10);
    const formattedDate = new Date();
    formattedDate.setHours(hours, minutes, seconds);
    const formattedTime = formattedDate.toISOString().substr(11, 8);
    return formattedTime;
  }
  timeToString(time: Time): string {
    if (!time) return '';
    const hours = time.hours.toString().padStart(2, '0');
    const minutes = time.minutes.toString().padStart(2, '0');
    const seconds = time.seconds.toString().padStart(2, '0');
    return `${hours}:${minutes}:${seconds}`;
  }
  format(Heure) {
    this.date = this.formatTime(Heure)
    return this.date.fromISO(Heure).toFormat('HH:mm:ss')
  }

  extractTime(datetime: string): string {
    if (datetime) {
      return datetime.slice(11, 19);
    } else {
      return '';
    }
  }

  getProgram(date){
    return this.service.getProgramConsulter(date).subscribe(
      (program:any) =>{
        if(program === null){
          this.service.createDefaultProgram().subscribe(
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
    teamId: selectedItems.map(item =>  item.teamId ),
  }
  return this.service.transferSelectedItems(update).subscribe(
    data =>{
      window.location.reload();
  }
  )
}

deleteEquipe(id: number){
  return this.service.deleteEquipeIntervention(id).subscribe(
    data => {
      window.location.reload();
    }
  )
}

enginsupp(id: number){
  this.enginSupp = id
}

createMessage(message: Message){
  return this.service.createMessage(message).subscribe(
    data => window.location.reload()
  )
}

getMessage(idIntervention: number, idCaserne: number){
  return this.service.getMessage(idIntervention,idCaserne).subscribe(
  data => {
    this.message = data;
  }
  )
}

updateMessage(){
  if(this.messageForm.valid){
     let message:Message={
    interventionId: this.service.formData.interventionId,
    equipeId: this.messageForm.get('equipeId').value,
    caserneId: this.id,
    message: this.messageForm.get('message').value,
  }
  this.toastr.success('Message Bien Enregistrer!');
  this.createMessage(message);
  } else {
    this.toastr.error('Veuillez renseigner tous les champs');
  }
}

affetcIdmessage(id:number){
  this.idMessage = id
}

deleteMessage(id:number){
  return this.service.deleteMessage(id).subscribe(
    data => {
      window.location.reload();
    }
  )

}


}
