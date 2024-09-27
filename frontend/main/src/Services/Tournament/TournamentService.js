import { ProtectedAPI } from "../ProtectedAPI";

const baseURL = "/tournament";

class TournamentService {
    async createTournament(tournament) {
        return await ProtectedAPI.post(`${baseURL}/create-tournament`, tournament);   
    }

    async createEvent(event) {
        return await ProtectedAPI.post(`${baseURL}/create-event`, event);
    }

    async getTournamentDetails(tid) {
        return await ProtectedAPI.get(`${baseURL}/tournament-details/${tid}`);
    }

    async getEventDetails(eid) {
        return await ProtectedAPI.get(`${baseURL}/event-details/${eid}`);
    }
}

export default new TournamentService();