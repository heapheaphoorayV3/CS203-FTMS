import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FencerDashboard from "./Components/FencerDashboard";
import CreateTournament from "./Components/Tournament/CreateTournament";
import OrganiserDashboard from "./Components/OrganiserDashboard";
import CreateEvent from "./Components/Tournament/CreateEvent";
import SignUpEvent from "./Components/Tournament/SignUpEvent";
import SignupOptions from "./Components/Authentication/SignupOptions";
import SignIn from "./Components/Authentication/SignIn";
import SignUpFencer from "./Components/Authentication/SignUpFencer";
import SignUpOrganiser from "./Components/Authentication/SignUpOrganiser";
import DefaultLayout from "./Layouts/DefaultLayout";
import UnauthenticatedLayout from "./Layouts/UnauthenticatedLayout";
import AuthLayout from "./Layouts/AuthLayout";
import AdminLayout from "./Layouts/AdminLayout";
import ViewTournament from "./Components/Tournament/ViewTournament";
import ProtectedRoute from "./Components/Authentication/ProtectedRoute";
import ViewEvent from "./Components/Tournament/ViewEvent";
import Tournaments from "./Components/Tournament/Tournaments";
import VerifyOrganiser from "./Components/Admin/VerifyOrganiser";
import LandingPage from "./Components/Others/LandingPage";
import InternationalRanking from "./Components/InternationalRanking";
import AuthProvider from "./Components/Authentication/AuthProvider";

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          {/* Default Layout */}
          <Route element={<DefaultLayout />}>
            <Route path="/" element={<LandingPage />} />
            <Route path="/fencer-dashboard" element={<FencerDashboard />} />
            <Route
              path="/organiser-dashboard"
              element={<OrganiserDashboard />}
            />
            <Route
              path="/tournaments/:tournamentID"
              element={<ViewTournament />}
            />
            <Route path="/view-event/:eventID" element={<ViewEvent />} />
            <Route path="/tournaments" element={<Tournaments />} />
            <Route path="/signin" element={<SignIn />} />
            <Route path="/signup-options" element={<SignupOptions />} />
            <Route path="/signup-fencer" element={<SignUpFencer />} />
            <Route path="/signup-organiser" element={<SignUpOrganiser />} />
            <Route path="/create-tournament" element={<CreateTournament />} />
            <Route
              path="/tournament/:tournamentID/create-event"
              element={<CreateEvent />}
            />
            <Route path="/signup-event" element={<SignUpEvent />} />
            <Route path="/verify-organiser" element={<VerifyOrganiser />} />
            <Route
              path="/international-ranking"
              element={<InternationalRanking />}
            />
          </Route>

          {/* Unauthenticated Layout */}
          <Route element={<UnauthenticatedLayout />}>
            <Route path="/" element={<LandingPage />} />
          </Route>

          {/* Admin Layout */}
          <Route element={<AdminLayout />}>
            {/* <Route path="/verify-organiser" element={<VerifyOrganiser />} /> */}
          </Route>

          {/* Authenticated Layout */}
          <Route element={<ProtectedRoute />}>
            <Route element={<AuthLayout />}>
              <Route path="/dashboard" element={<FencerDashboard />} />
            </Route>
            <Route
              path="/organiser-dashboard"
              element={<OrganiserDashboard />}
            />
            <Route path="/create-tournament" element={<CreateTournament />} />
            <Route path="/create-event" element={<CreateEvent />} />
            <Route path="/signup-event" element={<SignUpEvent />} />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
