import { DailyProgramTeamForm } from "./DailyProgramTeamForm.model";


export interface DailyProgramUpdateForm{
  dailyProgramId: number;

  date: Date;

  teamId: number;

  teamProgramList: DailyProgramTeamForm[];
}

