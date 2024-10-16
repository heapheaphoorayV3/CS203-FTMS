import axios from "axios";
import AuthService from "./Authentication/AuthService";

const API_BASE_URL = "http://localhost:8080/api/v1";

let ProtectedAPI = axios.create({
  baseURL: API_BASE_URL,
  timeout: 3000,
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
  },
  (error) => {
    // Do something with request error if needed
    if (error.status === 401) {
      const refreshToken = sessionStorage.getItem("refresh-token");
      const newToken = await AuthService.refreshToken();
      console.log(newToken);
      sessionStorage.setItem("token", newToken.data);
    }
    return Promise.reject(error);
  }
);

export { ProtectedAPI };