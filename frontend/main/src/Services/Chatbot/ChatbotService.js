import { ProtectedAPI } from "../ProtectedAPI";
import { API } from "../API";

const baseURL = "/chatbot";

class chatbotService {

    async getProjectedPoints(eid) {
        return await ProtectedAPI.get(`${baseURL}/projected-points/${eid}`);
    }

    async recommendTournaments(fencer) {
        return await ProtectedAPI.get(`${baseURL}/recommend-tournaments`, fencer);
    }

    async getWinRate(eid) {
        return await ProtectedAPI.get(`${baseURL}/winrate/${eid}`);
    }
}


export default new chatbotService;