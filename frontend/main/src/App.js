import React from "react";
import "./App.css";
import "flowbite";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FencerDashboard from "./Components/FencerDashboard";
import DefaultLayout from "./Layouts/DefaultLayout";
import ProtectedRoute from "./Components/Authentication/ProtectedRoute";


function App() {
  return (
    <Router>
      <Routes>
        {/* Default Layout */}
        <Route element={<DefaultLayout />}>
          <Route path="/" element={<FencerDashboard />} />
          <Route path="/fencer-dashboard" element={<FencerDashboard />} />
        </Route>

        {/* Admin Layout */}
        {/* <Route element={<AdminLayout />}></Route> */}

        {/* Authenticated Layout */}
        {/* <Route element={<ProtectedRoute />}>
          <Route element={<AuthLayout />}>
            <Route path="/fencer-dashboard" element={<FencerDashboard />} />
          </Route>
        </Route> */}
      </Routes>
    </Router>
  );
}

export default App;