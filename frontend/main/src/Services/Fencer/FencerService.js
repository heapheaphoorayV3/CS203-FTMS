import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async completeProfile(data) {
    return await ProtectedAPI.put(`${baseURL}/complete-profile`, data);
  }

  async getInternationalRanking() {
    return await ProtectedAPI.get(`${baseURL}/international-ranking`);
  }

  async getFencerUpcomingEvents() {
    return await ProtectedAPI.get(`${baseURL}/upcoming-events`);
  }

  async getFencerPastEvents() {
    return await ProtectedAPI.get(`${baseURL}/past-events`);
  }
}

export default new FencerService();