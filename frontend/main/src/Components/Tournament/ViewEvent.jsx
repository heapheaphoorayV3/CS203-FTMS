import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ViewCard from "../Others/ViewCard.jsx";
import EventList from "../Others/EventList.jsx";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import EventService from "../../Services/Event/EventService.js";
import PaginationButton from "../Others/Pagination.jsx";
import EventBracket from "./EventBracket.jsx";
import CreatePoules from "./CreatePoules.jsx";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

export default function ViewEvent() {
  const { eventID } = useParams();

  const [eventData, setEventData] = useState(null);
  const [pouleTableData, setPouleTableData] = useState(null);
  const [selectedPoule, setSelectedPoule] = useState("");
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [recommendedPoulesData, setRecommendedPoulesData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;

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
        console.log("response.data => ", response.data);
      } catch (error) {
        console.error("Error fetching event data:", error);
        setError("Failed to load event data.");
      } finally {
        setLoading(false);
      }
    };

    const fetchPouleTable = async () => {
      try {
        const response = await EventService.getPouleTable(eventID);
        setPouleTableData(response.data);
        console.log("Poule Table Data:", response.data);
      } catch (error) {
        console.error("Error fetching poule table data:", error);
        setError("Failed to load poule table data.");
      }
    };

    const fetchRecommendedPoules = async () => {
      try {
        const response = await EventService.getRecommendedPoules(eventID);
        setRecommendedPoulesData(response.data);
        console.log("Recommended Poules:", response.data);
      } catch (error) {
        console.log("Error fetching recommended poules", error);
        setError("Failed to load recommended poules");
      }
    };

    if (eventID) {
      fetchData();
      fetchPouleTable();
      fetchRecommendedPoules();
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
  }, [currentPage, limit, testData2]);

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

  const testData = {
    pouleTable: [
      {
        "3 Fencer (Singapore) -- 4": "-1,0,0,0,0",
        "1 Fencer (Singapore) -- 2": "0,-1,0,0,0",
        "5 Fencer (Singapore) -- 6": "0,0,-1,0,0",
        "7 Fencer (Singapore) -- 8": "0,0,0,-1,0",
        "4 Fencer (Singapore) -- 5": "0,0,0,0,-1",
      },
      {
        "9 Fencer (Singapore) -- 10": "-1,0,0,0,0",
        "6 Fencer (Singapore) -- 7": "0,-1,0,0,0",
        "10 Fencer (Singapore) -- 11": "0,0,-1,0,0",
        "2 Fencer (Singapore) -- 3": "0,0,0,-1,0",
        "8 Fencer (Singapore) -- 9": "0,0,0,0,-1",
      },
    ],
  };

  const createPoules = () => {
    setIsCreatePopupVisible(true);
  };

  const closeCreatePopup = () => {
    setIsCreatePopupVisible(false);
  };

  const submitCreatePopup = async (data) => {
    const formData = new FormData();

    formData.append("eid", String(eventID));

    console.log("Received data:", data);

    Object.keys(data).forEach((key) => {
      formData.append(key, data[key]);
    });
    try {
      await EventService.createPoules(formData);
    } catch (error) {
      console.log("error creating poules", error);
    }
    closeCreatePopup();
  };

  return (
    <div className="row-span-2 col-start-2 bg-gray-200 h-full overflow-y-auto">
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
        <Tabs>
          <Tab label="Poules">
            <div className="py-4">
              <label className="block font-medium mb-1 ml-1">
                Create Poules
              </label>
              <button
                onClick={createPoules}
                className="bg-blue-500 text-white px-4 py-2 rounded mt-2 mb-2"
              >
                Create Poules
              </button>
              <label className="block font-medium mb-1 ml-1">
                Poule Results
              </label>
              <select value={selectedPoule} onChange={handlePouleChange}>
                <option value="1">Poule 1</option>
                <option value="2">Poule 2</option>
              </select>
              <table className="table text-lg">
                {/* head */}
                <thead className="text-lg text-neutral">
                  <tr>
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
                  {Object.entries(testData.pouleTable[pouleIndex]).map(
                    ([fencer, results], idx) => {
                      const resultArray = results.split(",");
                      const cleanedFencerName = fencer.replace(/ -- \d+$/, "");
                      return (
                        <tr key={idx}>
                          <td className="w-60">{cleanedFencerName}</td>
                          <td className="font-bold text-center border-r border-black w-24">
                            {idx + 1}
                          </td>
                          {resultArray.map((result, resultIndex) => (
                            <td
                              key={resultIndex}
                              className="border border-black"
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
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Bracket</h2>
              {/* <EventBracket matches={eventData.matches} /> */}
            </div>
          </Tab>
          <Tab label="Ranking">
            <div className="py-4">
              {/* <h2 className="text-lg font-medium mb-2">Ranking</h2> */}
              <table className="table text-lg">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr>
                    <th className="text-center w-20">Rank</th>
                    <th className="w-1/2">Name</th>
                    <th className="text-center">Country</th>
                    <th className="text-center">Points</th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedData.map((item) => (
                    <tr key={item.id}>
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
              <table className="table text-lg">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr>
                    <th className="w-20"></th>
                    <th className="w-60">Name</th>
                    <th className="w-60">Club</th>
                    <th className="w-60">Points</th>
                  </tr>
                </thead>
                <tbody>
                  {eventData.fencers.map((fencer, index) => (
                    <tr key={fencer.id}>
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
