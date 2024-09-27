import React from 'react';
import {Outlet, Navigate} from 'react-router-dom';

const ProtectedRoute = () => {

  const token = sessionStorage.getItem('token');

  if (!token) {
    return <Navigate to="/signin" replace />;
  }

  return <Outlet/>;
};

export default ProtectedRoute;