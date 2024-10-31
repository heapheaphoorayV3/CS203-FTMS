import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/organiser";

class OrganiserService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }
  
  async getAllHostedTournaments() {
    return await ProtectedAPI.get(`${baseURL}/tournaments`);
}

}

export default new OrganiserService();
