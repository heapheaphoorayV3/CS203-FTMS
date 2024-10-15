import { DateTime } from "luxon";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import EventService from "../../Services/Event/EventService";
import FencerService from "../../Services/Fencer/FencerService.js";
import TournamentService from "../../Services/Tournament/TournamentService.js";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import { Tab, Tabs } from "../Others/DashboardTabs.jsx";
import CreateEvent from "./CreateEvent.jsx";
import SubmitButton from "../Others/SubmitButton.jsx";
import EventBracket from "./EventBracket.jsx";
import PaginationButton from "../Others/Pagination.jsx";

export default function ViewTournament() {
  // Retrieve tournament ID from URL
  const { tournamentID } = useParams();

  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [eventsArray, setEventsArray] = useState();
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [registeredEvents, setRegisteredEvents] = useState([]);
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
  const [isCreating, setIsCreating] = useState(false);
  const allEventTypes = [
    { value: "", label: "Select Weapon" },
    { value: "MF", label: "Male Foil" },
    { value: "ME", label: "Male Épée" },
    { value: "MS", label: "Male Sabre" },
    { value: "FF", label: "Female Foil" },
    { value: "FE", label: "Female Épée" },
    { value: "FS", label: "Female Sabre" },
  ];
  const [eventTypes, setEventTypes] = useState(allEventTypes);

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
        // Set eventsArray
        setEventsArray(response.data.events);
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

  // Check if event has already been added --> remove from eventTypes (dropdown)
  // Called when "Add Event"
  const checkEvents = () => {
    // Reset eventTypes to allEventTypes
    setEventTypes(allEventTypes);
    // Loop through eventsArray
    console.log("Checking Events");
    eventsArray.forEach((event) => {
      const eventName = event.gender + event.weapon;
      removeEventType(eventName);
    });
  };
  // Remove eventType from eventTypes
  const removeEventType = (event) => {
    setEventTypes((prevEventTypes) =>
      prevEventTypes.filter((eventType) => eventType.value !== event)
    );
  };

  // Create-Event "Add Event" button
  const openCreatePopup = () => {
    checkEvents();
    setIsCreatePopupVisible(true);
  };
  const closeCreatePopup = () => {
    setIsCreatePopupVisible(false);
  };
  const submitCreatePopup = async (data) => {
    // modify time to add seconds
    const startTimeString = data.startTime + ":00"; // Adding seconds
    const endTimeString = data.endTime + ":00"; // Adding seconds
    // Create DateTime objects
    const startTime = DateTime.fromFormat(startTimeString, "HH:mm:ss");
    const endTime = DateTime.fromFormat(endTimeString, "HH:mm:ss");
    // Format to hh:mm:ss
    const formattedStartTime = startTime.toFormat("HH:mm:ss");
    const formattedEndTime = endTime.toFormat("HH:mm:ss");
    // Extract gender and weapon from eventName
    const gender = data.eventName.charAt(0); // First character
    const weapon = data.eventName.charAt(1); // Second character
    // Extract minParticipants and date
    const minParticipants = data.minParticipants;
    const date = data.date;
    // Create JSON to add to eventsArray
    const formData = {
      gender: gender,
      weapon: weapon,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
      minParticipants: minParticipants,
      date: date,
    };

    // Add event to eventsArray and delete from eventTypes
    console.log("FormData: " + JSON.stringify(formData));
    setEventsArray([...eventsArray, formData]);
    console.log("CurrentEventsArray: " + JSON.stringify(eventsArray));
    checkEvents();
    console.log("Event Types Left: " + JSON.stringify(eventTypes));

    // Close the popup and set isCreating to true
    closeCreatePopup();
    setIsCreating(true);
  };

  // Return Proper Event Names in table (instead of initials)
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

  
  // Get New Events Array (events without key "fencers")
  const extractNewEvents = () => {
    let newEventsArray = [];
    eventsArray.forEach((event) => {
      if (!event.hasOwnProperty("fencers")) {
        newEventsArray.push(event);
      }
    });
    return newEventsArray;
  };






  // "Cancel Changes"
  const cancelCreatingChanges = async () => {
    console.log("Cancelling Changes");
    try {
      const response = await TournamentService.getTournamentDetails(
        tournamentID
      );
      setTournamentData(response.data);
      // Set eventsArray
      setEventsArray(response.data.events);
    } catch (error) {
      console.error("Error fetching tournament data:", error);
      setError("Failed to load tournament data.");
    } finally {
      setLoading(false);
    }
    setIsCreating(false);
  };
  // "Confirm Changes" --> Submit Events Array
  const submitEventsArray = async () => {
    // Only submit new events --> old events have key "fencers"
    let newEventsArray = extractNewEvents(eventsArray);
    try {
      const response = await EventService.createEvents(tournamentID, newEventsArray);
    } catch (error) {
      console.log(error);
    }
    // Set isCreating to false
    setIsCreating(false);
  };

  const registerEvent = async (eventID) => {
    try {
      await EventService.registerEvent(eventID).then(() => {
        // After successful registration, add the event ID to the state
        setRegisteredEvents((prevEvents) => [...prevEvents, eventID]);
        navigate("/fencer-dashboard");
      });
    } catch (error) {
      console.log(error);
    }
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
            <table className="table text-lg text-center">
              {/* head */}
              <thead className="text-lg">
                <tr>
                  {/* <th></th> */}
                  <th>Event Name</th>
                  <th>Date</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>
                    {sessionStorage.getItem("userType") === "O"
                      ? "Delete Event"
                      : "Register"}
                  </th>
                </tr>
              </thead>
              <tbody>
                {/* Render events */}
                {eventsArray.length > 0 ? (
                  eventsArray.map((event, index) => (
                    <tr key={index}>
                      {/* <td>Event details</td> */}
                      <td>
                        <a
                          href={`/view-event/${event.id}`}
                          className="underline hover:text-accent"
                        >
                          {constructEventName(event.gender, event.weapon)}
                        </a>
                        {/* no eventName attribute in new backend (pending) --> {event.eventName} */}
                      </td>
                      <td>{event.date}</td>
                      <td>{event.startTime}</td>
                      <td>{event.endTime}</td>
                      <td>
                        {sessionStorage.getItem("userType") === "F" ? (
                          <SubmitButton
                            onSubmit={() => registerEvent(event.id)}
                            disabled={registeredEvents.includes(event.id)}
                          >
                            {registeredEvents.includes(event.id)
                              ? "Registered"
                              : "Register"}
                          </SubmitButton>
                        ) : (
                          <span>delete event button</span>
                          /* <SubmitButton
                            onSubmit={() => registerEvent(event.id)}
                            disabled={registeredEvents.includes(event.id)}
                          >
                            {registeredEvents.includes(event.id)
                              ? "Registered"
                              : "Register"}
                          </SubmitButton> */
                        )}
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
                      {isCreating && (
                        <button
                          onClick={cancelCreatingChanges}
                          className="bg-red-400 text-white px-4 py-2 rounded"
                        >
                          Cancel Changes
                        </button>
                      )}
                      <button
                        onClick={openCreatePopup}
                        className="bg-blue-500 text-white px-4 py-2 rounded mx-36 mt-10"
                      >
                        Add Event
                      </button>
                      {isCreating && (
                        <button
                          onClick={submitEventsArray}
                          className="bg-green-400 text-white px-4 py-2 rounded"
                        >
                          Confirm Changes
                        </button>
                      )}
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
                eventTypes={eventTypes}
                tournamentDates={[tournamentData.startDate, tournamentData.endDate]}
              />
            )}
          </Tab>
          <Tab label="Tournament Ranking">
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
          </Tab>
        </Tabs>
      </div>
    </div>
  );
}
