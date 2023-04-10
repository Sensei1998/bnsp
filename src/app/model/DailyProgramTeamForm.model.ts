import { DailyProgramFunctionForm } from "./DailyProgramFunctionForm.model";

export interface DailyProgramTeamForm{
  equipeId: number;

  functionList: DailyProgramFunctionForm[];
}
