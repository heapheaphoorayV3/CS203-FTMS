import React from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import fencer from "../../Assets/fencer.png";
import organiser from "../../Assets/organiser.png";
import { motion } from "framer-motion";

export default function SignupOptions() {
  return (
    // Primary container for Page
    <div className="h-full flex flex-col items-center justify-center gap-10 bg-white">
      <h1 className="text-2xl font-semibold">
        What do you want to sign up as?
      </h1>

      {/* Container for the 2 options --> div for each button*/}
      <div className="flex flex-wrap justify-center items-center px-12 py-5 space-x-8 gap-10">
        <motion.button
          whileHover={{
            scale: 1.1,
            transition: { duration: 0.6 },
          }}
          whileTap={{ scale: 0.9 }}
          className="flex flex-col items-center p-3 rounded"
        >
          <Link
            to="/signup-fencer"
            className="flex flex-col items-center p-6 w-[400px] min-w-[300px] bg-white border rounded-lg shadow-lg"
          >
            <h1 className="text-2xl font-semibold">Fencer</h1>
            <img src={fencer} alt="Fencer" className="w-24 h-24 m-4" />
            <p className="text-lg m-4 italic">
              I want to participate in tournaments
            </p>{" "}
          </Link>
        </motion.button>

        <motion.button
          whileHover={{
            scale: 1.1,
            transition: { duration: 0.6 },
          }}
          whileTap={{ scale: 0.9 }}
          className="flex flex-col items-center p-3 rounded"
        >
          <Link
            to="/signup-organiser"
            className="flex flex-col items-center p-6 w-[400px] min-w-[300px] bg-white border rounded-lg shadow-lg"
          >
            <h1 className="text-2xl font-semibold">Organiser</h1>
            <img src={organiser} alt="Organiser" className="w-24 h-24 m-4" />
            <p className="text-lg m-4 italic">I want to organise tournaments</p>
          </Link>
        </motion.button>
      </div>
    </div>
  );
}
