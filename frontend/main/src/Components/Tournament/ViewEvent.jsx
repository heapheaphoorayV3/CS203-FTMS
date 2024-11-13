import { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { Tabs, Tab } from "../Others/Tabs.jsx";
import EventService from "../../Services/Event/EventService.js";
import OrganiserService from "../../Services/Organiser/OrganiserService.js";
import PaginationButton from "../Others/PaginationButton.jsx";
import EventBracket from "./EventBracket.jsx";
import CreatePoules from "./CreatePoules.jsx";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import UpdateBracketMatch from "./UpdateBracketMatch.jsx";
import EndPoules from "./EndPoules.jsx";
import EndEvent from "./EndEvent.jsx";
import { motion } from "framer-motion";
import LoadingPage from "../Others/LoadingPage.jsx";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

export default function ViewEvent() {
  const { tournamentID, eventID } = useParams();

  // Check if organiser is the owner of the event
  const [isOwner, setIsOwner] = useState(false);
  const [userType, setUserType] = useState(sessionStorage.getItem("userType"));
  const [eventData, setEventData] = useState(null);
  const [pouleTableData, setPouleTableData] = useState(null);
  const [cleanedPouleData, setCleanedPouleData] = useState([]);
  const [matches, setMatches] = useState(null);
  const [selectedPoule, setSelectedPoule] = useState(1);
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [isUpdatePopupVisible, setIsUpdatePopupVisible] = useState(false);
  const [isEndPoulesPopupVisible, setIsEndPoulesPopupVisible] = useState(false);
  const [isEndEventPopupVisible, setIsEndEventPopupVisible] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);
  const [eventRanking, setEventRanking] = useState([]);
  const [poulesResults, setPoulesResults] = useState(null);
  const [updatePoulesScores, setUpdatePoulesScores] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isInputValid, setIsInputValid] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;

  const [paginatedEventRankingData, setPaginatedEventRankingData] = useState(
    []
  );

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const fetchEventData = async () => {
    try {
      const response = await EventService.getEvent(eventID);
      setEventData(response.data);
      // console.log("event data =>");
    } catch (error) {
      if (error.response) {
        console.log(
          "Error response data for event data: ",
          error.response.data
        );
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request for event data: ", error.request);
        setError("Event Data has failed to load, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("Event Data has failed to load, please try again later.");
      }
    }
  };

  // Fetch Upcoming Tournament if Organiser to check if organiser is the owner of current event
  const checkIfOwner = async () => {
    try {
      const response = await OrganiserService.getAllHostedTournaments();
      const upcomingTournaments = response.data;
      console.log("upcoming tournaments:", upcomingTournaments);
      let found = false;
      for (let tournament of upcomingTournaments) {
        if (Array.isArray(tournament.events)) {
          for (let event of tournament.events) {
            console.log("tournament.events id=", event.id);
            console.log("current event id=", eventID);
            if (Number(event.id) === Number(eventID)) {
              found = true;
              break;
            }
          }
        }
        if (found) {
          break;
        }
      }
      setIsOwner(found);
    } catch (error) {
      console.error("Failed getting upcoming tournaments:", error);
      setError("Event Data has failed to load, please try again later.");
    }
  };

  const fetchPouleTable = async () => {
    try {
      const response = await EventService.getPouleTable(eventID);
      setPouleTableData(response.data);

      const processedData = response.data.pouleTable.map((poule) =>
        Object.entries(poule).map(([fencer, results]) => {
          const resultArray = results.split(",");
          const cleanedFencerName = fencer.replace(/ -- \d+$/, "");
          return { fencer: cleanedFencerName, results: resultArray };
        })
      );

      setCleanedPouleData(processedData);
    } catch (error) {
      if (error.response) {
        console.log(
          "Error response data for poule table: ",
          error.response.data
        );
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request for poule table: ", error.request);
        setError("Poule Data has failed to load, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("Poule Data has failed to load, please try again later.");
      }
    }
  };

  const fetchMatches = async () => {
    try {
      const response = await EventService.getMatches(eventID);
      setMatches(response.data);
      // console.log("matches:", response.data);
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request for matches: ", error.request);
        setError(
          "Direct Elimination Data has failed to load, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError(
          "Direct Elimination Data has failed to load, please try again later."
        );
      }
    }
  };

  const fetchEventRanking = async () => {
    try {
      const response = await EventService.getEventRanking(eventID);
      const startIndex = Math.max(0, (currentPage - 1) * limit);
      const endIndex = Math.min(response.data.length, startIndex + limit);
      setEventRanking(response.data.slice(startIndex, endIndex));
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log(
          "Error request for fetching event ranking: ",
          error.request
        );
        setError(
          "Event Ranking Data has failed to load, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError(
          "Event Ranking Data has failed to load, please try again later."
        );
      }
    }
  };

  const fetchPoulesResults = async () => {
    try {
      const response = await EventService.getPoulesResult(eventID);
      // console.log(response.data);
      setPoulesResults(response.data);
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError(
          "Poules Results Data has failed to load, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError(
          "Poules Results Data has failed to load, please try again later."
        );
      }
    }
  };

  useEffect(() => {
    setLoading(true);

    if (eventID) {
      Promise.all([
        fetchEventData(),
        fetchPouleTable(),
        fetchMatches(),
        fetchEventRanking(),
        fetchPoulesResults(),
        sessionStorage.getItem("userType") === "O" ? checkIfOwner() : Promise.resolve(),
      ])
        .then(() => {
          setLoading(false);
        });
    }
  }, [eventID, tournamentID, userType]);

  if (loading) {
    return <LoadingPage />; // Show loading state
  }

  if (error) {
    return (
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-2xl font-semibold">{error}</h1>
      </div>
    ); // Show error message if any
  }

  const constructEventName = (gender, weapon) => {
    let eventName = "";
    // Switch statement for gender
    switch (gender) {
      case "M":
        eventName += "Men's ";
        break;
      case "W":
        eventName += "Women's ";
        break;
    }
    // Switch statement for weapon
    switch (weapon) {
      case "F":
        eventName += "Foil";
        break;
      case "E":
        eventName += "Épée";
        break;
      case "S":
        eventName += "Sabre";
        break;
    }

    return eventName;
  };

  let homeLink;
  if (userType === "F") {
    homeLink = "/fencer-dashboard";
  } else if (userType === "O") {
    homeLink = "/organiser-dashboard";
  } else if (userType === "A") {
    homeLink = "/admin-dashboard";
  } else {
    homeLink = "/"; // Default link if userType is not recognized
  }

  const breadcrumbsItems = [
    { name: "Home", link: homeLink },
    { name: "Tournaments", link: "/tournaments" },
    {
      name: loading
        ? "Loading..."
        : eventData
          ? eventData.tournamentName
          : "Not Found",
      link: `/tournaments/${tournamentID}`,
    },
    {
      name: loading
        ? "Loading..."
        : eventData
          ? constructEventName(eventData.gender, eventData.weapon)
          : "Not Found",
    },
  ];

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return `${formattedDate}`;
  };

  const handlePouleChange = (event) => {
    setSelectedPoule(event.target.value);
  };

  const pouleIndex = selectedPoule ? parseInt(selectedPoule) - 1 : 0;

  const createPoules = () => {
    setIsCreatePopupVisible(true);
  };

  const closeCreatePopup = () => {
    setIsCreatePopupVisible(false);
    fetchPouleTable();
  };

  const updatePoules = () => {
    setIsUpdating(true);
    setIsInputValid(true);
  };

  const cancelUpdatePoules = () => {
    setIsUpdating(false);
    setIsInputValid(true);
    setUpdatePoulesScores({});
    fetchPouleTable();
  };

  function handleInputChange(event, index, rowIndex) {
    const inputValue = event.target.value;
    const newScore = Number(inputValue);

    if (!isNaN(newScore) && newScore >= 0 && newScore <= 5) {
      const poules = pouleTableData.pouleTable[pouleIndex];

      const rowData = Object.entries(poules)[rowIndex];
      const fencerName = rowData[0];
      const scoresArray = rowData[1].split(",").map(Number);

      scoresArray[index] = newScore;

      const editedScore = scoresArray.join(",");

      const updatedData = {
        [fencerName]: editedScore,
      };

      const pouleTable = pouleTableData.pouleTable[pouleIndex];

      for (const key in pouleTable) {
        if (key === fencerName) {
          pouleTable[key] = updatedData[fencerName];
          break;
        }
      }
    } else {
      setIsInputValid(false);
    }
  }

  const updateBracketMatch = () => {
    setIsUpdatePopupVisible(true);
  };

  const closeUpdatePopup = () => {
    setIsUpdatePopupVisible(false);
    fetchMatches();
  };

  const submitUpdatePoules = async () => {
    try {
      const updatedPouleData = pouleTableData.pouleTable[selectedPoule - 1];

      const singleTableMap = new Map(Object.entries(updatedPouleData));

      const combinedData = {
        pouleNumber: selectedPoule,
        singleTable: Object.fromEntries(singleTableMap),
      };

      console.log("sending to backend:", combinedData);

      await EventService.updatePouleTable(eventID, combinedData);
      fetchPoulesResults();

      // console.log("Poules updated successfully");
    } catch (error) {
      console.error("Error updating poules:", error);
    } finally {
      setIsUpdating(false);
    }
  };

  const endPoules = async () => {
    setIsEndPoulesPopupVisible(true);
  };

  const closeEndPoulesPopup = () => {
    setIsEndPoulesPopupVisible(false);
    fetchPouleTable();
    fetchMatches();
    fetchEventRanking();
    fetchPoulesResults();
  };

  const endEvent = async () => {
    setIsEndEventPopupVisible(true);
  };

  const closeEndEventPopup = () => {
    setIsEndEventPopupVisible(false);
    fetchEventData();
    fetchEventRanking();
  };
  // console.log("eventdata:", eventData);
  console.log("poules results:", poulesResults);
  console.log("poules results bypass:", poulesResults.bypassFencers);

  const totalPages = Math.ceil(eventRanking.length / limit);

  console.log("isowner:", isOwner);

  return (
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <Breadcrumbs items={breadcrumbsItems} />
      <div className="flex justify-between items-center mr-12 py-4 px-4">
        <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
          {eventData.tournamentName} -{" "}
          {constructEventName(eventData.gender, eventData.weapon)}
        </h1>
        {eventData.isOver ? (
          <motion.div
            className="shadow-lg rounded-lg bg-indigo-600 mr-12"
            whileHover={{
              scale: 0.9,
              transition: { duration: 0.3 },
            }}
          >
            <p className="font-semibold text-lg text-white p-4">
              Event has ended
            </p>
          </motion.div>
        ) : (
          userType === "O" &&
          isOwner && (
            <motion.button
              whileHover={{
                backgroundColor: "#E3170A",
                transition: { duration: 0.3 },
              }}
              className="bg-red-500 p-4 text-white rounded-md h-12 w-sm flex justify-center items-center"
              onClick={endEvent}
            >
              End Event
            </motion.button>
          )
        )}
      </div>

      {isEndEventPopupVisible && (
        <>
          <EndEvent id={eventID} closeEndEventPopup={closeEndEventPopup} />
        </>
      )}

      <div className="ml-12 mr-8 mb-10 grid grid-cols-3 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Date</div>
        <div className="font-semibold text-lg">Start Time</div>
        <div className="font-semibold text-lg">End Time</div>
        <div className="text-lg">{formatDate(eventData.eventDate)}</div>
        <div className="text-lg">{formatTimeTo24Hour(eventData.startTime)}</div>
        <div className="text-lg">{formatTimeTo24Hour(eventData.endTime)}</div>
      </div>

      <div className="ml-12 mr-8 text-lg overflow-x-auto">
        <Tabs>
          <Tab label="Poules">
            <div className="py-4">
              {/* Render only once for both 'O' (Organizers) and 'F' (Fencers) */}
              <div>
                {userType === "O" &&
                  pouleTableData.pouleTable.length === 0 &&
                  isOwner && (
                    <button
                      onClick={createPoules}
                      className="bg-blue-500 text-white px-4 py-2 rounded mt-2 mb-2"
                    >
                      Create Poules
                    </button>
                  )}

                {/* Common Poule Results Dropdown */}
                <div className="mr-12 h-20 max-w-sm">
                  <label className="block font-medium mb-1 ml-1">
                    Poule Results
                  </label>
                  {isUpdating ? (
                    `Poule ${selectedPoule}`
                  ) : (
                    <select
                      value={selectedPoule}
                      onChange={handlePouleChange}
                      className="block w-full py-2 px-3 border border-gray-300 rounded"
                    >
                      {pouleTableData.pouleTable.length === 0 ? (
                        <option disabled>No poules available</option>
                      ) : (
                        pouleTableData.pouleTable.map((poule, index) => (
                          <option key={index} value={index + 1}>
                            {`Poule ${index + 1}`}
                          </option>
                        ))
                      )}
                    </select>
                  )}
                </div>

                <div className="flex gap-2">
                  {/* Conditional buttons for organizers */}
                  {userType === "O" &&
                    isOwner &&
                    pouleTableData.pouleTable.length > 0 &&
                    matches.length === 0 && (
                      <div className="flex mt-4 pb-2 space-x-2">
                        <button
                          onClick={endPoules}
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                        >
                          End Poules
                        </button>

                        <button
                          onClick={updatePoules}
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                        >
                          Update Poules
                        </button>
                      </div>
                    )}

                  {/* Conditional buttons and popup for updating poules */}
                  {userType === "O" && isUpdating && isOwner && (
                    <div className="flex mt-4 pb-2 space-x-2">
                      <button
                        onClick={submitUpdatePoules}
                        className="bg-green-400 text-white px-4 py-2 rounded"
                      >
                        Confirm Changes
                      </button>
                      <button
                        onClick={cancelUpdatePoules}
                        className="bg-red-400 text-white px-4 py-2 rounded"
                      >
                        Cancel Changes
                      </button>
                      {!isInputValid && (
                        <span className="px-4 py-2 text-red-500 italic">
                          Invalid input. Input a number between 0 and 5.
                        </span>
                      )}
                    </div>
                  )}
                </div>

                {/* Show EndPoules Popup if visible */}
                {isEndPoulesPopupVisible && (
                  <EndPoules
                    id={eventID}
                    closeEndPoulesPopup={closeEndPoulesPopup}
                  />
                )}

                {/* Poule Table */}
                <table className="table text-lg">
                  <thead className="text-lg text-neutral">
                    <tr className="border-b border-gray-300 h-[50px]">
                      <th className="w-60 text-primary">Fencer</th>
                      <th className="w-24"></th>
                      {pouleTableData.pouleTable[pouleIndex] &&
                        Object.entries(
                          pouleTableData.pouleTable[pouleIndex]
                        )[0] &&
                        Array.from({
                          length: Object.entries(
                            pouleTableData.pouleTable[pouleIndex]
                          )[0][1].split(",").length,
                        }).map((_, idx) => (
                          <th key={idx} className="text-center w-24">
                            {idx + 1}
                          </th>
                        ))}
                    </tr>
                  </thead>
                  <tbody>
                    {pouleTableData.pouleTable[pouleIndex] ? (
                      Object.entries(pouleTableData.pouleTable[pouleIndex]).map(
                        ([fencer, results], idx) => {
                          const resultArray = results.split(",");
                          const cleanedFencerName = fencer.replace(
                            / -- \d+$/,
                            ""
                          );

                          return (
                            <tr
                              key={idx}
                              className="border-b border-gray-300 h-[68px]"
                            >
                              <td className="w-60">{cleanedFencerName}</td>
                              <td className="font-bold text-center border-r border-gray-300 w-24">
                                {idx + 1}
                              </td>
                              {resultArray.map((result, resultIndex) => (
                                <td
                                  key={resultIndex}
                                  className={`border border-gray-300 hover:bg-gray-100 ${result === "-1"
                                      ? "bg-gray-300 text-gray-300 hover:bg-gray-300"
                                      : ""
                                    }`}
                                >
                                  {result === "-1" ? (
                                    result
                                  ) : isUpdating ? (
                                    <input
                                      type="text"
                                      placeholder={result}
                                      onChange={(event) =>
                                        handleInputChange(
                                          event,
                                          resultIndex,
                                          idx
                                        )
                                      }
                                      className={`w-full text-center ${!isInputValid
                                          ? "border-red-500"
                                          : "border-gray-300"
                                        }`}
                                    />
                                  ) : (
                                    result
                                  )}
                                </td>
                              ))}
                            </tr>
                          );
                        }
                      )
                    ) : (
                      <tr className="text-center border-b border-gray-300">
                        <td colSpan={7}>No poules available yet</td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              {/* Create Poule Popup --> need to pass in submit/close */}
              {isCreatePopupVisible && (
                <CreatePoules onClose={closeCreatePopup} eventID={eventID} />
              )}
            </div>
          </Tab>
          <Tab label="Poules Results">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Ranking</h2> */}
              {poulesResults !== null &&
                // Check if at least one array has data
                (poulesResults.bypassFencers.length > 0 ||
                  poulesResults.fenceOffFencers.length > 0 ||
                  poulesResults.eliminatedFencers.length > 0 ? (
                  <table className="table text-lg border-collapse mb-4">
                    {/* Table Header */}
                    <thead className="text-lg text-primary">
                      <tr className="border-b border-gray-300">
                        <th className="text-center w-20">Rank</th>
                        <th className="w-1/4">Name</th>
                        <th className="text-center">Country</th>
                        <th className="text-center">Result</th>
                        <th className="text-center">Poule Wins</th>
                        <th className="text-center">Poule Points</th>
                      </tr>
                    </thead>
                    {/* Table Body */}
                    <tbody>
                      {poulesResults.bypassFencers
                        .concat(
                          poulesResults.fenceOffFencers,
                          poulesResults.eliminatedFencers
                        )
                        .map((fencer, idx) => (
                          <tr
                            key={idx}
                            className="border-b border-gray-300 hover:bg-gray-100"
                          >
                            <td className="text-center">{idx + 1}</td>
                            <td className="w-1/4">{fencer.fencerName}</td>
                            <td className="text-center">{fencer.country}</td>
                            <td className="text-center">
                              {poulesResults.bypassFencers.includes(fencer)
                                ? "Bypass"
                                : poulesResults.fenceOffFencers.includes(fencer)
                                  ? "Fence Off"
                                  : "Eliminated"}
                            </td>
                            <td className="text-center">{fencer.pouleWins}</td>
                            <td className="text-center">
                              {fencer.poulePoints}
                            </td>
                          </tr>
                        ))}
                    </tbody>
                  </table>
                ) : (
                  // Message when no ranking data is available
                  <div className="flex justify-center items-center h-full">
                    <h2 className="text-lg font-medium">
                      No ranking available yet
                    </h2>
                  </div>
                ))}
              <div className="flex flex-col justify-center items-center">
                <PaginationButton
                  totalPages={totalPages}
                  buttonSize="w-10 h-10"
                  currentPage={currentPage}
                  onPageChange={handlePageChange}
                />
              </div>
            </div>
          </Tab>
          <Tab label="Bracket">
            <div className="py-4 h-full w-full">
              {matches.length === 0 ? (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No matches available yet
                  </h2>
                </div>
              ) : (
                <>
                  {sessionStorage.getItem("userType") === "O" && isOwner && (
                    <div className="flex pb-2 space-x-2">
                      <button
                        onClick={updateBracketMatch}
                        className="bg-blue-500 text-white px-4 py-2 rounded"
                      >
                        Update Matches
                      </button>
                    </div>
                  )}
                  <EventBracket
                    matches={matches}
                    height="999999999"
                    width="999999999"
                  />
                </>
              )}
            </div>
            {isUpdatePopupVisible && (
              <UpdateBracketMatch
                onClose={closeUpdatePopup}
                eventID={eventID}
                matches={matches}
              />
            )}
          </Tab>
          <Tab label="Event Ranking">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Ranking</h2> */}
              {matches.length !== 0 ? (
                <table className="table text-lg border-collapse mb-4">
                  {/* head */}
                  <thead className="text-lg text-primary">
                    <tr className="border-b border-gray-300">
                      <th className="text-center w-20">Rank</th>
                      <th className="text-center">Name</th>
                      <th className="text-center">Country</th>
                    </tr>
                  </thead>
                  <tbody>
                    {eventRanking.map((item, index) => (
                      <tr
                        key={item.fencerId}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td className="text-center">{index + 1}</td>
                        <td className="text-center">{item.fencerName}</td>
                        <td className="text-center">{item.country}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    Event ranking not available yet
                  </h2>
                </div>
              )}
              <div className="flex flex-col justify-center items-center">
                <PaginationButton
                  totalPages={totalPages}
                  buttonSize="w-10 h-10"
                  currentPage={currentPage}
                  onPageChange={handlePageChange}
                />
              </div>
            </div>
          </Tab>
          <Tab label="Participants">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Participants</h2> */}
              {eventData.fencers.length > 0 ? (
                <table className="table text-lg border-collapse mb-4">
                  {/* head */}
                  <thead className="text-lg text-primary text-center">
                    <tr className="border-b border-gray-300">
                      <th className="w-20"></th>
                      <th className="w-60">Name</th>
                      <th className="w-60">Club</th>
                      <th className="w-60">Points</th>
                    </tr>
                  </thead>
                  <tbody className="text-center">
                    {eventData.fencers.map((fencer, index) => (
                      <tr
                        key={fencer.id}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td>{index + 1}</td>
                        <td>{fencer.name}</td>
                        <td>{fencer.club}</td>{" "}
                        {/* Assuming 'club' is a property in fencer */}
                        <td>{fencer.points}</td>{" "}
                        {/* Assuming 'points' is a property in fencer */}
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No participants available yet
                  </h2>
                </div>
              )}
              <div className="flex flex-col justify-center items-center">
                <PaginationButton
                  totalPages={totalPages}
                  buttonSize="w-10 h-10"
                  currentPage={currentPage}
                  onPageChange={handlePageChange}
                />
              </div>
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
}
