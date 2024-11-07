import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/fencer";

class FencerService {
  async getProfile() {
    return await ProtectedAPI.get(`${baseURL}/profile`);
  }

  async completeProfile(data) {
    return await ProtectedAPI.put(`${baseURL}/complete-profile`, data);
  }

  async getAllFencers() {
    return await ProtectedAPI.get(`${baseURL}/all`);
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

  async getMenSabreRanking() {
    return await ProtectedAPI.get(`${baseURL}/men-sabre-ranking`);
  }

  async getWomenSabreRanking() {
    return await ProtectedAPI.get(`${baseURL}/women-sabre-ranking`);
  }

  async getMenEpeeRanking() {
    return await ProtectedAPI.get(`${baseURL}/men-epee-ranking`);
  }

  async getWomenEpeeRanking() {
    return await ProtectedAPI.get(`${baseURL}/women-epee-ranking`);
  }

  async getMenFoilRanking() {
    return await ProtectedAPI.get(`${baseURL}/men-foil-ranking`);
  }

  async getWomenFoilRanking() {
    return await ProtectedAPI.get(`${baseURL}/women-foil-ranking`);
  }
}

export default new FencerService();