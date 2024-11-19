import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../Services/Authentication/AuthService';

export default function AuthProvider({ children, requiredUserType }) {
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    // Use effect to prevent children from rendering until token is re-verified
    useEffect(() => {
        const verifyToken = async () => {
            const storedUserType = sessionStorage.getItem("userType");
            const refreshToken = sessionStorage.getItem('refreshToken');

            // Check if refresh token exists and user type matches
            if (!refreshToken || storedUserType !== requiredUserType) {
                navigate('/unauthorised');
                return;
            }

            try {
                const response = await AuthService.refreshToken(refreshToken);
                sessionStorage.setItem('token', response.data.token);
                sessionStorage.setItem('refreshToken', response.data.refreshToken);
                sessionStorage.setItem('userType', response.data.userType);
            } catch (error) {
                console.error("Token verification failed");
                navigate('/signin');
            } finally {
                setLoading(false);
            }
        };

        verifyToken();
    }, [requiredUserType, navigate]);

    if (loading) {
        return <div>Loading...</div>; // Replace with your loading indicator
    }

    return <>{children}</>;
}