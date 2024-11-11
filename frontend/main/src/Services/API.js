import axios from 'axios';
import config from "../Config";

const API_BASE_URL = config.backendURL;

let API = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

API.interceptors.response.use(
  response => response,
  error => {
    if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
      error.response = {
        ...error.response,
        data: 'Request timed out. Please try again later.',
      };
    }
    return Promise.reject(error);
  }
);

export { API };

