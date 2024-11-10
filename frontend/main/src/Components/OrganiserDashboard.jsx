import React, { useEffect, useState } from "react";
import OrganiserService from "../Services/Organiser/OrganiserService";
import UpdateTournament from "./Tournament/UpdateTournament";
import DeleteTournament from "./Tournament/DeleteTournament";
import DropdownMenu from "./Others/DropdownMenu";
import { Tabs, Tab } from "./Others/Tabs";
import Table from "./Others/Table";
import editIcon from "../Assets/edit.png";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [tableLoading, setTableLoading] = useState(true);
  const [tableError, setTableError] = useState(null);
  const [tournamentData, setTournamentData] = useState(null);
  // Selected torunament to update/delete
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [isUpdateTournamentPopupVisible, setIsUpdateTournamentPopupVisible] = useState(false);
  const [isDeleteTournamentPopupVisible, setIsDeleteTournamentPopupVisible] = useState(false);
  const [tableHead, setTableHead] = useState(["Tournament Name", "Location", "Dates", "Total Participants"]);
  const [upcomingTableHead, setUpcomingTableHead] = useState(["Tournament Name", "Location", "Dates", "Total Participants", ""]);

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
    fetchData();
  }, []);

  useEffect(() => {
    const fetchTournamentData = async () => {
      try {
        console.log("Fetching table data...");
        const response = await OrganiserService.getAllHostedTournaments();
        setTournamentData(response.data);
      } catch (tableError) {
        console.error("Error fetching table data:", tableError);
        setTableError("Failed to load table data.");
      } finally {
        setTableLoading(false);
      }
    };
    fetchTournamentData();
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

  console.log("verified=" + userData.verified);

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
  }

  const closeDeleteTournamentPopup = () => {
    setIsDeleteTournamentPopupVisible(false);
    setSelectedTournament(null);
  }

  return (
    <div className="bg-white w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative overflow-x-hidden">

        <div className="w-1/4 flex-shrink-0 flex flex-col items-center my-auto">
          <div className="text-4xl font-semibold mr-4">{userData.name}'s</div>
          <div className="text-4xl font-semibold mr-4">Dashboard</div>
        </div>

        <div className="grid grid-cols-[2fr_8fr] gap-y-2 gap-x-4 ml-4 my-4 text-xl w-full">
          {/* Email, ContactNo, Country, Verification Status */}
          <div className="flex font-medium">Email:</div>
          <div className="flex">{userData.email}</div>
          <div className="flex font-medium">Contact Number:</div>
          <div className="flex">{userData.contactNo}</div>
          <div className="flex font-medium">Country:</div>
          <div className="flex">{userData.country}</div>
          <div className="flex font-medium">Verification Status:</div>
          <div className="flex">{userData.verified ? "Verified" : "Pending Verification"}</div>
        </div>

        {/* Edit Icon */}
        <div className="absolute top-4 right-4 cursor-pointer text-gray-600">
          <img src={editIcon} alt="Edit profile icon" className="w-6 h-6" />
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-col flex-grow w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="Ongoing Tournaments Hosted">
            <div className="h-full px-4 pt-4">
              {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={tableHead}
                  tableRows={tournamentData
                    .filter(tournament => {
                      const currentDate = new Date();
                      const startDate = new Date(tournament.startDate);
                      const endDate = new Date(tournament.endDate);
                      return currentDate >= startDate && currentDate <= endDate;
                    })
                    .map(tournament => [
                      <a href={`tournaments/${tournament.id}`} className="underline hover:text-accent">
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                    ])}
                />
              ) : (
                console.log("No ongoing tournaments")
              )}
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments Hosted">
            <div className="h-full px-4 pt-4">
              {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={upcomingTableHead}
                  tableRows={tournamentData
                    .filter(tournament => {
                      const currentDate = new Date();
                      const startDate = new Date(tournament.startDate);
                      return currentDate <= startDate;
                    })
                    .map(tournament => [
                      <a href={`tournaments/${tournament.id}`} className="underline hover:text-accent">
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                      <DropdownMenu
                        entity="Tournament"
                        updateEntity={() => updateTournament(tournament)}
                        deleteEntity={() => deleteTournament(tournament)}
                      />
                    ])}
                />
              ) : (
                console.log("No upcoming tournaments")
              )}
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
              {tournamentData && tournamentData.length > 0 ? (
                <Table
                  tableHead={tableHead}
                  tableRows={tournamentData
                    .filter(tournament => {
                      const currentDate = new Date();
                      const endDate = new Date(tournament.endDate);
                      return currentDate >= endDate;
                    })
                    .map(tournament => [
                      <a href={`tournaments/${tournament.id}`} className="underline hover:text-accent">
                        {tournament.name}
                      </a>,
                      tournament.location,
                      formatDateRange(tournament.startDate, tournament.endDate),
                      10,
                    ])}
                />
              ) : (
                console.log("No past tournaments")
              )}
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default OrganiserDashboard;
