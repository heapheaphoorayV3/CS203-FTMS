import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";

class EventService {
  async createEvents(tid, events) {
    return await ProtectedAPI.post(`${baseURL}/${tid}/create-event`, events);
  }

  async getEvent(eid) {
    return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
  }

  async registerEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/register/${eid}`);
  }

  async getPouleTable(eid) {
    return await ProtectedAPI.get(`${baseURL}/${eid}/get-poule-table`);
  }

  async createPoules(eid, data) {
    return await ProtectedAPI.post(`${baseURL}/${eid}/create-poules`, data);
  }

  async getRecommendedPoules(eid) {
    return await ProtectedAPI.get(`${baseURL}/${eid}/get-recommended-poules`);
  }

  async getMatches(eid) {
    return await ProtectedAPI.get(`${baseURL}/${eid}/get-direct-elimination-matches`);
  }
}

export default new EventService();
