import React from "react";
import landingpic from "../../Assets/landingpic.jpg";
import NavbarButton from "./NavbarButton";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

const LandingPage = () => {
  const navigate = useNavigate();

  const handleSubmit = (e, path) => {
    e.preventDefault();
    navigate(path);
  };

  return (
    <div
      className="h-full flex flex-col items-center justify-center bg-gradient-to-b text-sky-50 bg-cover bg-center overflow-auto"
      style={{ backgroundImage: `url(${landingpic})` }}
    >
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        <div className="bg-black bg-opacity-30 rounded-xl text-center max-w-xl mx-auto p-4">
          <h1 className="text-4xl font-bold my-4">
            View, Manage, or Participate in Fencing Tournaments.
          </h1>
          <p className="text-lg mb-8">
            FTMS is the all-new one-stop platform for all your fencing
            tournament needs. Sign up now to get started.
          </p>
          <motion.button
            whileHover={{
              scale: 1.1,
              backgroundColor: "#4059AD",
            }}
            transition={{ duration: 0.5, yoyo: Infinity }}
            onClick={(e) => handleSubmit(e, "/signup-options")}
            className="bg-indigo-600 hover:bg-indigo-500 text-white text-xl font-bold py-2 px-6 rounded-md mb-8"
          >
            Sign up now
          </motion.button>
        </div>
      </motion.div>
      {/* Features Section */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        <div className="bg-black bg-opacity-30 rounded-xl grid grid-cols-1 md:grid-cols-3 gap-8 max-w-4xl mx-auto mt-12 p-4">
          <div className="text-center">
            <h2 className="text-xl font-semibold mb-2">
              View all Fencing Tournaments
            </h2>
            <p>
              View all the fencing tournaments happening around you. Get all the
              details you need to participate in them.
            </p>
          </div>
          <div className="text-center">
            <h2 className="text-xl font-semibold mb-2">
              Manage Fencing Tournaments
            </h2>
            <p>
              Register as an organiser and organise your own fencing
              tournaments. Manage all the events, participants, and results with
              ease.
            </p>
          </div>
          <div className="text-center">
            <h2 className="text-xl font-semibold mb-2">
              Participate in Fencing Tournaments
            </h2>
            <p>
              Register as a fencer and sign up for fencing tournaments and
              events. Get all the details you need to participate in them.
            </p>
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default LandingPage;
