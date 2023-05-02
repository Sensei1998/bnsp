import { caserneType } from "./CaserneType.model";

export interface Caserne{
  id: number,
  caserneType: caserneType,
  name: string,
  city: string,
  area: string,
  phoneNumber: string,
  email: string
}
