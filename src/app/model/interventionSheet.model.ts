export interface interventionSheet{
  interventionId: number,
  caserneId: number,
  interventionSheet:[{
    equipeId: number,
    departure: string,
    presentation:string,
    available: string,
    checkIn:string
  }]

}
