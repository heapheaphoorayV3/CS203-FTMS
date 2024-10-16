import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async completeProfile(data) {
    return await ProtectedAPI.put(`${baseURL}/complete-profile`, data);
  }
}

export default new FencerService();