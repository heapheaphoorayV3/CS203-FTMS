import { ProtectedAPI } from "../ProtectedAPI";
import { API } from "../API";

const baseURL = "/tournament";

class TournamentService {
    async createTournament(tournament) {
        return await ProtectedAPI.post(`${baseURL}/create-tournament`, tournament);   
    }

    async getTournamentDetails(tid) {
        return await ProtectedAPI.get(`${baseURL}/tournament-details/${tid}`);
    }

    async getAllTournaments() {
        return await ProtectedAPI.get(`${baseURL}/tournaments`);
        // return await API.get(`${baseURL}/tournaments`);
    }
}

export default new TournamentService();