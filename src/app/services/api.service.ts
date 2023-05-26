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

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    url= "http://localhost:8081/bnsp/api";
    formData: any = {};
    id:number;
    constructor(private http: HttpClient) {}

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

}
