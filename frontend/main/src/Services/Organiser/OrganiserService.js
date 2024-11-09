import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/organiser";

class OrganiserService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async getAllHostedTournaments() {
    return await ProtectedAPI.get(`${baseURL}/tournaments`);
  }

  async getUpcomingTournaments() {
    return await ProtectedAPI.get(`${baseURL}/upcoming-tournaments`);
  }

  async getAllOrganisers() {
    return await ProtectedAPI.get(`${baseURL}/all`);
  }
}

export default new OrganiserService();
