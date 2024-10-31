import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../Components/Navbar";
import Sidebar from "../Components/Sidebar";

const AuthLayout = () => {
  return (
    <div className="grid grid-rows-[50px_1fr] grid-cols-[250px_1fr] h-screen w-screen min-h-[700px] min-w-[1200px] overflow-x-hidden">
        <div className="row-span-1 col-span-2">
          <Navbar />
        </div>
      <div className="row-span-2 col-start-1">
        <Sidebar />
      </div>

      <main className="flex flex-col col-start-2 row-start-2">
        <Outlet /> {/* This is where the child routes will be rendered */}
      </main>
    </div>
  );
};

export default AuthLayout;
