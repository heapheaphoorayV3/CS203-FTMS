import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";

class EventService {

    async createEvents(tid, events) {
        return await ProtectedAPI.post(`${tid}/create-event`, events);
    }

    async getEventDetails(eid) {
        return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
    }
}

export default new EventService();