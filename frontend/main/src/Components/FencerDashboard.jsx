import React, { useEffect, useLayoutEffect, useState } from "react";
import editLogo from "../Assets/edit.png";
import FencerService from "../Services/Fencer/FencerService";
import { Tabs, Tab } from "./Others/Tabs";
import LineGraph from "./Others/LineGraph";
import { Link } from "react-router-dom";
import EventService from "../Services/Event/EventService";

function formatTimeTo24Hour(timeString) {
  const [hours, minutes] = timeString.split(":"); // Get hours and minutes
  return `${hours}${minutes}`; // Return formatted time
}

const FencerDashboard = () => {
  const [userData, setUserData] = useState({});
  const [rankingData, setRankingData] = useState(null);
  const [upcomingEvents, setUpcomingEvents] = useState([]);
  const [pastEvents, setPastEvents] = useState([]);
  const [pastRank, setPastRank] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isInputValid, setIsInputValid] = useState(true);
  const [editedData, setEditedData] = useState({
    gender: "",
    weapon: "",
    dominantArm: "",
    debutYear: "",
    club: "",
  });
  // Hooks for Graph data (pastEvents will store the pat event names while the following will store the points)
  const [pastEventPoints, setPastEventPoints] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await FencerService.getProfile();
        setUserData(response.data);
      } catch (error) {
        console.error("Error fetching user data:", error);
        setError("Failed to load user data.");
      } finally {
        setLoading(false);
      }
    };

    const fetchInternationalRanking = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getInternationalRanking();
        setRankingData(response.data);
      } catch (error) {
        console.error("Error fetching international ranking: ", error);
        setError("Failed to load international ranking");
      } finally {
        setLoading(false);
      }
    };

    const fetchUpcomingEvents = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getFencerUpcomingEvents();
        const sortedEvents = response.data.sort((a, b) => {
          const dateA = new Date(a.eventDate);
          const dateB = new Date(b.eventDate);
          return dateA - dateB;
        });
    
        setUpcomingEvents(response.data);
      } catch (error) {
        console.error("Error fetching upcoming events: ", error);
        setError("Failed to load upcoming events");
      } finally {
        setLoading(false);
      }
    };

    const fetchPastEvents = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getFencerPastEvents();
        setPastEvents(response.data);
      } catch (error) {
        console.error("Error fetching past events: ", error);
        setError("Failed to load past events");
      } finally {
        setLoading(false);
      }
    };

    const fetchPastRank = async () => {
      if (!pastEvents || pastEvents.length === 0) return;
      setLoading(true);
      try {
        const eventIDs = pastEvents.map((event) => event.id);
        const ranks = [];

        for (const eventId of eventIDs) {
          const response = await EventService.getEventRanking(eventId);
          let userRank = response.data.find(
            (arr) => arr.fencerId === userData.id
          );

          if (userRank) {
            ranks.push({ eventId, rank: userRank.tournamentRank });
          }
        }

        setPastRank(ranks);
      } catch (error) {
        console.error("Error fetching rank: ", error);
      } finally {
        setLoading(false);
      }
    };

    const fetchPastEventPointsForGraph = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getPastEventPointsForGraph();
        setPastEventPoints(response.data);
      } catch (error) {
        console.error("Error fetching past events points for graph: ", error);
        setError("Failed to load past events points for graph");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
    fetchInternationalRanking();
    fetchUpcomingEvents();
    fetchPastEvents();
    fetchPastRank();
    fetchPastEventPointsForGraph();
  }, []);

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return formattedDate;
  };

  const handleEditClick = () => {
    setIsEditing(!isEditing); // Toggle between view and edit mode
  };

  const isValidDebutYear = (year, dateOfBirth) => {
    if (!dateOfBirth) return false;
    const birthYear = new Date(dateOfBirth).getFullYear();
    const minDebutYear = birthYear + 8;
    const currentYear = new Date().getFullYear();
    return year >= minDebutYear && year <= currentYear;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSave = async () => {
    const { debutYear } = editedData;
    const dateOfBirth = userData.dateOfBirth;

    if (!isValidDebutYear(Number(debutYear), dateOfBirth)) {
      setIsInputValid(false);
      console.error(
        `Invalid input. Minimum debut year is ${new Date(dateOfBirth).getFullYear() + 8
        }.`
      );
      return;
    }

    try {
      await FencerService.completeProfile(editedData);
      setUserData((prevData) => ({ ...prevData, ...editedData }));
      setIsEditing(false);
      setIsInputValid(true);
    } catch (error) {
      console.error("Error saving profile:", error);
    }
  };

  const cancelCompleteProfile = () => {
    setIsEditing(false);
    setIsInputValid(true);
    setEditedData({});
  };

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  // Data and options for the Event Points per Event graph
  const getPastSevenEvents = () => {
    let pastSevenEvents = [];
    // Check only if past events are not null and length is greater than 0
    if (pastEvents && pastEvents.length > 0) {
      const lastSevenEvents = pastEvents.slice(-7);
      lastSevenEvents.forEach((event, index) => {
        let name = `${event.tournamentName} (${event.gender} ${event.weapon})`;
        pastSevenEvents[index] = name;
      });
    }
    return pastSevenEvents;
  };

  const getPastSevenEventsPoints = () => {
    // return [65, 59, 80, 81, 56, 55, 40];
    let pastSevenEventPoints = [];
    if (pastEventPoints && pastEventPoints.length > 0) {
      const lastSevenPoints = pastEventPoints.slice(-7);
      lastSevenPoints.forEach((event, index) => {
        pastSevenEventPoints[index] = event.pointsAfterEvent;
      });
    }
    return pastSevenEventPoints;
  };

  const rankGraphData = {
    labels: getPastSevenEvents(),
    datasets: [
      {
        label: "Points After Event",
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
        data: getPastSevenEventsPoints(),
      },
    ],
  };

  const rankGraphOptions = {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };


  // Data and options for the #Tournament per month graph
  const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
  const getMostRecentSevenMonths = () => {

    const currentDate = new Date();
    const months = [];

    for (let i = 6; i >= 0; i--) {
      const monthIndex = (currentDate.getMonth() - i + 12) % 12;
      const year = currentDate.getFullYear() - Math.floor((i - currentDate.getMonth() + 11) / 12);
      months.push(`${monthNames[monthIndex]} ${year}`);
    }

    return months;
  };

  const getParticaptedTournamentsPerMonth = () => {

    let months = getMostRecentSevenMonths();
    // Initialise an array of all 0s
    const eventCounts = new Array(months.length).fill(0);

    // Iterate over the events and count the number of events for each month
    if (pastEvents && pastEvents.length > 0) {
      for (let i = 0; i < pastEvents.length; i++) {
        // Format pastEvent eventDate to be compared to the values in months[]
        const pastEventDate = new Date(pastEvents[i].eventDate);
        const pastEventMonth = `${monthNames[pastEventDate.getMonth() - 1]} ${pastEventDate.getFullYear()}`;

        // Comparing formatted month with values in months[]
        for (let i = 0; i < months.length; i++) {
          if (pastEventMonth === months[i]) {
            eventCounts[i]++;
          }
        }
      }
    }

    return eventCounts;
  }

  const pointsGraphData = {
    labels: getMostRecentSevenMonths(),
    datasets: [
      {
        label: "Tournament Participations",
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
        data: getParticaptedTournamentsPerMonth(),
      },
    ],
  };

  const pointsGraphOptions = {
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          stepSize: 1
        }
      },
    },
  };


  const getUserRank = () => {
    if (!rankingData || !Array.isArray(rankingData)) {
      return "Ranking data not available";
    }

    const sortedRankingData = rankingData.sort((a, b) => b.points - a.points);
    const userIndex = sortedRankingData.findIndex(
      (rank) => rank.id === userData.id
    );
    return userIndex !== -1 ? userIndex + 1 : "User rank not found";
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

  return (
    <div className="bg-white w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-col w-full relative overflow-x-hidden">
        <div className="w-full flex-shrink-0 flex flex-col my-4 ml-4">
          <div className="text-4xl font-semibold">
            Welcome back, {userData.name}
          </div>
        </div>

        <div className="grid grid-cols-[2fr_8fr] gap-y-2 gap-x-4 ml-4 my-4 text-xl w-full">
          {/* Email, Birth Date, Gender, Category, Hand, Year, Org, Country */}
          {/* <div className="text-4xl font-semibold mr-4">{userData.name}'s</div><div></div> */}
          <div className="flex font-medium">Email:</div>
          <div className="flex">{userData.email}</div>
          <div className="flex font-medium">Birth Date:</div>
          <div className="flex">{formatDate(userData.dateOfBirth)}</div>
          <div className="flex font-medium">Gender:</div>
          <div className="flex">
            {isEditing ? (
              <select
                name="gender"
                value={editedData.gender}
                onChange={handleInputChange}
                className="border p-1 rounded-lg"
              >
                <option value="" disabled>
                  - {/* Default placeholder */}
                </option>
                <option value="M">Male</option>
                <option value="F">Female</option>
              </select>
            ) : userData.gender === "M" ? (
              "Male"
            ) : userData.gender === "F" ? (
              "Female"
            ) : (
              "-"
            )}
          </div>
          <hr className="col-span-2 my-4 border-gray-300 w-full" />
          <div className="flex font-medium">Category:</div>
          <div className="flex">
            {isEditing ? (
              <select
                name="weapon"
                value={editedData.weapon}
                onChange={handleInputChange}
                className="border p-1 rounded-lg"
              >
                <option value="" disabled>
                  - {/* Default placeholder */}
                </option>
                <option value="F">Foil</option>
                <option value="E">Épée</option>
                <option value="S">Sabre</option>
              </select>
            ) : userData.weapon === "F" ? (
              "Foil"
            ) : userData.weapon === "E" ? (
              "Épée"
            ) : userData.weapon === "S" ? (
              "Sabre"
            ) : (
              "-"
            )}
          </div>
          <div className="flex font-medium">Dominant Arm:</div>
          <div className="flex">
            {isEditing ? (
              <select
                name="dominantArm"
                value={editedData.dominantArm}
                onChange={handleInputChange}
                className="border p-1 rounded-lg"
              >
                <option value="" disabled>
                  - {/* Default placeholder */}
                </option>
                <option value="R">Right</option>
                <option value="L">Left</option>
              </select>
            ) : userData.dominantArm === "R" ? (
              "Right"
            ) : userData.dominantArm === "L" ? (
              "Left"
            ) : (
              "-"
            )}
          </div>
          <div className="flex font-medium">Debut Year:</div>
          <div className="flex">
            {isEditing ? (
              <input
                type="number"
                name="debutYear"
                value={editedData.debutYear}
                onChange={handleInputChange}
                className="border p-1 rounded-lg"
                placeholder="-"
              />
            ) : userData.debutYear ? (
              userData.debutYear
            ) : (
              "-"
            )}
            {!isInputValid && (
              <span className="ml-8 text-red-500 italic">
                {`Invalid input. Minimum debut year is ${new Date(userData.dateOfBirth).getFullYear() + 8
                  }.`}
              </span>
            )}
          </div>
          <div className="flex font-medium">Club:</div>
          <div className="flex">
            {isEditing ? (
              <input
                name="club"
                type="text"
                value={editedData.club}
                onChange={handleInputChange}
                className="border border-gray px-2 py-1 w-180 rounded-lg"
                placeholder="-"
              />
            ) : userData.club ? (
              userData.club
            ) : (
              "-"
            )}
          </div>
          <div className="flex font-medium">Country:</div>
          <div className="flex">{userData.country}</div>
          {isEditing && (
            <div>
              <button
                onClick={handleSave}
                className="bg-green-400 text-white mt-2 px-2 py-1 mr-4 rounded"
              >
                Confirm Changes
              </button>
              <button
                onClick={cancelCompleteProfile}
                className="bg-red-400 text-white mt-2 px-2 py-1 rounded"
              >
                Cancel Changes
              </button>
            </div>
          )}
        </div>

        {/* Edit Icon */}
        <div
          className="absolute top-4 right-4 cursor-pointer text-gray-600"
          onClick={handleEditClick}
        >
          <img src={editLogo} alt="Edit profile button" className="w-6 h-6" />
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-row flex-grow w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="Ranking & Statistics">
            <div className="grid grid-cols-[2fr_3fr_3fr]">
              <div className="grid grid-cols-[2fr_1fr] gap-y-4 ml-4 my-4 text-xl w-75">
                <div className="flex font-medium">International Rank</div>
                <div className="flex">{getUserRank()}</div>
                <div className="flex font-medium">Total Points</div>
                <div className="flex">
                  {userData.points ? userData.points : "-"}
                </div>
                <div className="flex font-medium">
                  Tournament Participations
                </div>
                {/* placeholder stuff */}
                <div className="flex">{pastEvents.length}</div>
              </div>

              <div className="w-[99%] h-full">
                <LineGraph
                  data={rankGraphData}
                  options={rankGraphOptions}
                  height={200}
                />
              </div>
              <div className="w-[99%] h-full">
                <LineGraph
                  data={pointsGraphData}
                  options={pointsGraphOptions}
                  height={200}
                />
              </div>
            </div>
          </Tab>
          <Tab label="Past Tournaments">
            <div className="py-4">
              {pastEvents.length > 0 ? (
                <table className="table text-lg border-collapse">
                  {/* head */}
                  <thead className="text-lg text-primary">
                    <tr className="border-b border-gray-300">
                      <th className="w-20"></th>
                      <th className="w-1/4">Tournament Name</th>
                      <th className="text-center">Date</th>
                      <th className="text-center">Rank</th>
                    </tr>
                  </thead>
                  <tbody>
                    {pastEvents.map((item, index) => (
                      <tr
                        key={item.id}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td className="text-center">{index + 1}</td>
                        <td className="underline hover:text-primary">
                          {item.tournamentName}
                        </td>
                        <td className="text-center">{formatDate(item.eventDate)}</td>
                        <td className="text-center">
                          {pastRank && pastRank[index]
                            ? pastRank[index].rank
                            : "-"}
                        </td>
                      </tr>
                    ))
                    }
                  </tbody>
                </table>) :
                (
                  <div className="flex justify-center items-center h-full">
                    <h2 className="text-lg font-medium">
                      No past tournaments available yet
                    </h2>
                  </div>
                )}
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments">
            <div className="py-4">
              {upcomingEvents.length > 0 ? (
                <table className="table text-lg border-collapse">
                  {/* head */}
                  <thead className="text-lg text-primary">
                    <tr className="border-b border-gray-300">
                      <th className="w-20"></th>
                      <th className="w-1/4">Tournament Name</th>
                      <th className="text-center">Date</th>
                      <th className="text-center">Start Time</th>
                      <th className="text-center">End Time</th>
                    </tr>
                  </thead>
                  <tbody>
                    {upcomingEvents.map((item, index) => (
                      <tr
                        key={item.id}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td className="text-center">{index + 1}</td>
                        <td>
                          <Link
                            to={`/tournaments/${item.id}`}
                            className="underline hover:text-primary"
                          >
                            {item.tournamentName}
                          </Link>
                        </td>
                        <td className="text-center">{formatDate(item.eventDate)}</td>
                        <td className="text-center">
                          {formatTimeTo24Hour(item.startTime)}
                        </td>
                        <td className="text-center">
                          {formatTimeTo24Hour(item.endTime)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>) :
                (
                  <div className="flex justify-center items-center h-full">
                    <h2 className="text-lg font-medium">
                      No upcoming tournaments available yet
                    </h2>
                  </div>
                )}
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default FencerDashboard;
