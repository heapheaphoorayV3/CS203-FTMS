import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async registerEvent(eid) {
    return await ProtectedAPI.get(`/tournament/register/${eid}`);
  }  
}

export default new FencerService();