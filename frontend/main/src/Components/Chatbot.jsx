import { useState, useEffect } from "react";
import sender1 from "../Assets/sender1.svg";
import user1 from "../Assets/user1.svg";
import { motion } from "framer-motion";
import ChatbotService from "../Services/Chatbot/ChatbotService";
import FencerService from "../Services/Fencer/FencerService";
import SubmitButton from "./Others/SubmitButton";
import { Link } from "react-router-dom";

export default function Chatbot() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userData, setUserData] = useState({});
  const [fencerUpcomingEvents, setFencerUpcomingEvents] = useState([]);
  const [recommendedTournaments, setRecommendedTournaments] = useState([]);
  const [selectedChoice, setSelectedChoice] = useState(null);
  const [showInput, setShowInput] = useState(false);
  const [eventID, setEventID] = useState("");
  const [accessDenied, setAccessDenied] = useState(false);
  const [messages, setMessages] = useState([
    { text: "Hello! What would you like to know?", sender: "bot" },
  ]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await FencerService.getProfile();
        setUserData(response.data);
      } catch (error) {
        if (error.response.status === 403) {
          console.log("Unauthorized access to upcoming events.");
          setError(
            "Unauthorized: You don't have permission to use the chatbot."
          );
        } else if (error.request) {
          // The request was made but no response was received
          console.log("Error request: ", error.request);
          setError("Fencer Data has failed to load, please try again later.");
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log("Unknown Error: " + error);
          setError("Fencer Data has failed to load, please try again later.");
        }
      } finally {
        setLoading(false);
      }
    };

    const fetchFencerUpcomingEvents = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getFencerUpcomingEvents();
        setFencerUpcomingEvents(response.data);
      } catch (error) {
        if (error.response.status === 403) {
          console.log("Unauthorized access to upcoming events.");
          setError(
            "Unauthorized: You don't have permission to use the chatbot."
          );
        } else if (error.request) {
          // The request was made but no response was received
          console.log("Error request: ", error.request);
          setError(
            "Fencer Upcoming Events Data has failed to load, please try again later."
          );
        } else {
          // Something happened in setting up the request that triggered an Error
          console.log("Unknown Error: " + error);
          setError(
            "Fencer Upcoming Events Data has failed to load, please try again later."
          );
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
    fetchFencerUpcomingEvents();
  }, []);

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return formattedDate;
  };

  const addMessage = (text, sender) => {
    setMessages((prev) => [...prev, { text, sender }]);
  };

  const fetchProjectedPoints = async (eventID) => {
    setLoading(true);
    try {
      console.log("event ID:", eventID);
      const response = await ChatbotService.getProjectedPoints(eventID);
      addMessage(`Your projected points: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {
      console.log("Error status:", error.response?.status);
      console.error("Error fetching projected points: ", error);
      setError("Failed to load projected points");
      if (error.response?.status === 400) {
        addMessage("No projected points available for this event.", "botError");
      }
      setShowInput(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchWinRate = async (eventID) => {
    setLoading(true);
    try {
      const response = await ChatbotService.getWinRate(eventID);

      addMessage(`Your win rate: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {
      console.error("Error fetching win rate: ", error);
      setError("Failed to load win rate");
      if (error.response?.status === 400) {
        addMessage("No win rate available for this event.", "botError");
      }
      setShowInput(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchRecommendedTournaments = async () => {
    setLoading(true);
    try {
      const fencerDetails = {
        weapon: userData.weapon,
        gender: userData.gender,
        experience: new Date().getFullYear() - userData.debutYear,
      };
      const response = await ChatbotService.recommendTournaments(fencerDetails);
      console.log("recc tourns: ", response.data);
      if (response.data.length === 0) {
        addMessage(
          "There are currently no recommended tournaments available.",
          "bot"
        );
        setRecommendedTournaments([]);
      } else {
        console.log("upcoming:", fencerUpcomingEvents);
        const filteredTournaments = response.data.filter((tournament) => {
          return !fencerUpcomingEvents.some(
            (upcomingEvent) => upcomingEvent.tournamentName === tournament.name
          );
        });
        console.log("filtered:", filteredTournaments);
        if (filteredTournaments.length === 0) {
          addMessage(
            "There are no recommended tournaments available that you haven't already registered for.",
            "bot"
          );
        } else {
          addMessage(`Recommended tournaments: `, "bot");
          setRecommendedTournaments(filteredTournaments);
        }
      }
    } catch (error) {
      console.error("Error fetching recommended tournaments: ", error);
      setError("Failed to load recommended tournaments");
      if (error.response?.status === 400) {
        addMessage(
          "No recommended tournaments available for this event.",
          "botError"
        );
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (eventID) {
      if (selectedChoice === "projected points") {
        fetchProjectedPoints();
      } else if (selectedChoice === "win rate") {
        fetchWinRate();
      }
    }
  }, [eventID, selectedChoice]);

  const handleOptionClick = (choice) => {
    setSelectedChoice(choice);
    addMessage(`I want to get my ${choice}!`, "user");

    if (choice === "projected points" || choice === "win rate") {
      if (fencerUpcomingEvents.length === 0) {
        addMessage(
          "No upcoming tournaments. Please register for a tournament first.",
          "bot"
        );
        setShowInput(false); // Prevent input from showing
      } else {
        setShowInput(true); // Show input if there are upcoming events
      }
    } else if (choice === "recommended tournaments") {
      fetchRecommendedTournaments();
    } else {
      setShowInput(false);
    }

    setEventID("");
  };

  const handleEventSelection = (id) => {
    setEventID(id);
    handleSubmitEventID(id);
  };

  const handleSubmitEventID = (id) => {
    if (id) {
      const selectedEvent = fencerUpcomingEvents.find((event) => {
        return event.id === id;
      });

      console.log("selected event:", selectedEvent);

      const tournamentName = selectedEvent
        ? selectedEvent.tournamentName
        : "Unknown Event";

      addMessage(`Tournament chosen: ${tournamentName}`, "user");
      setEventID(id);

      if (selectedChoice === "projected points") fetchProjectedPoints(id);
      if (selectedChoice === "win rate") fetchWinRate(id);
    }

    setShowInput(false);
    setEventID("");
  };

  const clearChat = () => {
    setMessages([
      { text: "Hello! What would you like to know?", sender: "bot" },
    ]);
    setShowInput(false);
  };

  const OptionButtons = () => (
    <div className="flex flex-col gap-4 w-[70%] ml-[200px] mb-4">
      <div className="flex gap-4 text-lg">
        <motion.button
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          whileHover={{
            scale: 1.1,
            backgroundColor: "#4059AD",
            transition: { duration: 0.3 },
          }}
          className="bg-blue-500 p-4 text-white rounded-md w-full"
          onClick={() => handleOptionClick("projected points")}
        >
          Get my projected points
        </motion.button>
        <motion.button
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          whileHover={{
            scale: 1.1,
            backgroundColor: "#4059AD",
            transition: { duration: 0.3 },
          }}
          className="bg-blue-500 p-4 text-white rounded-md w-full"
          onClick={() => handleOptionClick("win rate")}
        >
          Get my win rate
        </motion.button>
        <motion.button
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          whileHover={{
            scale: 1.1,
            backgroundColor: "#4059AD",
            transition: { duration: 0.3 },
          }}
          className="bg-blue-500 p-4 text-white rounded-md w-full"
          onClick={() => handleOptionClick("recommended tournaments")}
        >
          Recommend me tournaments
        </motion.button>
      </div>
    </div>
  );

  const TournamentOptions = () => (
    <div className="flex flex-col justify-center gap-4 max-w-sm ml-[200px] mb-4">
      {recommendedTournaments.length > 0 &&
        recommendedTournaments.map((tournament, index) => (
          <Link
            to={`/tournaments/${tournament.id}`}
            className="text-white text-lg"
            key={index}
          >
            <motion.button
              initial={{ opacity: 0, scale: 0.5 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.5 }}
              whileHover={{ scale: 1.1 }}
              className="bg-gray-200 p-4 text-black rounded-md w-full my-2"
            >
              {tournament.name}
            </motion.button>
          </Link>
        ))}
    </div>
  );

  // Loading / Error states
  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }
  if (error) {
    return (
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-2xl font-semibold">{error}</h1>
      </div>
    ); // Show error message if any
  }

  return (
    <div className="bg-white h-full overflow-y-auto">
      <div className="flex justify-between items-center mr-12 py-4 px-4">
        <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
          Chatbot
        </h1>
        <motion.button
          initial={{ opacity: 0, scale: 0.5 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.5 }}
          whileHover={{
            scale: 1.1,
            backgroundColor: "#E3170A",
            transition: { duration: 0.3 },
          }}
          className="bg-red-500 p-4 text-white rounded-md h-12 w-sm flex justify-center items-center"
          onClick={clearChat}
        >
          Clear Chat
        </motion.button>
      </div>
      <div className="flex flex-col w-[90%] ml-12 mb-12 text-lg">
        {messages.map((msg, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
            className={`flex 
              ${msg.sender === "bot" || msg.sender === "botError" ? "justify-start" : "justify-end"
            } mb-4`}
          >
            {msg.sender === "bot" ? (
              <div className="flex items-center">
                <img
                  src={sender1}
                  alt="Sender image"
                  className="w-[200px] h-auto"
                />
                <div className="bg-blue-500 rounded-md p-4 text-white max-w-lg">
                  <p>{msg.text}</p>
                </div>
              </div>
            ) : msg.sender === "botError" ? (
              <div className="flex items-center">
                <img
                  src={sender1}
                  alt="Sender image"
                  className="w-[200px] h-auto"
                />
                <div className="flex items-center">
                  <div className="bg-red-500 rounded-md p-4 text-white max-w-lg">
                    <p>{msg.text}</p>
                  </div>
                </div>
              </div>
            ) : (
              <div className="flex items-center">
                <div className="bg-blue-800 rounded-md p-4 text-white max-w-lg">
                  <p>{msg.text}</p>
                </div>
                <img
                  src={user1}
                  alt="Sender image"
                  className="w-[200px] h-auto"
                />
              </div>
            )}
          </motion.div>
        ))}
        {/* Show input field and submit button only if required */}
        {showInput && selectedChoice && (
          <div className="flex justify-center w-full mb-12">
            <div className="flex flex-col h-auto w-[70%] max-w-xl">
              <div className="rounded-lg bg-gray-100 p-6 text-gray-800 shadow-md text-lg">
                <h2 className="text-2xl font-bold">Select a tournament</h2>
                <div className="mt-2 space-y-2">
                  {fencerUpcomingEvents.map((event) => (
                    <button
                      key={event.id}
                      onClick={() => handleEventSelection(event.id)}
                      className="block w-full text-left p-2 rounded-md bg-indigo-100 hover:text-primary"
                    >
                      {event.tournamentName} - {formatDate(event.eventDate)}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          </div>
        )}
        {selectedChoice === "recommended tournaments" && <TournamentOptions />}
        {/* Show option buttons after each bot response */}
        <OptionButtons />
      </div>
    </div>
  );
}

//   return (
//     <div className="bg-white h-full overflow-y-auto">
//       <h1 className="my-10 ml-12 text-left text-4xl font-semibold">Chatbot</h1>
//       <div className="flex w-[80%] ml-12 mb-12">
//         <motion.div
//           initial={{ opacity: 0, scale: 0.5 }}
//           animate={{ opacity: 1, scale: 1 }}
//           transition={{ duration: 0.8 }}
//         >
//           <img src={sender1} alt="Sender image" className="w-[200px] h-auto" />
//         </motion.div>
//         <div className="flex flex-col h-auto w-full" id="messages">
//           <motion.div
//             initial={{ opacity: 0, scale: 0.5 }}
//             animate={{ opacity: 1, scale: 1 }}
//             transition={{ duration: 0.8 }}
//           >
//             <div className="rounded-md bg-blue-500 p-4 text-white mb-4 mr-12">
//               <p>Hello! What would you like to know?</p>
//             </div>
//           </motion.div>
//           <div className="flex justify-between gap-4 mr-12">
//             <motion.div
//               initial={{ opacity: 0, scale: 0.5 }}
//               animate={{ opacity: 1, scale: 1 }}
//               transition={{ duration: 1 }}
//               whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
//               className="rounded-md bg-blue-500 p-4 text-white w-full"
//               onClick={() => handleOptionClick("points")}
//             >
//               <p>Get my projected points</p>
//             </motion.div>
//             <motion.div
//               initial={{ opacity: 0, scale: 0.5 }}
//               animate={{ opacity: 1, scale: 1 }}
//               transition={{ duration: 1.4 }}
//               whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
//               className="rounded-md bg-blue-500 p-4 text-white w-full"
//               onClick={() => handleOptionClick("win rate")}
//             >
//               <p>Get my win rate</p>
//             </motion.div>
//             <motion.div
//               initial={{ opacity: 0, scale: 0.5 }}
//               animate={{ opacity: 1, scale: 1 }}
//               transition={{ duration: 1.8 }}
//               whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
//               className="rounded-md bg-blue-500 p-4 text-white w-full"
//               onClick={() => setSelectedChoice("tournaments")}
//             >
//               <p>Recommend me tournaments</p>
//             </motion.div>

//             {selectedChoice && selectedChoice !== "tournaments" && (
//               <div className="flex justify-end w-[80%] ml-[200px] mb-4">
//                 <div className="flex flex-col h-auto w-full">
//                   <div className="rounded-md bg-blue-800 p-4 text-white mb-4 ml-12">
//                     <p>
//                       {selectedChoice === "points" &&
//                         "Enter event ID for projected points:"}
//                       {selectedChoice === "win rate" &&
//                         "Enter event ID for win rate:"}
//                     </p>
//                     <input
//                       type="text"
//                       value={eventID}
//                       onChange={(e) => setEventID(e.target.value)}
//                       placeholder="Enter event ID"
//                       className="p-2 mt-2 rounded-md text-black"
//                     />
//                     <button
//                       onClick={handleSubmitEventID}
//                       className="mt-2 bg-green-500 p-2 rounded-md text-white"
//                     >
//                       Submit
//                     </button>
//                   </div>
//                 </div>
//               </div>
//             )}
//             {/* <div className="rounded-md bg-blue-500 p-4 text-white w-full">
//               <p>Get my win rate</p>
//             </div>
//             <div className="rounded-md bg-blue-500 p-4 text-white w-full">
//               <p>Recommend me tournaments</p>
//             </div> */}
//           </div>
//         </div>
//       </div>
//       {/* <div className="flex justify-end w-[80%] ml-[200px]">
//         <div className="flex flex-col h-auto w-full" id="messages">
//           <div className="rounded-md bg-blue-800 p-4 text-white mb-4 ml-12"> */}
//       {selectedChoice && (
//         <div className="flex justify-end w-[80%] ml-[200px]">
//           <div className="flex flex-col h-auto w-full" id="messages">
//             <div className="rounded-md bg-blue-800 p-4 text-white mb-4 ml-12">
//               {selectedChoice === "points" && projectedPoints && (
//                 <p>Your projected points: {projectedPoints}</p>
//               )}
//               {selectedChoice === "win rate" && winRate && (
//                 <p>Your win rate: {winRate}</p>
//               )}
//               {selectedChoice === "tournaments" && recommendedTournaments && (
//                 <p>Recommended tournaments: {recommendedTournaments}</p>
//               )}
//             </div>
//           </div>
//           <img src={user1} alt="Sender image" className="w-[200px] h-auto" />
//         </div>
//       )}
//     </div>
//     //     </div>
//     //     {/* <img src={user1} alt="Sender image" className="w-[200px] h-auto" /> */}
//     //   </div>
//     // </div>
//   );
// }
