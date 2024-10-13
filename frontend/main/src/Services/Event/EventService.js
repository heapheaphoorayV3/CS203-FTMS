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
    return await ProtectedAPI.put(`/tournament/register/${eid}`);
  }
}

export default new EventService();
