import { DailyProgramTeamForm } from "./DailyProgramTeamForm.model";

export interface DailyProgramCreationForm{
  date: Date;

  teamProgramList: DailyProgramTeamForm[];
}
