import { createContext, useState, useEffect, useContext } from "react";
import AuthService from "../Services/Authentication/AuthService";

// Create the AuthContext
const AuthContext = createContext();

// AuthProvider component that wraps around all components needing access to auth info
export default function AuthProvider({ children }) {
    const [authToken, setAuthToken] = useState();
    const [userType, setUserType] = useState();

    // Automatically check if user is logged in when the component mounts
    // useEffect(() => {
    //     async function autoLogin() {
    //         try {
    //             const response = await AuthService.verifyToken();
    //             const { token, userType } = response.data;

    //             setAuthToken(token);
    //             setUserType(userType);
    //         } catch (error) {
    //             setAuthToken(null);
    //             setUserType(null);
    //         }
    //     }

    //     autoLogin();
    // }, []);

    // Handle login
    async function handleLogin(formData) {
        try {
            const response = await AuthService.loginUser(formData);
            const { token, userType } = response.data;

            setAuthToken(token);
            sessionStorage.setItem("token", token);
            setUserType(userType);
        } catch (error) {
            throw error;
        }
    }

    // Handle logout
    async function handleLogout() {
        setAuthToken(null);
        setUserType(null);
    }

    // Provide auth values and functions to all children components
    return (
        <AuthContext.Provider value={{ authToken, userType, handleLogin, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
}

// Custom hook to use the AuthContext in any child component
export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}
