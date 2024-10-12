import { API } from "../API";

const baseURL = "/admin";

/* TODO
- Update getUnverifiedOrganisers() URL to GET from correct endpoint
*/
class AdminService {
  async getUnverifiedOrganisers(credentials) {
    return await API.get(`${baseURL}/unverified-organisation`, credentials);
  }

}

export default new AdminService();
