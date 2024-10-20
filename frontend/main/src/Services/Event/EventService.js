import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/event";

class EventService {

    async createEvent(tid, event) {
        return await ProtectedAPI.post(`${tid}/create-event`, event);
    }

    async getEventDetails(eid) {
        return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
    }
}

export default new EventService();