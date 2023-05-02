export interface DailyProgramAddEquipeForm{
  programId: number;

  typeId: number;

  designation: number;

  members: {
    principalId: number,
      remplacantId: number,
      fonctionId: number
  };
}
