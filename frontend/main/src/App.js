import "flowbite";
import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import "./App.css";
import FencerDashboard from "./Components/FencerDashboard";
import DefaultLayout from "./Layouts/DefaultLayout";
import SignupOptions from "./Components/Authentication/SignUpChoice";
import SignIn from "./Components/Authentication/SignIn";
import SignUpFencer from "./Components/Authentication/SignUpFencer";
import SignUpOrganiser from "./Components/Authentication/SignUpOrganiser";


function App() {
  return (
    <Router>
      <Routes>
        {/* Default Layout */}
        <Route element={<DefaultLayout />}>
          <Route path="/" element={<FencerDashboard />} />
          <Route path="/fencer-dashboard" element={<FencerDashboard />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup-options" element={<SignupOptions />} />
          <Route path="/signup-fencer" element={<SignUpFencer />} />
          <Route path="/signup-organiser" element={<SignUpOrganiser />} />
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