import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { AgentCreationForm } from '@/model/AgentCreationForm.model';
import { AgentUpdateForm } from '@/model/AgentUpdateForm.model';
import { EquipeCreationForm } from '@/model/EquipeCreationForm.model';
import { EquipeUpdateForm } from '@/model/EquipeUpdateForm.model';

import { CaserneCreationForm } from '@/model/CaserneCreationForm.model';
import { CaserneUpdateForm } from '@/model/CaserneUpdateForm.model';
import { EnginCreationForm } from '@/model/EnginCreationForm.model';
import { EnginUpdateForm } from '@/model/EnginUpdateForm.model';
import { EnginUpdateAvailability } from '@/model/EnginUpdateAvailability.model';
import { EnginUpdateOut } from '@/model/EnginUpdateOut.model';
import { DailyProgramAddEquipeForm } from '@/model/DailyProgramAddEquipeForm.model';
import { DailyProgramCreationForm } from '@/model/DailyProgramCreationForm.model';
import { interventionCaserne } from '@/model/InterventionCaserne.model';
import { IncidentPartial } from '@/model/IncidentPartial.model';
import { interventionSheet } from '@/model/interventionSheet.model';
import { Message } from '@/model/message.model';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    url= "http://localhost:8081/bnsp/api";
    //url = "http://192.168.2.52:8081/bnsp/api";
    formData: any = {};
    id:number;
    constructor(private http: HttpClient) {}

    getGrade(){
      return this.http.get(this.url + "/users/grades");
    }


    getZone(){
      return this.http.get(this.url + "/casernes/zones");
    }


    createAgent(agent: AgentCreationForm){
      return this.http.post<AgentCreationForm>(this.url+"/users/create", agent);
    }

    updateAgent(agent: AgentUpdateForm){
      return this.http.put<AgentUpdateForm>(this.url+"/users/update", agent);
    }

    getAgent(){
      return this.http.get(this.url + "/users/");
    }

    getAgentById(id: number){
      return this.http.get(this.url + "/users/" + id);
    }

    getAgentByCaserne(id: number){
      return this.http.get(this.url + "/users/caserne/" + id);
    }

    getAgentGrade(){
      return this.http.get(this.url + "/users/grades");
    }

    deleteAgent(id: number){
      return this.http.delete(this.url + "/users/" + id );
    }

    createEquipe(equipe: EquipeCreationForm){
      return this.http.post<EquipeCreationForm>(this.url + "/teams/create" , equipe);
    }

    updateEquipe(equipe: EquipeUpdateForm){
      return this.http.put<EquipeUpdateForm>(this.url + "/teams/update", equipe);
    }

    getEquipe(){
      return this.http.get(this.url + "/teams");
    }

    getEquipeById(id: number){
      return this.http.get(this.url + "/teams/" + id);
    }

    getEquipeByCaserne(id: number){
      return this.http.get(this.url + "/teams/caserne/" + id);
    }

    getEquipeByTypes(){
      return this.http.get(this.url + "/teams/types");
    }

    deleteEquipe(id: number){
      return this.http.delete(this.url + "/teams/" + id);
    }





    createCaserne(caserne: CaserneCreationForm){
      return this.http.post<CaserneCreationForm>(this.url + "/casernes/create" , caserne);
    }

    updateCaserne(caserne: CaserneUpdateForm){
      return this.http.put<CaserneUpdateForm>(this.url + "/casernes/update" , caserne);
    }

    getCaserne(){
      return this.http.get(this.url + "/casernes");
    }

    getCaserneById(id: number){
      return this.http.get(this.url + "/casernes/" + id);
    }


    getCaserneTypesById(id: number){
      return this.http.get(this.url + "/casernes/types/" + id);
    }

    getCaserneAffliationById(id: number){
      return this.http.get(this.url + "/casernes/affiliation/" + id);
    }

    deleteCaserne(id: number){
      return this.http.delete(this.url + "/casernes/" + id);
    }



    createEngins(engin: EnginCreationForm){
      return this.http.post<EnginCreationForm>(this.url + "/engins/create", engin);
    }

    updateEngin(engin:  EnginUpdateForm){
      return this.http.put<EnginUpdateForm>(this.url + "/engins/update", engin);
    }

    updateEnginAvailable(available: EnginUpdateAvailability){
      return this.http.put<EnginUpdateAvailability>(this.url + "/engins/update/available", available);
    }

    updateEnginSortie(sortie: EnginUpdateOut){
      return this.http.put<EnginUpdateOut>(this.url + "/engins/update/sortie", sortie);
    }

    getEngin(){
      return this.http.get(this.url + "/engins");
    }

    getEnginById(id: number){
      return this.http.get(this.url + "/engins/" + id);
    }

    getEnginByCaserne(id: number){
      return this.http.get(this.url + "/engins/caserne/" + id);
    }

    getEnginByTypes(){
      return this.http.get(this.url + "/engins/types");
    }

    deleteEngin(id: number){
      return this.http.delete(this.url + "/engins/" + id);
    }

    getTeam() {
      return this.http.get(this.url + "/programs/team/types");
    }

    getAllProgram(){
      return this.http.get(this.url + "/programs");
    }

    getProgramByCaserne(id:number){
      return this.http.get(this.url + "/programs/caserne/"+id);
    }

    getProgram(date){
      const dateObj = new Date(date);
      const jour = dateObj.getDate();
      const mois = dateObj.getMonth() + 1; // Les mois sont indexés à partir de 0, donc on ajoute 1
      const annee = dateObj.getFullYear();

      // Formater la date avec le format "dd-MM-yyyy"
      const dateFormatee = `${jour.toString().padStart(2, '0')}-${mois.toString().padStart(2, '0')}-${annee}`;

        return this.http.get(this.url + "/programs/search?date=" +dateFormatee);
    }



