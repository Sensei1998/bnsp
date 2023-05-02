import { Agent } from "./AgentLongin.model";

export interface Login {
  agent: Agent;
  status: string;
  token: string;
}
