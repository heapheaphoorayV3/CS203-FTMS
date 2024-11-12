import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/admin";

class AdminService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async getUnverifiedOrganisers() {
    return await ProtectedAPI.get(`${baseURL}/unverified-organiser`);
  }

  async verifyOrganiser(organisers) {
    return await ProtectedAPI.put(`${baseURL}/verify-organiser`, organisers);
  }

}

export default new AdminService();
