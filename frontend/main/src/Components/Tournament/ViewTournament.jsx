import { DateTime } from "luxon";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import EventDropdownMenu from "../Others/EventDropdownMenu.jsx";
import EventService from "../../Services/Event/EventService";
import FencerService from "../../Services/Fencer/FencerService.js";
import TournamentService from "../../Services/Tournament/TournamentService.js";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import { Tab, Tabs } from "../Others/DashboardTabs.jsx";
import CreateEvent from "./CreateEvent.jsx";
import UpdateEvent from "./UpdateEvent.jsx";
import DeleteEvent from "./DeleteEvent.jsx";
import UpdateTournament from "./UpdateTournament.jsx";
import SubmitButton from "../Others/SubmitButton.jsx";
import editLogo from "../../Assets/edit.png";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

export default function ViewTournament() {
  // Retrieve tournament ID from URL
  const { tournamentID } = useParams();

  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [eventsArray, setEventsArray] = useState([]);
  //Popup for updating tournament
  const [isUpdateTournamentPopupVisible, setIsUpdateTournamentPopupVisible] =
    useState(false);
  const [tournamentToUpdate, setTournamentToUpdate] = useState(false);
  // One Popup for create-event the other for update-event
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [isUpdatePopupVisible, setIsUpdatePopupVisible] = useState(false);
  const [registeredEvents, setRegisteredEvents] = useState([]);
  const [isDeleteEventPopUpVisible, setIsDeleteEventPopUpVisible] =
    useState(false);
  // Selected Event for deletion / update (organiser)
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [isCreating, setIsCreating] = useState(false);
  const allEventTypes = [
    { value: "", label: "Select Weapon" },
    { value: "MF", label: "Men's Foil" },
    { value: "ME", label: "Men's Épée" },
    { value: "MS", label: "Men's Sabre" },
    { value: "FF", label: "Women's Foil" },
    { value: "FE", label: "Women's Épée" },
    { value: "FS", label: "Women's Sabre" },
  ];
  const [eventTypes, setEventTypes] = useState(allEventTypes);

  const navigate = useNavigate();

  // Fetch tournament data
  const fetchTournamentData = async () => {
    try {
      const response = await TournamentService.getTournamentDetails(
        tournamentID
      );
      setTournamentData(response.data);
      console.log("response.data => ", response.data);
      // Set eventsArray
      const eventsArray = response.data.events; // Accessing events directly
      setEventsArray(eventsArray);
    } catch (error) {
      console.error("Error fetching tournament data:", error);
      setError("Failed to load tournament data.");
    } finally {
      setLoading(false);
    }
  };

  // Fetch fetch data when tournamentID changes
  useEffect(() => {
    if (tournamentID) {
      fetchTournamentData();
    }
  }, [tournamentID]);

  const userType = sessionStorage.getItem("userType");

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
      const response = await EventService.createEvents(
        tournamentID,
        newEventsArray
      );
    } catch (error) {
      console.log(error);
    }
    // Set isCreating to false
    setIsCreating(false);
  };

  const registerEvent = async (eventID) => {
    console.log("Registering event with ID:", eventID);
    try {
      await EventService.registerEvent(eventID).then(() => {
        setRegisteredEvents((prevEvents) => [...prevEvents, eventID]);
        navigate("/fencer-dashboard");
      });
    } catch (error) {
      if (error.response && error.response.status === 403) {
        console.error("You do not have permission to register for this event.");
      } else {
        console.log(error);
      }
    }
  };

  const closeUpdatePopup = () => {
    setIsUpdatePopupVisible(false);
  };
  const updateEvent = (selectedEvent) => {
    setIsUpdatePopupVisible(true);
    setSelectedEvent(selectedEvent);
  };

  const closeDeleteEventPopUp = () => {
    setIsDeleteEventPopUpVisible(false);
  };
  const deleteEvent = (selectedEvent) => {
    setIsDeleteEventPopUpVisible(true);
    setSelectedEvent(selectedEvent);
  };

  const formatDifficulty = (difficultyChar) => {
    let difficulty = "";

    if (difficultyChar === "B") {
      difficulty = "Beginner";
    } else if (difficultyChar === "I") {
      difficulty = "Intermediate";
    } else if (difficultyChar === "A") {
      difficulty = "Advanced";
    } else {
      difficulty = "Unknown";
    }

    return difficulty;
  };

  const updateTournament = (tournamentToUpdate) => {
    setIsUpdateTournamentPopupVisible(true);
    setTournamentToUpdate(tournamentToUpdate);
  };

  const closeUpdateTournamentPopup = () => {
    setIsUpdateTournamentPopupVisible(false);
    setTournamentToUpdate(null);
  };

  const submitUpdateTournament = async (data) => {
    try {
      console.log(data);
      await TournamentService.updateTournament(tournamentID, data);
    } catch (error) {
      console.error("Error updating tournament:", error);
      // Add error notification here
    }
  };

  return (
    // Grid for Navbar, Sidebar and Content
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <Breadcrumbs items={breadcrumbsItems} />
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-4xl font-semibold">
          {tournamentData.name}
        </h1>
        <div className="cursor-pointer text-gray-600">
          <img
            src={editLogo}
            alt="Edit Tournament"
            className="w-6 h-6"
            onClick={updateTournament}
          />
        </div>
      </div>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-5 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Organiser</div>
        <div className="font-semibold text-lg">Difficulty</div>
        <div className="font-semibold text-lg">Dates</div>
        <div className="font-semibold text-lg">Location</div>
        <div className="font-semibold text-lg">Status</div>
        <div className="text-lg">{tournamentData.organiserName}</div>
        <div className="text-lg">
          {formatDifficulty(tournamentData.difficulty)}
        </div>
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
      {/* Create Event Popup --> need to pass in submit/close */}
      {isUpdateTournamentPopupVisible && (
        <UpdateTournament
          onClose={closeUpdateTournamentPopup}
          onSubmit={submitUpdateTournament}
        />
      )}
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
            <table
              className="table text-lg text-center border-collapse "
              style={{ zIndex: 20 }}
            >
              {/* head */}
              <thead className="text-lg text-primary">
                <tr className="border-b border-gray-300">
                  {/* <th></th> */}
                  <th>Event Name</th>
                  <th>Date</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>
                    {sessionStorage.getItem("userType") === "O"
                      ? ""
                      : "Register"}
                  </th>
                </tr>
              </thead>
              <tbody>
                {/* Render events */}
                {eventsArray.length > 0 ? (
                  eventsArray.map((event, index) => (
                    <tr
                      key={index}
                      className="border-b border-gray-300 hover:bg-gray-100"
                    >
                      {/* <td>Event details</td> */}
                      <td>
                        <a
                          href={`/view-event/${event.id}`}
                          className="underline hover:text-primary"
                        >
                          {constructEventName(event.gender, event.weapon)}
                        </a>
                        {/* no eventName attribute in new backend (pending) --> {event.eventName} */}
                      </td>
                      <td>{event.date}</td>
                      <td>{formatTimeTo24Hour(event.startTime)}</td>
                      <td>{formatTimeTo24Hour(event.endTime)}</td>
                      <td>
                        {sessionStorage.getItem("userType") === "F" && (
                          <SubmitButton
                            onSubmit={() => registerEvent(event.id)}
                            disabled={registeredEvents.includes(event.id)}
                          >
                            {registeredEvents.includes(event.id)
                              ? "Registered"
                              : "Register"}
                          </SubmitButton>
                        )}
                        {sessionStorage.getItem("userType") === "O" && (
                          <EventDropdownMenu
                            updateEvent={() => updateEvent(event)}
                            deleteEvent={() => deleteEvent(event.id)}
                          />
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
                tournamentDates={[
                  tournamentData.startDate,
                  tournamentData.endDate,
                ]}
              />
            )}

            {/* Update Event Popup --> need to pass in submit/close */}
            {isUpdatePopupVisible && (
              <UpdateEvent
                onClose={closeUpdatePopup}
                selectedEvent={selectedEvent}
                tournamentDates={[
                  tournamentData.startDate,
                  tournamentData.endDate,
                ]}
                fetchTournamentData={fetchTournamentData}
              />
            )}

            {/* Delete Event Popup --> need to pass in submit/close */}
            {isDeleteEventPopUpVisible && (
              <DeleteEvent
                id={selectedEvent.id}
                closeDeleteEventPopUp={closeDeleteEventPopUp}
              />
            )}
          </Tab>
        </Tabs>
      </div>
    </div>
  );
}
