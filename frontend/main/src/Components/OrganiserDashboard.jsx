import React, { useEffect, useState } from "react";
import OrganiserService from "../Services/Organiser/OrganiserService";
import UpdateTournament from "./Tournament/UpdateTournament";
import DeleteTournament from "./Tournament/DeleteTournament";
import DropdownMenu from "./Others/DropdownMenu";
import { Tabs, Tab } from "./Others/Tabs";
import { Link } from "react-router-dom";
import editIcon from "../Assets/edit.png";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [upcomingTournaments, setUpcomingTournaments] = useState([]);
  const [pastTournaments, setPastTournaments] = useState([]);
  const [ongoingTournaments, setOngoingTournaments] = useState([]);
  // Selected torunament to update/delete
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [isUpdateTournamentPopupVisible, setIsUpdateTournamentPopupVisible] =
    useState(false);
  const [isDeleteTournamentPopupVisible, setIsDeleteTournamentPopupVisible] =
    useState(false);

  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const response = await OrganiserService.getProfile();
        setUserData(response.data);
      } catch (error) {
        console.error("Error fetching user data:", error);
        setError("Failed to load user data.");
      } finally {
        setLoading(false);
      }
    };

    const fetchTournamentData = async () => {
      setLoading(true);
      try {
        const response = await OrganiserService.getAllHostedTournaments();
        const tournaments = response.data;

        const currentDate = new Date();
        const ongoingTournamentsData = tournaments
          .filter((tournament) => {
            const startDate = new Date(tournament.startDate);
            const endDate = new Date(tournament.endDate);
            return startDate <= currentDate && currentDate <= endDate;
          })
          .map((tournament) => {
            const totalParticipants = tournament.events.reduce((sum, event) => {
              return sum + (event.fencers ? event.fencers.length : 0);
            }, 0);
            return { ...tournament, totalParticipants };
          });
        setOngoingTournaments(ongoingTournamentsData);
      } catch (error) {
        console.error("Error fetching data:", error);
        setError("Failed to load data.");
      } finally {
        setLoading(false);
      }
    };

    const fetchUpcomingTournaments = async () => {
      setLoading(true);
      try {
        const response =
          await OrganiserService.getOrganiserUpcomingTournaments();
        setUpcomingTournaments(response.data);
      } catch (error) {
        console.error("Error fetching upcoming tournaments: ", error);
        setError("Failed to fetch upcoming tournaments");
      } finally {
        setLoading(false);
      }
    };

    const fetchPastTournaments = async () => {
      setLoading(true);
      try {
        const response = await OrganiserService.getOrganiserPastTournaments();
        setPastTournaments(response.data);
      } catch (error) {
        console.error("Error fetching past tournaments: ", error);
        setError("Failed to fetch past tournaments");
      } finally {
        setLoading(false);
      }
    };

    fetchTournamentData();
    fetchData();
    fetchUpcomingTournaments();
    fetchPastTournaments();
  }, []);

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

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  // console.log("verified=" + userData.verified);

  const updateTournament = (tournamentToUpdate) => {
    setIsUpdateTournamentPopupVisible(true);
    setSelectedTournament(tournamentToUpdate);
  };

  const closeUpdateTournamentPopup = () => {
    setIsUpdateTournamentPopupVisible(false);
    setSelectedTournament(null);
  };

  const deleteTournament = (tournamentToDelete) => {
    setIsDeleteTournamentPopupVisible(true);
    setSelectedTournament(tournamentToDelete);
  };

  const closeDeleteTournamentPopup = () => {
    setIsDeleteTournamentPopupVisible(false);
    setSelectedTournament(null);
  };

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return formattedDate;
  };

  console.log(upcomingTournaments);

  return (
    <div className="bg-white w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative overflow-x-hidden">
        <div className="w-full flex-shrink-0 flex flex-col my-4 ml-4">
          <div className="text-4xl font-semibold">
            Welcome back, {userData.name}
          </div>

          <div className="grid grid-cols-[2fr_8fr] gap-y-2 gap-x-4 my-4 text-xl w-full">
            {/* Email, ContactNo, Country, Verification Status */}
            <div className="flex font-medium">Email:</div>
            <div className="flex">{userData.email}</div>
            <div className="flex font-medium">Contact Number:</div>
            <div className="flex">{userData.contactNo}</div>
            <div className="flex font-medium">Country:</div>
            <div className="flex">{userData.country}</div>
            <div className="flex font-medium">Verification Status:</div>
            <div className="flex">
              {userData.verified ? "Verified" : "Pending Verification"}
            </div>
          </div>
        </div>

        {/* Edit Icon */}
        <div className="absolute top-4 right-4 cursor-pointer text-gray-600">
          <img src={editIcon} alt="Edit profile icon" className="w-6 h-6" />
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-col flex-grow w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="Ongoing Tournaments Hosted">
            <div className="py-4">
              <table className="table text-lg border-collapse">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr className="border-b border-gray-300">
                    <th className="w-20"></th>
                    <th className="w-1/4">Tournament Name</th>
                    <th className="text-center">Location</th>
                    <th className="text-center">Dates</th>
                    <th className="text-center">Total participants</th>
                  </tr>
                </thead>
                <tbody>
                  {ongoingTournaments && ongoingTournaments.length > 0 ? (
                    ongoingTournaments.map((item, index) => (
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
                            {item.name}
                          </Link>
                        </td>
                        <td className="text-center">{item.location}</td>
                        <td className="text-center">
                          {formatDateRange(item.startDate, item.endDate)}
                        </td>
                        <td className="text-center">
                          {item.totalParticipants}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan="5"
                        className="text-center border-b border-gray-300"
                      >
                        No ongoing tournaments available.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments Hosted">
            <div className="py-4">
              <table className="table text-lg border-collapse">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr className="border-b border-gray-300">
                    <th className="w-20"></th>
                    <th className="w-1/4">Tournament Name</th>
                    <th className="text-center">Location</th>
                    <th className="text-center">Dates</th>
                    <th className="text-center"></th>
                  </tr>
                </thead>
                <tbody>
                  {upcomingTournaments && upcomingTournaments.length > 0 ? (
                    upcomingTournaments.map((item, index) => (
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
                            {item.name}
                          </Link>
                        </td>
                        <td className="text-center">{item.location}</td>
                        <td className="text-center">
                          {formatDateRange(item.startDate, item.endDate)}
                        </td>
                        <td>
                          <DropdownMenu
                            entity="Tournament"
                            updateEntity={() => updateTournament(item)}
                            deleteEntity={() => deleteTournament(item)}
                          />
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan="4"
                        className="text-center border-b border-gray-300"
                      >
                        No upcoming tournaments available.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
            {isUpdateTournamentPopupVisible && (
              <UpdateTournament
                onClose={closeUpdateTournamentPopup}
                selectedTournament={selectedTournament}
              />
            )}
            {isDeleteTournamentPopupVisible && (
              <DeleteTournament
                closeDeletePopUp={closeDeleteTournamentPopup}
                id={selectedTournament.id}
              />
            )}
          </Tab>
          <Tab label="Past Tournaments Hosted">
            <div className="py-4">
              <table className="table text-lg border-collapse">
                {/* head */}
                <thead className="text-lg text-primary">
                  <tr className="border-b border-gray-300">
                    <th className="w-20"></th>
                    <th className="w-1/4">Tournament Name</th>
                    <th className="text-center">Location</th>
                    <th className="text-center">Dates</th>
                  </tr>
                </thead>
                <tbody>
                  {pastTournaments && pastTournaments.length > 0 ? (
                    pastTournaments.map((item, index) => (
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
                            {item.name}
                          </Link>
                        </td>
                        <td className="text-center">{item.location}</td>
                        <td className="text-center">
                          {formatDateRange(item.startDate, item.endDate)}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td
                        colSpan="4"
                        className="text-center border-b border-gray-300"
                      >
                        No past tournaments available.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default OrganiserDashboard;
