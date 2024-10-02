import React from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../Components/Navbar";

const AdminLayout = () => {
    return (
      <>
        <Navbar />
        <main>
          <Outlet /> {/* This is where the child routes will be rendered */}
        </main>
      </>
    );
  };
  
  export default AdminLayout;