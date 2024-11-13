import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FencerDashboard from "./Components/FencerDashboard";
import CreateTournament from "./Components/Tournament/CreateTournament";
import OrganiserDashboard from "./Components/OrganiserDashboard";
import AdminDashboard from "./Components/Admin/AdminDashboard";
import CreateEvent from "./Components/Tournament/CreateEvent";
import SignupOptions from "./Components/Authentication/SignupOptions";
import SignIn from "./Components/Authentication/SignIn";
import ForgotPassword from "./Components/Authentication/ForgotPassword";
import ResetPassword from "./Components/Authentication/ResetPassword";
import SignUpFencer from "./Components/Authentication/SignUpFencer";
import SignUpOrganiser from "./Components/Authentication/SignUpOrganiser";
import DefaultLayout from "./Layouts/DefaultLayout";
import ViewTournament from "./Components/Tournament/ViewTournament";
import ViewEvent from "./Components/Tournament/ViewEvent";
import Tournaments from "./Components/Tournament/Tournaments";
import VerifyOrganisers from "./Components/Admin/VerifyOrganisers";
import LandingPage from "./Components/Others/LandingPage";
import InternationalRanking from "./Components/InternationalRanking";
import Chatbot from "./Components/Chatbot";
import UnauthorisedPage from "./Components/Authentication/UnauthorisedPage";
import AuthProviderFencer from "./Components/Authentication/AuthProviderFencer";
import AuthProviderOrganiser from "./Components/Authentication/AuthProviderOrganiser";
import AuthProviderAdmin from "./Components/Authentication/AuthProviderAdmin";


function App() {
  return (
    <Router>

      <Routes>
        {/* Unauthenticated Layout */}
        <Route element={<DefaultLayout />}>
          <Route path="/" element={<LandingPage />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/reset-password/:token" element={<ResetPassword />} />
          <Route path="/signup-options" element={<SignupOptions />} />
          <Route path="/signup-fencer" element={<SignUpFencer />} />
          <Route path="/signup-organiser" element={<SignUpOrganiser />} />
          <Route path="/tournaments" element={<Tournaments />} />
          <Route path="/tournaments/:tournamentID" element={<ViewTournament />} />
          <Route path="/:tournamentID/view-event/:eventID" element={<ViewEvent />} />
          <Route path="/international-ranking" element={<InternationalRanking />} />
          <Route path="/unauthorised" element={<UnauthorisedPage />} />
        </Route>

        {/* Fencer Layout */}
        <Route element={<AuthProviderFencer><DefaultLayout /></AuthProviderFencer>}>
          <Route path="/fencer-dashboard" element={<FencerDashboard />} />
          <Route path="/chatbot" element={<Chatbot />} />
        </Route>

        {/* Organiser Layout */}
        <Route element={<AuthProviderOrganiser><DefaultLayout /></AuthProviderOrganiser>}>
          <Route path="/organiser-dashboard" element={<OrganiserDashboard />} />
          <Route path="/create-tournament" element={<CreateTournament />} />
          <Route path="/tournament/:tournamentID/create-event" element={<CreateEvent />} />
        </Route>

        {/* Admin Layout */}
        <Route element={<AuthProviderAdmin><DefaultLayout /></AuthProviderAdmin>}>
          <Route path="/admin-dashboard" element={<AdminDashboard />} />
          <Route path="/verify-organisers" element={<VerifyOrganisers />} />
        </Route>

      </Routes>

    </Router>
  );
}

export default App;
