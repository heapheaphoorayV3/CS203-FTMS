import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/organiser";

class OrganiserService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }
  
}

export default new OrganiserService();
