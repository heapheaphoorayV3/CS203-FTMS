import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/admin";

/* TODO
- Update getUnverifiedOrganisers() URL to GET from correct endpoint
*/
class AdminService {
  async getUnverifiedOrganisers() {
    return await ProtectedAPI.get(`${baseURL}/unverified-organisation`);
  }

  async verifyOrganiser(organisers) {
    return await ProtectedAPI.post(`${baseURL}/verify-organisation`, organisers);
  }

}

export default new AdminService();
