import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import FencerService from "../../Services/Fencer/FencerService.js";
import TournamentService from "../../Services/Tournament/TournamentService.js";
import CreateEvent from "./CreateEvent.jsx";
import UpdateEvent from "./UpdateEvent.jsx";
import PaginationButton from "../Others/Pagination.jsx";

export default function ViewTournament() {
  // Retrieve tournament ID from URL
  const { tournamentID } = useParams();

  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [isUpdatePopupVisible, setIsUpdatePopupVisible] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;

  const testData = Array.from({ length: 20 }, (_, index) => ({
    id: index + 1,
    name: "Name",
    country: "SG",
    score: 0,
  }));

  const [paginatedData, setPaginatedData] = useState([]);

  // Effect to update the organisers and total pages based on current page
  useEffect(() => {
    const startIndex = (currentPage - 1) * limit;
    const endIndex = startIndex + limit;
    const paginatedData = testData.slice(startIndex, endIndex);
    setPaginatedData(paginatedData); // Set paginated data for the current page
  }, [currentPage]);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };
  const totalPages = Math.ceil(testData.length / limit);

  const navigate = useNavigate();

  // Fetch tournament data if tournamentID changes
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await TournamentService.getTournamentDetails(
          tournamentID
        );
        setTournamentData(response.data);
        console.log("response.data => ", response.data);
      } catch (error) {
        console.error("Error fetching tournament data:", error);
        setError("Failed to load tournament data.");
      } finally {
        setLoading(false);
      }
    };

    if (tournamentID) {
      fetchData();
    }
  }, [tournamentID]);

  const breadcrumbsItems = [
    { name: "Home", link: "/" },
    { name: "Tournaments", link: "/tournaments" },
    {
      name: loading
        ? "Loading..."
        : tournamentData
        ? tournamentData.name
        : "Not Found",
    },
  ];

  // Loading / Error states
  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  const handleSubmit = async (data) => {
    try {
      await FencerService.registerEvent(data).then(() => {
        navigate("/fencer-dashboard");
      });
    } catch (error) {
      console.log(error);
    }
  };

  const formatDateRange = (start, end) => {
    const startDate = new Date(start).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    const endDate = new Date(end).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return `${startDate} - ${endDate}`;
  };

  const getTournamentStatus = (start, end) => {
    const currentDate = new Date();
    const startDate = new Date(start);
    const endDate = new Date(end);

    if (currentDate < startDate) {
      return "Upcoming";
    } else if (currentDate >= startDate && currentDate <= endDate) {
      return "Ongoing";
    } else {
      return "Past";
    }
  };

  // console.log(tournamentData);
  const eventsArray = Array.from(tournamentData.events ?? []);
  // console.log(eventsArray);

  // Create array of the 6 event types
  let eventTypes = [
    "MaleEpee",
    "MaleFoil",
    "MaleSaber",
    "FemaleEpee",
    "FemaleFoil",
    "FemaleSaber",
  ];

  // Display create-event popup on click "Add Event" button

  const closeCreatePopup = () => {
    setIsCreatePopupVisible(false);
  };
  const openCreatePopup = () => {
    setIsCreatePopupVisible(true);
  };
  const submitCreatePopup = (eventDetails) => {
    console.log(eventDetails);
    // Add event to eventsArray
    eventsArray.push(eventDetails);
    // Close popup
    closeCreatePopup();
  };

  // Display update-event popup on click "Update Event" button

  const closeUpdatePopup = () => {
    setIsUpdatePopupVisible(false);
  };
  const openUpdatePopup = (event) => {
    setSelectedEvent(event);
    setIsUpdatePopupVisible(true);
  };
  const submitUpdatePopup = (eventDetails) => {
    console.log(eventDetails);
    eventsArray.push(eventDetails);
    closeUpdatePopup();
  };

  return (
    // Grid for Navbar, Sidebar and Content

    <div className="row-span-2 col-start-2 bg-gray-300 h-full overflow-y-auto">
      <Breadcrumbs items={breadcrumbsItems} />
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        {tournamentData.name}
      </h1>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-4 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Organiser</div>
        <div className="font-semibold text-lg">Dates</div>
        <div className="font-semibold text-lg">Location</div>
        <div className="font-semibold text-lg">Status</div>
        <div className="text-lg">{tournamentData.organiserName}</div>
        <div className="text-lg">
          {formatDateRange(tournamentData.startDate, tournamentData.endDate)}
        </div>
        <div className="text-lg">{tournamentData.location}</div>
        <div className="text-lg">
          {getTournamentStatus(
            tournamentData.startDate,
            tournamentData.endDate
          )}
        </div>
      </div>

      <div className="ml-12 mr-8 text-lg overflow-x-auto">
        <Tabs>
          <Tab label="Overview">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Rules</h2>
              <p className="text-gray-700">{tournamentData.rules}</p>
              <h2 className="text-lg font-medium mb-2">Description</h2>
              <p className="text-gray-700">{tournamentData.description}</p>
            </div>
          </Tab>
          <Tab label="Events">
            <table className="table text-lg">
              {/* head */}
              <thead className="text-lg">
                <tr>
                  <th></th>
                  <th>Event Name</th>
                  <th>Date</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>
                    {sessionStorage.getItem("userType") === "O"
                      ? "Update Event"
                      : "Sign Up"}
                  </th>
                </tr>
              </thead>
              <tbody>
                {/* Render events */}
                {eventsArray.length > 0 ? (
                  eventsArray.map((event, index) => (
                    <tr key={index}>
                      <td>{/* Event details */}</td>
                      <td>
                        <a
                          href={`/view-event/${event.id}`}
                          className="underline hover:text-accent"
                        >
                          {event.eventName}
                        </a>
                      </td>
                      <td>{event.date}</td>
                      <td>{event.startTime}</td>
                      <td>{event.endTime}</td>
                      <td>
                        <button
                          key={event.id}
                          onClick={() => openUpdatePopup(event)}
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                        >
                          Update {event.eventName}
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" className="text-center">
                      No events available.
                    </td>
                  </tr>
                )}
                {/* Add Event button row only if organiser */}
                {sessionStorage.getItem("userType") === "O" && (
                  <tr>
                    <td colSpan="6" className="text-center">
                      <button
                        onClick={openCreatePopup}
                        className="bg-blue-500 text-white px-4 py-2 rounded"
                      >
                        Add Event
                      </button>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
            {/* Create Event Popup --> need to pass in submit/close */}
            {isCreatePopupVisible && (
              <CreateEvent
                onClose={closeCreatePopup}
                onSubmit={submitCreatePopup}
              />
            )}

            {/* Update Event Popup --> need to pass in submit/close */}
            {isUpdatePopupVisible && (
              <UpdateEvent
                onClose={closeUpdatePopup}
                onSubmit={submitUpdatePopup}
                selectedEvent={selectedEvent}
              />
            )}
          </Tab>
          <Tab label="Tournament Ranking">
            <div className="py-4">
              <label className="block font-medium ml-1 mb-1">Event</label>
              <select>
                <option value="">Select Event</option>
                {eventsArray.map((event) => (
                  <option key={event.id} value={event.id}>
                    {event.eventName}
                  </option>
                ))}
              </select>
              <table className="table text-lg">
                {/* head */}
                <thead className="text-lg">
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
        </Tabs>
      </div>
    </div>
  );
}
