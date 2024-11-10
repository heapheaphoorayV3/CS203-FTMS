import React, { useEffect, useState } from "react";
import OrganiserService from "../Services/Organiser/OrganiserService";
import UpdateTournament from "./Tournament/UpdateTournament";
import DeleteTournament from "./Tournament/DeleteTournament";
import DropdownMenu from "./Others/DropdownMenu";
import { Tabs, Tab } from "./Others/Tabs";
import editIcon from "../Assets/edit.png";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [upcomingTournaments, setUpcomingTournaments] = useState([]);
  const [pastTournaments, setPastTournaments] = useState([]);
  const [tournamentData, setTournamentData] = useState(null);
  // Selected torunament to update/delete
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [isUpdateTournamentPopupVisible, setIsUpdateTournamentPopupVisible] =
    useState(false);
  const [isDeleteTournamentPopupVisible, setIsDeleteTournamentPopupVisible] =
    useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        console.log("Fetching user data...");
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
      try {
        console.log("Fetching table data...");
        const response = await OrganiserService.getAllHostedTournaments();
        setTournamentData(response.data);
      } catch (error) {
        console.error("Error fetching table data:", error);
        setError("Failed to load table data.");
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

  console.log(tournamentData);

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
                  {tournamentData && tournamentData.length > 0 ? (
                    tournamentData.map((item, index) => (
                      <tr
                        key={item.id}
                        className="border-b border-gray-300 hover:bg-gray-100"
                      >
                        <td className="text-center">{index + 1}</td>
                        <td className="underline hover:text-primary">
                          {item.name}
                        </td>
                        <td className="text-center">{item.location}</td>
                        <td className="text-center">{formatDateRange(item.startDate, item.endDate)}</td>
                        {/* <td className="text-center">{item.events.fencers.length}</td> */}
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
              {/* {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={tableHead}
                  tableRows={tournamentData
                    .filter((tournament) => {
                      const currentDate = new Date();
                      const startDate = new Date(tournament.startDate);
                      const endDate = new Date(tournament.endDate);
                      return currentDate >= startDate && currentDate <= endDate;
                    })
                    .map((tournament) => [
                      <a
                        href={`tournaments/${tournament.id}`}
                        className="underline hover:text-accent"
                      >
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                    ])}
                />
              ) : (
                console.log("No ongoing tournaments")
              )} */}
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments Hosted">
            <div className="h-full px-4 pt-4">
              {/* {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={upcomingTableHead}
                  tableRows={tournamentData
                    .filter((tournament) => {
                      const currentDate = new Date();
                      const startDate = new Date(tournament.startDate);
                      return currentDate <= startDate;
                    })
                    .map((tournament) => [
                      <a
                        href={`tournaments/${tournament.id}`}
                        className="underline hover:text-accent"
                      >
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                      <DropdownMenu
                        entity="Tournament"
                        updateEntity={() => updateTournament(tournament)}
                        deleteEntity={() => deleteTournament(tournament)}
                      />,
                    ])}
                />
              ) : (
                console.log("No upcoming tournaments")
              )} */}
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
            <div className="h-full px-4 pt-4">
              {/* {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={tableHead}
                  tableRows={tournamentData
                    .filter((tournament) => {
                      const currentDate = new Date();
                      const endDate = new Date(tournament.endDate);
                      return currentDate >= endDate;
                    })
                    .map((tournament) => [
                      <a
                        href={`tournaments/${tournament.id}`}
                        className="underline hover:text-accent"
                      >
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                    ])}
                />
              ) : (
                console.log("No past tournaments")
              )} */}
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default OrganiserDashboard;
