import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/admin";

/* TODO
- Update getUnverifiedOrganisers() URL to GET from correct endpoint
*/
class AdminService {
  async getUnverifiedOrganisers() {
    return await ProtectedAPI.get(`${baseURL}/unverified-organiser`);
  }

  async verifyOrganiser(organisers) {
    return await ProtectedAPI.post(`${baseURL}/verify-organiser`, organisers);
  }

}

export default new AdminService();
