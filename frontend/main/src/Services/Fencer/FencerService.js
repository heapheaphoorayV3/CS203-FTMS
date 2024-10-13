import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }
}

export default new FencerService();