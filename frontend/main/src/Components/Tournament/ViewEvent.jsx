import { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import EventService from "../../Services/Event/EventService.js";
import PaginationButton from "../Others/Pagination.jsx";
import EventBracket from "./EventBracket.jsx";
import CreatePoules from "./CreatePoules.jsx";
import { set } from "react-hook-form";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

export default function ViewEvent() {
  const { eventID } = useParams();

  const [eventData, setEventData] = useState(null);
  const [pouleTableData, setPouleTableData] = useState(null);
  const [matches, setMatches] = useState(null);
  const [selectedPoule, setSelectedPoule] = useState("");
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);
  const [recommendedPoulesData, setRecommendedPoulesData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;


  // Set up ref and initial parent size
  const parentRef = useRef(null);
  const [parentSize, setParentSize] = useState({ width: 0, height: 0 });

  useEffect(() => {
    const updateSize = () => {
      if (parentRef.current) {
        const { width, height } = parentRef.current.getBoundingClientRect();
        setParentSize({ width, height });
      }
    };

    // Trigger initial measurement with requestAnimationFrame
    requestAnimationFrame(updateSize);

    // Listen for window resize
    window.addEventListener("resize", updateSize);
    return () => window.removeEventListener("resize", updateSize);
  }, []);


  const testData2 = Array.from({ length: 20 }, (_, index) => ({
    id: index + 1,
    name: "Name",
    country: "SG",
    score: 0,
  }));

  const [paginatedData, setPaginatedData] = useState([]);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };
  const totalPages = Math.ceil(testData2.length / limit);

  useEffect(() => {
    setLoading(true);

    const fetchData = async () => {
      try {
        const response = await EventService.getEvent(eventID);
        setEventData(response.data);
      } catch (error) {
        console.error("Error fetching event data:", error);
        setError("Failed to load event data.");
      }
    };

    const fetchPouleTable = async () => {
      try {
        const response = await EventService.getPouleTable(eventID);
        setPouleTableData(response.data);
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
        console.log("Error fetching recommended poules", error);
        setError("Failed to load recommended poules");
      }
    };

    const fetchMatches = async () => {
      try {
        const response = await EventService.getMatches(eventID);
        setMatches(response.data);
      } catch (error) {
        console.error("Error fetching matches:", error);
        setError("Failed to load matches.");
      }
    };

    if (eventID) {
      Promise.all([
        fetchData(),
        fetchPouleTable(),
        fetchRecommendedPoules(),
        fetchMatches()
      ]).then(() => {
        // Code to run after all functions complete
        console.log('All functions have completed.');
        setLoading(false);
        console.log("Matches: ", matches);
      });

    }
  }, [eventID]);

  useEffect(() => {
    if (Array.isArray(testData2) && testData2.length) {
      const startIndex = Math.max(0, (currentPage - 1) * limit);
      const endIndex = Math.min(testData2.length, startIndex + limit);
      setPaginatedData(testData2.slice(startIndex, endIndex));
    } else {
      setPaginatedData([]);
    }
  }, []);

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
        eventName += "Male ";
        break;
      case "F":
        eventName += "Female ";
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
      console.log("error creating poules", error);
    }
    closeCreatePopup();
  };

  const updatePoules = () => {
    setIsUpdating(true);
  };

  return (
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        {eventData.tournamentName} -{" "}
        {constructEventName(eventData.gender, eventData.weapon)}
      </h1>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-3 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Date</div>
        <div className="font-semibold text-lg">Start Time</div>
        <div className="font-semibold text-lg">End Time</div>
        <div className="text-lg">{formatDate(eventData.date)}</div>
        <div className="text-lg">{formatTimeTo24Hour(eventData.startTime)}</div>
        <div className="text-lg">{formatTimeTo24Hour(eventData.endTime)}</div>
      </div>

      <div className="ml-12 mr-8 text-lg overflow-x-auto">
        <Tabs parentRef={parentRef}>
          <Tab label="Poules">
            <div className="py-4">
              <button
                onClick={createPoules}
                className="bg-blue-500 text-white px-4 py-2 rounded mt-2 mb-2"
              >
                Create Poules
              </button>
              <div className="flex">
                <label className="block font-medium mb-1 ml-1">
                  Poule Results
                </label>
                <select value={selectedPoule} onChange={handlePouleChange}>
                  <option value="1">Poule 1</option>
                  <option value="2">Poule 2</option>
                </select>
                <button
                  onClick={updatePoules}
                  className="bg-blue-500 text-white px-4 py-2 rounded mt-2 mb-2"
                >
                  Update Poules
                </button>
              </div>
              <table className="table text-lg">
                <thead className="text-lg text-neutral">
                  <tr className="border-b border-gray-300">
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
                  {pouleTableData && pouleTableData.pouleTable[pouleIndex] &&
                    Object.entries(pouleTableData.pouleTable[pouleIndex]).map(
                      ([fencer, results], idx) => {
                        const resultArray = results.split(",");
                        const cleanedFencerName = fencer.replace(/ -- \d+$/, "");
                        return (
                          <tr key={idx} className="border-b border-gray-300">
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
                                {result}
                              </td>
                            ))}
                          </tr>
                        );
                      }
                    )}
                </tbody>
              </table>
            </div>
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
              <EventBracket matches={matches} height={parentSize.height} width={parentSize.width} />
            </div>
          </Tab>
          <Tab label="Ranking">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Ranking</h2> */}
              <table className="table text-lg border-collapse">
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
                  {paginatedData.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-gray-300 hover:bg-gray-100"
                    >
                      <td className="text-center">{item.id}</td>
                      <td>{item.name}</td>
                      <td className="text-center">{item.country}</td>
                      <td className="text-center">{item.score}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
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
              <table className="table text-lg border-collapse">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr className="border-b border-gray-300">
                    <th className="w-20"></th>
                    <th className="w-60">Name</th>
                    <th className="w-60">Club</th>
                    <th className="w-60">Points</th>
                  </tr>
                </thead>
                <tbody>
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
