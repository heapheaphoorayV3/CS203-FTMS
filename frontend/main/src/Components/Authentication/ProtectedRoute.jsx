import React from 'react';
import {Outlet, Navigate} from 'react-router-dom';

const ProtectedRoute = ({ requiredRole }) => {

  const token = sessionStorage.getItem('token');
  const userRole = sessionStorage.getItem("user_type");

  if (!token) {
    return <Navigate to="/signin" replace />;
  }

  if (requiredRole && userRole !== requiredRole) {
    return <Navigate to="/signin" replace />; // Redirect to home or a "not authorized" page
  }

  return <Outlet/>;
};

export default ProtectedRoute;