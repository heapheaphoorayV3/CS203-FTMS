import { API } from "../API";

const baseURL = "/auth";

class AuthService {
  async loginUser(credentials) {
    return await API.post(`${baseURL}/login`, credentials);
  }

  async createFencer(user) {
    return await API.post(`/auth/register-fencer`, user);
  }

  async createOrganiser(user) {
    return await API.post(`${baseURL}/register-organiser`, user);
  }

  async forgetPassword(email) {
    return await API.get(`${baseURL}/forget-password/${email}`);
  }

  async resetPassword(token, password) {
    return await API.post(`${baseURL}/reset-password/${token}`, password);
  }
}

export default new AuthService();
