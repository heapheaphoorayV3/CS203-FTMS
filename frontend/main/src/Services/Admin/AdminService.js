import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/admin";

/* TODO
- Update getUnverifiedOrganisers() URL to GET from correct endpoint
*/
class AdminService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async changePassword(data) {
    return await ProtectedAPI.put(`${baseURL}/change-password`, data);
  }

  async getUnverifiedOrganisers() {
    return await ProtectedAPI.get(`${baseURL}/unverified-organiser`);
  }

  async verifyOrganiser(organisers) {
    return await ProtectedAPI.put(`${baseURL}/verify-organiser`, organisers);
  }

}

export default new AdminService();
