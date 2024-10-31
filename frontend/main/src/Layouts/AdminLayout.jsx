import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../Components/Navbar";

const AdminLayout = () => {
    return (
      <>
        <Navbar />
        <main className="flex flex-col col-start-2 row-start-2">
          <Outlet /> {/* This is where the child routes will be rendered */}
        </main>
      </>
    );
  };
  
  export default AdminLayout;