import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../Components/Navbar";
import Sidebar from "../Components/Sidebar";
// import { AuthProvider } from "../Components/Authentication/AuthenticationContext";

const DefaultLayout = () => {
  // const { isLoggedIn } = AuthProvider();
  return (
    <div className="grid grid-rows-[50px_1fr] grid-cols-[250px_1fr] h-screen w-screen min-h-[700px] min-w-[1200px] overflow-x-hidden">
      <div className="row-span-1 col-span-2 fixed top-0 left-0 right-0 z-50">
        <Navbar />
      </div>
      <div className="row-span-2 col-start-1 mt-[50px]">
        <Sidebar />
      </div>

      <main className="flex flex-col col-start-2 row-start-2" >
        <Outlet />
      </main>
    </div>
  );
};

export default DefaultLayout;
