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
  const [isUpdating, setIsUpdating] = useState(false);
  const [eventRanking, setEventRanking] = useState(null);
  const [updatePoulesScores, setUpdatePoulesScores] = useState({});
  const [recommendedPoulesData, setRecommendedPoulesData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isInputValid, setIsInputValid] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;

  const [paginatedData, setPaginatedData] = useState([]);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    setLoading(true);

    const fetchEventData = async () => {
      try {
        const response = await EventService.getEvent(eventID);
        setEventData(response.data);
        // console.log("event data =>");
      } catch (error) {
        console.error("Error fetching event data:", error);
        setError("Failed to load event data.");
      }
    };

    // Fetch Upcoming Tournament if Organiser to check if organiser is the owner of current event
    const checkIfOwner = async () => {
      try {
        const response =
          await OrganiserService.getOrganiserUpcomingTournaments();
        const upcomingTournaments = response.data;
        console.log("upcoming tournaments:", upcomingTournaments);
        let found = false;
        for (let tournament of upcomingTournaments) {
          if (Array.isArray(tournament.events)) {
            for (let event of tournament.events) {
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
        console.error("Error fetching event:", error);
        setError("Failed to load event.");
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
        console.error("Error fetching poule table data:", error);
        setError("Failed to load poule table data.");
      }
    };

    const fetchRecommendedPoules = async () => {
      try {
        const response = await EventService.getRecommendedPoules(eventID);
        setRecommendedPoulesData(response.data);
      } catch (error) {
        console.error("Error fetching recommended poules", error);
        setError("Failed to load recommended poules");
      }
    };

    const fetchMatches = async () => {
      try {
        const response = await EventService.getMatches(eventID);
        setMatches(response.data);
        // console.log("matches:", response.data);
      } catch (error) {
        console.error("Error fetching matches:", error);
        setError("Failed to load matches.");
      }
    };

    const fetchEventRanking = async () => {
      try {
        const response = await EventService.getEventRanking(eventID);
        // console.log(response.data);
        setEventRanking(response.data);
      } catch (error) {
        console.error("Error fetching event ranking: ", error);
        setError("Failed to load event ranking");
      }
    };

    if (sessionStorage.getItem("userType") === "O") {
      checkIfOwner();
    }

    if (eventID) {
      Promise.all([
        fetchEventData(),
        fetchPouleTable(),
        userType === "O" && fetchRecommendedPoules(),
        fetchMatches(),
        fetchEventRanking(),
      ])
        .then(() => {
          // Code to run after all functions complete
          // console.log("All functions have completed.");
          setLoading(false);
        })
        .catch((error) => {
          console.error("Error in fetching some data:", error);
          setLoading(false);
        });
    }
  }, [eventID, tournamentID, userType]);

  useEffect(() => {
    if (Array.isArray(eventRanking) && eventRanking.length) {
      // Sort eventRanking based on poulePoints in descending order
      const sortedRanking = [...eventRanking].sort(
        (a, b) => a.tournamentRank - b.tournamentRank
      );

      const startIndex = Math.max(0, (currentPage - 1) * limit);
      const endIndex = Math.min(sortedRanking.length, startIndex + limit);
      setPaginatedData(sortedRanking.slice(startIndex, endIndex));
    } else {
      setPaginatedData([]);
    }
  }, [eventRanking, currentPage, limit]);

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  const constructEventName = (gender, weapon) => {
    let eventName = "";
    // Switch statement for gender
    switch (gender) {
      case "M":
        eventName += "Men's ";
        break;
      case "F":
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
  };

  const submitCreatePopup = async (data) => {
    const payload = {
      eid: String(eventID), // Add the eventID to the payload
      ...data, // Spread the rest of the data
    };

    console.log(payload);
    try {
      await EventService.createPoules(payload.eid, payload);
    } catch (error) {
      console.error("error creating poules", error);
    }
    closeCreatePopup();
  };

  const updatePoules = () => {
    setIsUpdating(true);
    setIsInputValid(true);
  };

  const cancelUpdatePoules = () => {
    setIsUpdating(false);
    setIsInputValid(true);
    setUpdatePoulesScores({});
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

      const pouleTable = pouleTableData.pouleTable[0];

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
  };

  const submitUpdateBracketMatches = async (data) => {
    try {
      const matchId = data.trackSelectedMatch.id;

      // Structure the combined data to match the backend's expected DTO format
      const combinedData = {
        matchId: matchId,
        score1: data.firstScore,
        score2: data.secondScore,
      };

      console.log(combinedData);

      await EventService.updateDEMatch(eventID, combinedData);

      // console.log("Bracket matches updated successfully");

      closeUpdatePopup();

      window.location.reload();
    } catch (error) {
      console.error("Error updating bracket matches:", error);
    }
  };

  const submitUpdatePoules = async () => {
    try {
      const updatedPouleData = pouleTableData.pouleTable[selectedPoule - 1];

      const singleTableMap = new Map(Object.entries(updatedPouleData));

      const combinedData = {
        pouleNumber: selectedPoule,
        singleTable: Object.fromEntries(singleTableMap),
      };

      console.log(combinedData);

      await EventService.updatePouleTable(eventID, combinedData);

      // console.log("Poules updated successfully");
    } catch (error) {
      console.error("Error updating poules:", error);
    } finally {
      setIsUpdating(false);
    }
  };

  const totalPages = Math.ceil(eventRanking.length / limit);

  return (
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <Breadcrumbs items={breadcrumbsItems} />
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        {eventData.tournamentName} -{" "}
        {constructEventName(eventData.gender, eventData.weapon)}
      </h1>

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
              {pouleTableData && pouleTableData.pouleTable[pouleIndex] ? (
                <>
                  {userType === "O" && (
                    <div>
                      {!pouleTableData && (
                        <button
                          onClick={createPoules}
                          className="bg-blue-500 text-white px-4 py-2 rounded mt-2 mb-2"
                        >
                          Create Poules
                        </button>
                      )}
                      <div className="flex items-end w-full">
                        <div className="mr-12 h-20">
                          <label className="block font-medium mb-1 ml-1">
                            Poule Results
                          </label>
                          <select
                            value={selectedPoule}
                            onChange={handlePouleChange}
                            className="block w-full py-2 px-3 border border-gray-300 rounded"
                          >
                            {pouleTableData.pouleTable.map((poule, index) => (
                              <option key={index} value={index}>
                                {`Poule ${index + 1}`}
                              </option>
                            ))}
                          </select>
                        </div>

                        {sessionStorage.getItem("userType") === "O" &&
                          isOwner && (
                            <div className="flex mt-4 pb-2 space-x-2">
                              <button
                                onClick={updatePoules}
                                className="bg-blue-500 text-white px-4 py-2 rounded"
                              >
                                Update Poules
                              </button>

                              {isUpdating && (
                                <>
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
                                      Invalid input. Input a number between 0
                                      and 5.
                                    </span>
                                  )}
                                </>
                              )}
                            </div>
                          )}
                      </div>
                    </div>
                  )}

                  <table className="table text-lg">
                    <thead className="text-lg text-neutral">
                      <tr className="border-b border-gray-300 h-[50px]">
                        <th className="w-60 text-primary">Fencer</th>
                        <th className="w-24"></th>
                        <th className="text-center w-24">1</th>
                        <th className="text-center w-24">2</th>
                        <th className="text-center w-24">3</th>
                        <th className="text-center w-24">4</th>
                        <th className="text-center w-24">5</th>
                      </tr>
                    </thead>
                    <tbody>
                      {Object.entries(
                        pouleTableData.pouleTable[pouleIndex]
                      ).map(([fencer, results], idx) => {
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
                                className={`border border-gray-300 hover:bg-gray-100 ${
                                  result === "-1"
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
                                      handleInputChange(event, resultIndex, idx)
                                    }
                                    className={`w-full text-center ${
                                      !isInputValid
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
                      })}
                    </tbody>
                  </table>
                </>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No poules available yet
                  </h2>
                </div>
              )}
            </div>
            {/* Create Event Popup --> need to pass in submit/close */}
            {isCreatePopupVisible && (
              <CreatePoules
                onClose={closeCreatePopup}
                onSubmit={submitCreatePopup}
                recommendedPoulesData={recommendedPoulesData}
              />
            )}
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
                onSubmit={submitUpdateBracketMatches}
                matches={matches}
              />
            )}
          </Tab>
          <Tab label="Ranking">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Ranking</h2> */}
              {paginatedData.length > 0 ? (
                <table className="table text-lg border-collapse mb-4">
                  {/* head */}
                  <thead className="text-lg text-primary">
                    <tr className="border-b border-gray-300">
                      <th className="text-center w-20">Rank</th>
                      <th className="w-1/2">Name</th>
                      <th className="text-center">Country</th>
                      <th className="text-center">Points</th>
                    </tr>
                  </thead>
                  <tbody>
                    {paginatedData.map((item, index) => (
                      <tr
                        key={item.fencerId}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td className="text-center">{index + 1}</td>
                        <td>{item.fencerName}</td>
                        <td className="text-center">{item.country}</td>
                        <td className="text-center">{item.poulePoints}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No ranking available yet
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
