import { Time } from "@angular/common";

export interface IncidentPartial {
  cctoId: number;
  date: Date;
  time: Time;
  provenance: string;
  phoneNumber: string;
  name: string;
  address: string;
  longitude: number;
  latitude:number;
  precision: string;
  incident: {
    categoryId: number;
    incidentTypeId: number;
    comments: string;
  }
}
