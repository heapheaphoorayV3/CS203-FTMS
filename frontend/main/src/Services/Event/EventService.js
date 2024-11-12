import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";
const pouleBaseURL = "/poule";
const DEBaseURL = "/direct-elimination";

class EventService {
  async createEvents(tid, events) {
    return await ProtectedAPI.post(`${baseURL}/create-event/${tid}`, events);
  }

  async getEvent(eid) {
    return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
  }

  async updateEvent(eid, data) {
    return await ProtectedAPI.put(`${baseURL}/update-event/${eid}`, data);
  }

  async registerEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/register/${eid}`);
  }

  async unregisterEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/unregister/${eid}`);
  }

  async getPouleTable(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/get-poule-table/${eid}`);
  }

  async updatePouleTable(eid, data) {
    return await ProtectedAPI.put(`${pouleBaseURL}/update-poule-table/${eid}`, data);
  }

  async createPoules(eid, data) {
    return await ProtectedAPI.post(`${pouleBaseURL}/create-poules/${eid}`, data);
  }

  async getRecommendedPoules(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/get-recommended-poules/${eid}`);
  }

  async getMatches(eid) {
    return await ProtectedAPI.get(`${DEBaseURL}/get-direct-elimination-matches/${eid}`);
  }

  async getPoulesResult(eid) {
    return await ProtectedAPI.get(`${pouleBaseURL}/get-poules-result/${eid}`);
  }

  async getEventRanking(eid) {
    return await ProtectedAPI.get(`${baseURL}/get-event-ranking/${eid}`);
  }

  async createDEMatches(eid) {
    return await ProtectedAPI.post(`${DEBaseURL}/create-direct-elimination-matches/${eid}`);
  }

  async updateDEMatch(eid, data) {
    return await ProtectedAPI.put(`${DEBaseURL}/update-direct-elimination-match/${eid}`, data);
  }

  // TODO: Check Endpoint with Backend
  async deleteEvent(eid) {
    return await ProtectedAPI.delete(`${baseURL}/delete-event/${eid}`);
  }

  async endEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/end-event/${eid}`);
  }
}

export default new EventService();
