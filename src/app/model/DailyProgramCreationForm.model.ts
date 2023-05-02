export interface DailyProgramCreationForm{
  date: Date;
  caporal: {
      principal : number,
      remplacant : number
  },
  sergent: {
      principal : number,
      remplacant : number
  }
}
