import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";

class EventService {
  async createEvents(tid, events) {
    return await ProtectedAPI.post(`${baseURL}/${tid}/create-event`, events);
  }

  async getEventDetails(eid) {
    return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
  }

  async registerEvent(eid) {
    return await ProtectedAPI.put(`${baseURL}/register/${eid}`);
  }

  async getPouleTable(eid) {
    return await ProtectedAPI.get(`${baseURL}/${eid}/get-poule-table`);
  }
}

export default new EventService();
