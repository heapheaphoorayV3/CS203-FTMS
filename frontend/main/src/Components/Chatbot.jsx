import { useState, useEffect } from "react";
import sender1 from "../Assets/sender1.svg";
import user1 from "../Assets/user1.svg";
import { motion } from "framer-motion";
import ChatbotService from "../Services/Chatbot/ChatbotService";
import FencerService from "../Services/Fencer/FencerService";
import { Link } from "react-router-dom";
import EventService from "../Services/Event/EventService";

export default function Chatbot() {
  const [error, setError] = useState(null);
  const [userData, setUserData] = useState({});
  const [fencerEvents, setFencerEvents] = useState([]);
  const [fencerUpcomingEvents, setFencerUpcomingEvents] = useState([]);
  const [recommendedTournaments, setRecommendedTournaments] = useState([]);
  const [selectedChoice, setSelectedChoice] = useState(null);
  const [showInput, setShowInput] = useState(false);
  const [eventID, setEventID] = useState("");
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
          setError(
            "Unauthorized: You don't have permission to use the chatbot."
          );
        } else if (error.request) {
          // The request was made but no response was received
          setError("Fencer Data has failed to load, please try again later.");
        } else {
          // Something happened in setting up the request that triggered an Error
          setError("Fencer Data has failed to load, please try again later.");
        }
      }
    };

    const fetchFencerEvents = async () => {
      try {
        const response = await EventService.getAllEventsByGenderAndWeapon();
        setFencerEvents(response.data);
      } catch (error) {
        if (error.response.status === 403) {
          setError(
            "Unauthorized: You don't have permission to use the chatbot."
          );
        } else if (error.request) {
          // The request was made but no response was received
          setError(
            "Fencer Events Data has failed to load, please try again later."
          );
        } else {
          // Something happened in setting up the request that triggered an Error
          setError(
            "Fencer Events Data has failed to load, please try again later."
          );
        }
      }
    };

    const fetchFencerUpcomingEvents = async () => {
      try {
        const response = await FencerService.getFencerUpcomingEvents();
        setFencerUpcomingEvents(response.data);
      } catch (error) {
        if (error.response.status === 403) {
          setError(
            "Unauthorized: You don't have permission to use the chatbot."
          );
        } else if (error.request) {
          // The request was made but no response was received
          setError(
            "Fencer Upcoming Events Data has failed to load, please try again later."
          );
        } else {
          // Something happened in setting up the request that triggered an Error
          setError(
            "Fencer Upcoming Events Data has failed to load, please try again later."
          );
        }
      }
    };

    fetchUserData();
    fetchFencerEvents();
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
    try {
      const response = await ChatbotService.getProjectedPoints(eventID);
      addMessage(`Your projected points: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {
      if (error.response?.status === 400) {
        addMessage("No projected points available for this event.", "botError");
      } else {
        addMessage("Failed to load projected points", "botError");
      }
      setShowInput(false);
    }
  };

  const fetchWinRate = async (eventID) => {
    try {
      const response = await ChatbotService.getWinRate(eventID);

      addMessage(`Your win rate: ${response.data}`, "bot");
      setShowInput(false);
    } catch (error) {

      if (error.response?.status === 400) {
        addMessage("No win rate available for this event.", "botError");
      } else {
        addMessage("Failed to load win rate.", "botError");
      }
      setShowInput(false);
    }
  };

  const fetchRecommendedTournaments = async () => {
    try {
      const fencerDetails = {
        weapon: userData.weapon,
        gender: userData.gender,
        experience: new Date().getFullYear() - userData.debutYear,
      };
      const response = await ChatbotService.recommendTournaments(fencerDetails);

      if (response.data.length === 0) {
        addMessage(
          "There are currently no recommended tournaments available.",
          "bot"
        );
        setRecommendedTournaments([]);
      } else {
        const filteredTournaments = response.data.filter((tournament) => {
          return !fencerUpcomingEvents.some(
            (upcomingEvent) => upcomingEvent.tournamentName === tournament.name
          );
        });
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

      if (error.response?.status === 400) {
        addMessage(
          "No recommended tournaments available for this event.",
          "botError"
        );
      } else {
        addMessage("Failed to load recommended tournaments", "botError");
      }
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
      if (fencerEvents.length === 0) {
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
      const selectedEvent = fencerEvents.find((event) => {
        return event.id === id;
      });


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
          transition={{ duration: 0.6 }}
          whileHover={{
            scale: 1.1,
            backgroundColor: "#4059AD",
            transition: { duration: 0.3 },
          }}
          whileTap={{ scale: 0.9 }}
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
          whileTap={{ scale: 0.9 }}
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
          whileTap={{ scale: 0.9 }}
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
              whileHover={{
                scale: 1.1,
                transition: { duration: 0.3 },
              }}
              whileTap={{ scale: 0.9 }}
              className="bg-gray-200 p-4 text-black rounded-md w-full my-2"
            >
              {tournament.name}
            </motion.button>
          </Link>
        ))}
    </div>
  );

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
              ${
                msg.sender === "bot" || msg.sender === "botError"
                  ? "justify-start"
                  : "justify-end"
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
                  {fencerEvents.map((event) => (
                    <button
                      key={event.id}
                      onClick={() => handleEventSelection(event.id)}
                      className="block w-full text-left p-2 rounded-md hover:text-primary"
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
