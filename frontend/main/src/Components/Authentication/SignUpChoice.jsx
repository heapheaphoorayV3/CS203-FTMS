import React from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import fencer from "../../Assets/fencer.png";
import organiser from "../../Assets/organiser.png";


export default function SignupOptions() {
  return (
    // Primary container for Page
    <div className="flex flex-col items-center min-h-screen gap-10 bg-gray-200">

      <h1 className="mt-10 text-2xl font-semibold px-12 py-5">What do you want to sign up as?</h1>

      {/* Container for the 2 options --> div for each button*/}
      <div className="flex flex-wrap justify-center items-center px-12 py-5 space-x-8 gap-10" >

        <Link to="/signup-fencer" className="flex flex-col items-center p-6 w-[400px] min-w-[300px] bg-white border rounded-lg shadow-lg">
          <button className="flex flex-col items-center p-3 rounded">
            <h1 className="text-2xl font-semibold">Fencer</h1>
            <img src={fencer} alt="Fencer" className="w-24 h-24 m-4" />
            <p className="text-lg m-4 italic">I want to participate in tournaments</p>
          </button>
        </Link>

        <Link to="/signup-organiser" className="flex flex-col items-center p-6 w-[400px] min-w-[300px] bg-white rounded-lg shadow-lg">
          <button className="flex flex-col items-center p-3 rounded">
            <h1 className="text-2xl font-semibold">Organiser</h1>
            <img src={organiser} alt="Organiser" className="w-24 h-24 m-4" />
            <p className="text-lg m-4 italic">I want to organise tournaments</p>
          </button>
        </Link>
        
      </div>
    </div>
    
  );
}