createProgram(program: DailyProgramCreationForm){
  return this.http.post<DailyProgramCreationForm>(this.url + "/programs/create", program);
}

createDefaultProgram(){
  return this.http.post(this.url + "/programs/create/default", '');
}

getProgramConsulter(date){
  return this.http.get(this.url + "/programs/search?date=" +date);
}


addEquipeOnFiche(equipe: DailyProgramAddEquipeForm){
  return this.http.put<DailyProgramAddEquipeForm>(this.url + "/programs/team/add" , equipe);
}

UpdateEquipeProgram(equipe){
  return this.http.put(this.url + "/programs/team/update", equipe);
}

supprimerTeam(id: number){
  return this.http.delete(this.url + "/programs/team/"+ id);
}

getInterventionByCaserne(id: number){
  const url =`${this.url}/intervention/sheet/caserne/${id}`;
  return this.http.get(url);
}


getIntervention(){
  return this.http.get(this.url + "/intervention");
}


getInterventionDetailsById(id: number){
  return this.http.get(this.url + "/intervention?id="+id);
}

closeIntervention(id: number) {
  return this.http.put(this.url + "/intervention/update/close/" + id, '');
}

getLibelleBycategory(id: number){
  const url = `${this.url}/intervention/types?category=${id}`;
  return this.http.get(url);
}

getCategory(){
  return this.http.get(this.url + "/intervention/types/category");
}

addCaserneIntervention(Caserne: interventionCaserne){
  return this.http.put<interventionCaserne>(this.url + "/intervention/update/info", Caserne);
}

getProfil(){
  return this.http.get(this.url + "/users/profil");
}

createPartialIntervention(Incident : IncidentPartial){
  return this.http.post<IncidentPartial>(this.url + "/intervention/create/partial", Incident);
}

updateEquipIntervention(equipe: interventionSheet){
  return this.http.put<interventionSheet>(this.url + "/intervention/sheet/update/team", equipe);
}

transferSelectedItems(update) {
  return this.http.put(this.url + "/intervention/sheet/update", update);
}

deleteEquipeIntervention(id: number){
  return this.http.delete(this.url + "/intervention/sheet/delete/team/"+ id);
}

createMessage(message: Message){
  return this.http.post<Message>(this.url + "/intervention/sheet/message", message);
}

getMessage(idIntervention: number, idCaserne: number){
  return this.http.get(`${this.url}/intervention/sheet/message/intervention/${idIntervention}/caserne/${idCaserne}`);
}

deleteMessage(id:number){
  return this.http.delete(this.url + "/intervention/sheet/message/"+ id);

}

getInterventionBCot(id: number) {
  return this.http.get(`${this.url}/intervention/sheet/caserne/${id}`);
}

login(login){
  return this.http.post(this.url + "/users/login", login);
}

saveCaserne(id){
 return this.http.get(this.url + "/casernes/" + id);
}

}
