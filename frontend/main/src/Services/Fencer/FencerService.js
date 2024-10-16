import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async completeProfile() {
    return await ProtectedAPI.put(`${baseURL}/complete-profile`);
  }
}

export default new FencerService();