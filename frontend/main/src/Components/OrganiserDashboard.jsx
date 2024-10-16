import React, { useEffect, useState } from "react";
import jackinpic from "../Assets/jackinpic.jpg";
import OrganiserService from "../Services/Organiser/OrganiserService";
import TournamentService from "../Services/Tournament/TournamentService";
import { Tabs, Tab } from "./Others/DashboardTabs";
import Table from "./Others/Table";
import { set } from "react-hook-form";
import editIcon from "../Assets/edit.png";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [tableLoading, setTableLoading] = useState(true);
  const [tableError, setTableError] = useState(null);
  const [tournamentData, setTournamentData] = useState(null);
  const [tableHead, setTableHead] = useState(["Tournament Name", "Location", "Dates", "Total Participants"]);

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

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  console.log("verified=" + userData.verified);

  return (
    <div className="bg-gray-200 w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative overflow-x-hidden">
        {/* Profile Image and Name */}
        <div className="flex-shrink-0 flex flex-col items-center my-auto">
          <img
            className="w-56 h-56 rounded-full mr-4"
            src={jackinpic}
            alt="Profile picture"
          />
          <div className="text-xl font-semibold mt-4 mr-4">
            {userData.name}
          </div>
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
          <img src={editIcon} alt="Edit profile icon" className="w-6 h-6"/>
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
                      `${new Date(tournament.startDate).toLocaleDateString()} - ${new Date(tournament.endDate).toLocaleDateString()}`,
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
                  tableHead={tableHead}
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
                      `${new Date(tournament.startDate).toLocaleDateString()} - ${new Date(tournament.endDate).toLocaleDateString()}`,
                      10,
                    ])}
                />
              ) : (
                console.log("No upcoming tournaments")
              )}
            </div>
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
                      `${new Date(tournament.startDate).toLocaleDateString()} - ${new Date(tournament.endDate).toLocaleDateString()}`,
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
