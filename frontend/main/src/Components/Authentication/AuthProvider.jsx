import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../Services/Authentication/AuthService';

export default function AuthProvider({ children }) {
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    // Use effect to prevent children from rendering until token is re-verified
    useEffect(() => {
        const verifyToken = async () => {
            // If no refresh token redirect to sign-in
            if (!sessionStorage.getItem('refreshToken')) {
                console.log("No refresh token found, directing to sign-in"); 
                navigate('/signin');
            }

            try {
                console.log("In verify token");
                const response = await AuthService.refreshToken(sessionStorage.getItem('refreshToken'));
                sessionStorage.setItem('token', response.data.token);
                sessionStorage.setItem('refreshToken', response.data.refreshToken);
                sessionStorage.setItem('userType', response.data.userType);
                console.log("Token refreshed successfully");
            } catch (error) {
                // If error redirect to sign-in also
                console.error("Token verification failed in AuthProvider");
                navigate('/signin');
            } finally {
                setLoading(false);
            }
        };

        verifyToken();
    }, []);

    if (loading) {
        return <div>Loading...</div>; // Replace with your loading indicator
    }

    return <>{children}</>;
}