import { DailyProgramAddEquipeForm } from "./DailyProgramAddEquipeForm.model";

export interface DailyProgramTeamForm{
  typeId: number;

  designation: string;

  enginId:number;

  members: DailyProgramAddEquipeForm[];

}
