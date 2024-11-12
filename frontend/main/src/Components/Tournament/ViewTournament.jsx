import { DateTime, FixedOffsetZone } from "luxon";
import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import DropdownMenu from "../Others/DropdownMenu.jsx";
import EventService from "../../Services/Event/EventService";
import FencerService from "../../Services/Fencer/FencerService.js";
import Organiser from "../../Services/Organiser/OrganiserService.js";
import TournamentService from "../../Services/Tournament/TournamentService.js";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import { Tab, Tabs } from "../Others/Tabs.jsx";
import CreateEvent from "./CreateEvent.jsx";
import UpdateEvent from "./UpdateEvent.jsx";
import DeleteEvent from "./DeleteEvent.jsx";
import SubmitButton from "../Others/SubmitButton.jsx";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

export default function ViewTournament() {
  // Retrieve tournament ID from URL
  const { tournamentID } = useParams();

  const [tournamentData, setTournamentData] = useState(null);
  // Check if organiser is the owner of the tournament
  const [isOwner, setIsOwner] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [addEventError, setAddEventError] = useState(null);
  const [eventsArray, setEventsArray] = useState([]);
  // One Popup for create-event the other for update-event
  const [isCreatePopupVisible, setIsCreatePopupVisible] = useState(false);
  const [isUpdateEventPopupVisible, setIsUpdateEventPopupVisible] =
    useState(false);
  const [registeredEvents, setRegisteredEvents] = useState([]);
  const [registerEventError, setRegisterEventError] = useState(null);
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
    { value: "WF", label: "Women's Foil" },
    { value: "WE", label: "Women's Épée" },
    { value: "WS", label: "Women's Sabre" },
  ];
  const [eventTypes, setEventTypes] = useState(allEventTypes);
  const [newEventsArray, setNewEventsArray] = useState([]);

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
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError("Tournament Data has failed to load, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("Tournament Data has failed to load, please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  // Fetch Registered Tournament Events
  const fetchRegisteredEvents = async () => {
    try {
      const response = await FencerService.getFencerUpcomingEvents();
      console.log("Response Data: ", response.data);
      // Assuming response.data is an array of Event Objects
      const eventIds = response.data.map((event) => event.id);
      console.log("Registered Events: ", eventIds);
      setRegisteredEvents(eventIds);
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError(
          "Registered Event Data has failed to load, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError(
          "Registered Event Data has failed to load, please try again later."
        );
      }
    }
  };

  // Fetch Upcoming Tournament if Organiser to check if organiser is the owner of current tournament
  const checkIfOwner = async () => {
    try {
      const response = await Organiser.getOrganiserUpcomingTournaments();
      const upcomingTournaments = response.data;
      let found = false;
      for (let i = 0; i < upcomingTournaments.length; i++) {
        if (upcomingTournaments[i].id == tournamentID) {
          found = true;
          break;
        }
      }
      setIsOwner(found);
    } catch (error) {
      console.error(
        "Error fetching upcoming tournaments for organiser:",
        error
      );
      setError("Failed to load Tournament Data.");
    }
  };

  // Fetch fetch data when tournamentID changes
  const fetchData = async () => {
    if (tournamentID) {
      await fetchTournamentData();

      if (sessionStorage.getItem("userType") === "F") {
        fetchRegisteredEvents();
      } else if (sessionStorage.getItem("userType") === "O") {
        checkIfOwner();
      }
    }
  };
  // Fetch data whenever tournamentID changes
  useEffect(() => {
    fetchData();
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
    return (
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-2xl font-semibold">{error}</h1>
      </div>
    ); // Show error message if any
  }

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
    const date = data.eventDate;
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
    setNewEventsArray([...newEventsArray, formData]);
    checkEvents();

    // Close the popup and set isCreating to true
    closeCreatePopup();
    setIsCreating(true);
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
    try {
      const response = await EventService.createEvents(
        tournamentID,
        newEventsArray
      );
      // Set isCreating to false
      setIsCreating(false);
      // Set NewEventArray to empty
      setNewEventsArray([]);
      // Only re-fetch data if successful
      fetchData();
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setAddEventError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setAddEventError("An error has occured, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setAddEventError("An error has occured, please try again later.");
      }
    }
  };

  // Check if today is past the start date of the tournament (for the register button)
  const isPastStartDate = () => {
    const today = new Date();
    const eventStartDate = new Date(tournamentData.signupEndDate);
    return today > eventStartDate;
  };

  // Return Proper Event Names in table (instead of initials)
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

  const registerEvent = async (eventID) => {
    console.log("Registering event with ID:", eventID);
    try {
      await EventService.registerEvent(eventID).then(() => {
        fetchRegisteredEvents();
        fetchData();
      });
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setRegisterEventError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setRegisterEventError(
          "Event registration has failed, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setRegisterEventError(
          "Event registration has failed, please try again later."
        );
      }
    }
  };

  const unregisterEvent = async (eventID) => {
    console.log("Unregistering event with ID:", eventID);
    try {
      await EventService.unregisterEvent(eventID).then(() => {
        fetchRegisteredEvents();
        fetchData();
      });
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setRegisterEventError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setRegisterEventError(
          "Event registration/deregistration has failed, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setRegisterEventError(
          "Event registration/deregistration has failed, please try again later."
        );
      }
    }
  };

  const closeUpdateEventPopup = () => {
    setIsUpdateEventPopupVisible(false);
    fetchData();
  };
  const updateEvent = (selectedEvent) => {
    setIsUpdateEventPopupVisible(true);
    setSelectedEvent(selectedEvent);
  };

  const closeDeleteEventPopUp = () => {
    setIsDeleteEventPopUpVisible(false);
    fetchData();
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

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return `${formattedDate}`;
  };

  return (
    // Grid for Navbar, Sidebar and Content
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <Breadcrumbs items={breadcrumbsItems} />
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-4xl font-semibold">
          {tournamentData.name}
        </h1>
      </div>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-3 auto-rows-fr gap-x-[10px]">
        <div className="font-semibold text-lg">Organiser</div>
        <div className="font-semibold text-lg">Difficulty</div>
        <div className="font-semibold text-lg">Dates</div>
        <div className="text-lg mt-[-8px]">{tournamentData.organiserName}</div>
        <div className="text-lg mt-[-8px]">
          {formatDifficulty(tournamentData.difficulty)}
        </div>
        <div className="text-lg mt-[-8px]">
          {formatDateRange(tournamentData.startDate, tournamentData.endDate)}
        </div>

        <div className="font-semibold text-lg mt-2">Signup End Date</div>
        <div className="font-semibold text-lg mt-2">Location</div>
        <div className="font-semibold text-lg mt-2">Status</div>
        <div className="text-lg">
          {formatDate(tournamentData.signupEndDate)}
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
            <table
              className="table text-lg text-center border-collapse"
              style={{ position: "relative", zIndex: 1 }}
            >
              {/* head */}
              <thead className="text-lg text-primary">
                <tr className="border-b border-gray-300">
                  {/* <th></th> */}
                  <th>Event Name</th>
                  <th>Date</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>Participant Count</th>
                  {sessionStorage.getItem("userType") === "F" && (
                    <th>Register</th>
                  )}
                  {sessionStorage.getItem("userType") === "O" && isOwner && (
                    <th></th>
                  )}
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
                        {isCreating ? (
                          constructEventName(event.gender, event.weapon)
                        ) : (
                          <Link
                            to={{
                              pathname: `/${tournamentID}/view-event/${event.id}`,
                            }}
                            className="underline hover:text-primary"
                          >
                            {constructEventName(event.gender, event.weapon)}
                          </Link>
                        )}
                      </td>
                      <td>{formatDate(event.eventDate || event.date)}</td>
                      <td>{formatTimeTo24Hour(event.startTime)}</td>
                      <td>{formatTimeTo24Hour(event.endTime)}</td>
                      <td>{event.fencers ? event.fencers.length : 0}</td>
                      <td>
                        {sessionStorage.getItem("userType") === "F" &&
                          isPastStartDate() && (
                            <SubmitButton
                              disabled={true}
                              styling={`h-12 w-40 justify-center rounded-md my-5 text-lg font-semibold leading-6 text-white shadow-sm bg-gray-500`}
                            >
                              Signups Ended
                            </SubmitButton>
                          )}
                        {sessionStorage.getItem("userType") === "F" &&
                          !isPastStartDate() &&
                          registeredEvents.includes(event.id) && (
                            <SubmitButton
                              styling={`h-12 w-40 justify-center rounded-md my-5 text-lg font-semibold leading-6 text-white shadow-sm bg-green-400`}
                              onSubmit={() => unregisterEvent(event.id)}
                            >
                              Unregister
                            </SubmitButton>
                          )}
                        {sessionStorage.getItem("userType") === "F" &&
                          !isPastStartDate() &&
                          !registeredEvents.includes(event.id) && (
                            <SubmitButton
                              styling={`h-12 w-40 justify-center rounded-md my-5 bg-indigo-600 text-lg font-semibold leading-6 text-white shadow-sm `}
                              onSubmit={() => {
                                registerEvent(event.id);
                              }}
                            >
                              Register
                            </SubmitButton>
                          )}
                        {sessionStorage.getItem("userType") === "O" &&
                          isOwner &&
                          !isCreating &&
                          getTournamentStatus(
                            tournamentData.startDate,
                            tournamentData.endDate
                          ) === "Upcoming" && (
                            <DropdownMenu
                              entity="Event"
                              updateEntity={() => updateEvent(event)}
                              deleteEntity={() => deleteEvent(event)}
                            />
                          )}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan="6"
                      className="text-center border-b border-gray-300"
                    >
                      No events available.
                    </td>
                  </tr>
                )}
                {/* Add Event button row only if organiser and isOwner */}
                {sessionStorage.getItem("userType") === "O" && isOwner && (
                  <>
                    {addEventError && (
                      <tr className="border-transparent">
                        <td colSpan="6" className="text-center text-red-500">
                          {addEventError}
                        </td>
                      </tr>
                    )}
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
                        {isOwner &&
                          getTournamentStatus(
                            tournamentData.startDate,
                            tournamentData.endDate
                          ) === "Upcoming" && (
                            <button
                              onClick={openCreatePopup}
                              className="bg-blue-500 text-white px-4 py-2 rounded mx-36 mt-10"
                            >
                              Add Event
                            </button>
                          )}
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
                  </>
                )}
              </tbody>
            </table>
            {registerEventError && (
              <h2 className="text-red-500 text-center mt-4">
                {" "}
                {registerEventError}{" "}
              </h2>
            )}
          </Tab>
        </Tabs>
        <div style={{ position: "fixed", zIndex: 10 }}>
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
          {isUpdateEventPopupVisible && (
            <UpdateEvent
              onClose={closeUpdateEventPopup}
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
        </div>
      </div>
    </div>
  );
}
