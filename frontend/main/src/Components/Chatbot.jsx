import { useState, useEffect } from "react";
import sender1 from "../Assets/sender1.svg";
import user1 from "../Assets/user1.svg";
import { motion } from "framer-motion";
import ChatbotService from "../Services/Chatbot/ChatbotService";
import SubmitButton from "./Others/SubmitButton";

export default function Chatbot() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedChoice, setSelectedChoice] = useState(null);
  const [showInput, setShowInput] = useState(false);
  const [eventID, setEventID] = useState("");
  const [messages, setMessages] = useState([
    { text: "Hello! What would you like to know?", sender: "bot" },
  ]);

  const addMessage = (text, sender) => {
    setMessages((prev) => [...prev, { text, sender }]);
  };

  const fetchProjectedPoints = async () => {
    setLoading(true);
    try {
      const response = await ChatbotService.getProjectedPoints(eventID);
      addMessage(`Your projected points: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {
      console.error("Error fetching projected points: ", error);
      setError("Failed to load projected points");
      addMessage(
        "I'm sorry, but I was unable to fetch your projected points. Please try again later.",
        "bot"
      );
      setShowInput(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchWinRate = async () => {
    setLoading(true);
    try {
      const response = await ChatbotService.getWinRate(eventID);
      addMessage(`Your win rate: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {
      console.error("Error fetching win rate: ", error);
      setError("Failed to load win rate");
      addMessage(
        "I'm sorry, but I was unable to fetch your win rate. Please try again later.",
        "bot"
      );
      setShowInput(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchRecommendedTournaments = async () => {
    setLoading(true);
    try {
      const response = await ChatbotService.recommendTournaments();
      if (response.data.length === 0) {
        addMessage(
          "There are currently no recommended tournaments available.",
          "bot"
        ); // Message for empty list
      } else {
        addMessage(`Recommended tournaments: ${response.data}`, "bot"); // Normal message for non-empty list
      }
    } catch (error) {
      console.error("Error fetching recommended tournaments: ", error);
      setError("Failed to load recommended tournaments");
      addMessage(
        "I'm sorry, but I was unable to fetch recommended tournaments. Please try again later.",
        "bot"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (selectedChoice === "tournaments") {
      fetchRecommendedTournaments();
    } else {
      setShowInput(true);
    }
  }, [selectedChoice]);

  const handleOptionClick = (choice) => {
    setSelectedChoice(choice);
    addMessage(`User selected: ${choice}`, "user");
    setEventID("");
  };

  const handleSubmitEventID = () => {
    if (eventID) {
      addMessage(`Event ID entered: ${eventID}`, "user");
      if (selectedChoice === "points") fetchProjectedPoints();
      if (selectedChoice === "win rate") fetchWinRate();
    }
  };

  const OptionButtons = () => (
    <div className="flex justify-between gap-4 w-[70%] ml-[200px] mb-4">
      <motion.button
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
        className="bg-blue-500 p-4 text-white rounded-md w-full"
        onClick={() => handleOptionClick("points")}
      >
        Get my projected points
      </motion.button>
      <motion.button
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
        className="bg-blue-500 p-4 text-white rounded-md w-full"
        onClick={() => handleOptionClick("win rate")}
      >
        Get my win rate
      </motion.button>
      <motion.button
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        whileHover={{ scale: 1.1, transition: { duration: 0.3 } }}
        className="bg-blue-500 p-4 text-white rounded-md w-full"
        onClick={() => handleOptionClick("tournaments")}
      >
        Recommend me tournaments
      </motion.button>
    </div>
  );

  return (
    <div className="bg-white h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">Chatbot</h1>
      <div className="flex flex-col w-[90%] ml-12 mb-12">
        {messages.map((msg, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
            className={`flex ${
              msg.sender === "bot" ? "justify-start" : "justify-end"
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
            ) : (
              <div className="flex items-center">
                <div className="bg-blue-800 rounded-md p-4 text-white max-w-xs">
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
        {selectedChoice && selectedChoice !== "tournaments" && showInput && (
          <div className="flex ml-[200px] w-[70%] mb-4">
            <div className="flex flex-col h-auto w-full">
              <div className="rounded-md bg-gray-200 pt-4 pl-4 text-black mb-4">
                <p>
                  {selectedChoice === "points"
                    ? "Enter event ID for projected points:"
                    : "Enter event ID for win rate:"}
                </p>
                <input
                  type="text"
                  value={eventID}
                  onChange={(e) => setEventID(e.target.value)}
                  placeholder="Enter event ID"
                  className="p-2 mt-2 rounded-md mr-4"
                />
                <button
                  onClick={handleSubmitEventID}
                  className="h-8 w-24 justify-center rounded-md bg-indigo-600 my-5 text-lg font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                  Submit
                </button>
              </div>
            </div>
          </div>
        )}
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
