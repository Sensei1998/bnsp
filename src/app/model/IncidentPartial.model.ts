
export interface IncidentPartial {
  cctoId: number;
  date: Date;
  time: string;
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
