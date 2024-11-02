import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";
const pouleBaseURL = "/poule";
const DEBaseURL = "/direct-elimination";

class EventService {
  async createEvents(tid, events) {
    return await ProtectedAPI.post(`${baseURL}/${tid}/create-event`, events);
  }

  async getEvent(eid) {
    return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
  }

  async updateEvent(eid, data) {
    return await ProtectedAPI.put(`${baseURL}/${eid}/update-event`, data);
  }

  async registerEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/register/${eid}`);
  }

  async getPouleTable(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/${eid}/get-poule-table`);
  }

  async updatePouleTable(eid, data) {
    return await ProtectedAPI.put(`${pouleBaseURL}/${eid}/update-poule-table`, data);
  }

  async createPoules(eid, data) {
    return await ProtectedAPI.post(`${pouleBaseURL}/${eid}/create-poules`, data);
  }

  async getRecommendedPoules(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/${eid}/get-recommended-poules`);
  }

  async getMatches(eid) {
    return await ProtectedAPI.get(`${DEBaseURL}/${eid}/get-direct-elimination-matches`);
  }

  async getPoulesResult(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/${eid}/get-poules-result`);
  }

  async getEventRanking(eid) {
    return await ProtectedAPI.get(`${baseURL}/${eid}/get-event-ranking`);
  }

  async updateDEMatch(eid, data) {
    return await ProtectedAPI.put(`${DEBaseURL}/${eid}/update-direct-elimination-match`, data);
  }

  // TODO: Check Endpoint with Backend
  async deleteEvent(eid) {
    return await ProtectedAPI.delete(`${baseURL}/${eid}/delete-event`);
  }
}

export default new EventService();
