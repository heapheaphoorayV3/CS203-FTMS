import { useContext } from "react";
import React, { useState, useEffect } from "react";
import AuthService from "../Services/Authentication/AuthService";

// Create Auth Context so components and nested components can use auth info
const AuthContext = React.createContext();

// Wraps web view components to allow the use of useAuth hooks
export default function AuthProvider({ children }) {

    /* User token will be:
     - undefined if have not checked if user has logged in
     - null if user is not logged in
     - String if user has logged in */
    const [authToken, setAuthToken] = useState();

    // Set current user type
    const [userType, setUserType] = useState();

    // Automatically check if user has valid token when any component mounts
    useEffect(() => {
        async function autoLogin() {
            try {
                const response = await AuthService.verifyToken();

                const { authToken , user } = response[1];

// May be subject to change depending on token format
                setAuthToken(authToken);
                setUserType(user);
            } catch (error) {
                setAuthToken(null);
                setUserType(null);
            }
        }

        // Call autoLogin()
        autoLogin();
    }, []);

    async function handleLogin(formData) {
        try {
            const response = await AuthService.loginUser(formData);

            const { authToken , user } = response[1];

// May be subject to change depending on token format
            setAuthToken(authToken);
            setUserType(user);
        } catch (error) {
            throw error;
        }
    }

    async function handleLogout() {
        // Set token to null
        setAuthToken(null);
        setUserType(null);
    }

    // Returnt the AuthProvider component
    return (
        <AuthContext.Provider value={{ authToken, userType, handleLogin, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
}

// Custom hook to allow components to access auth info
export function useAuth() {
    const context = useContext(AuthContext);

    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }

    return useContext(AuthContext);
}