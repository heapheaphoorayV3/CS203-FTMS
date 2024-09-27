import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../Components/Navbar";

const AuthLayout = () => {
  return (
    <div className="grid grid-rows-[50px_1fr]">
      
      <div><Navbar/></div>

      <main>
        <Outlet /> {/* This is where the child routes will be rendered */}
      </main>
    </div>
  );
};

export default AuthLayout;
