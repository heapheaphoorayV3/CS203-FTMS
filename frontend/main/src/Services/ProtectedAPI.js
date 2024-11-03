import axios from "axios";
import AuthService from "./Authentication/AuthService";

// const API_BASE_URL = "http://localhost:8080/api/v1";
const API_BASE_URL = process.env.REACT_APP_API_URL;

let ProtectedAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

// Add a request interceptor --> add token to header
ProtectedAPI.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem("token");
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    } else {
      console.error("No token found in session storage");
    }
    return config;
  }
);

export { ProtectedAPI };