import axios from 'axios';
import config from "../Config";

const API_BASE_URL = config.backendURL;

let API = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

export { API };

