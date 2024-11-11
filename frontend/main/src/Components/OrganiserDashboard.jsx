import React, { useEffect, useState } from "react";
import OrganiserService from "../Services/Organiser/OrganiserService";
import UpdateTournament from "./Tournament/UpdateTournament";
import DeleteTournament from "./Tournament/DeleteTournament";
import DropdownMenu from "./Others/DropdownMenu";
import { Tabs, Tab } from "./Others/Tabs";
import { Link } from "react-router-dom";
import editIcon from "../Assets/edit.png";
import validator from "validator";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [upcomingTournaments, setUpcomingTournaments] = useState([]);
  const [pastTournaments, setPastTournaments] = useState([]);
  const [ongoingTournaments, setOngoingTournaments] = useState([]);
  // Selected torunament to update/delete
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [isUpdateTournamentPopupVisible, setIsUpdateTournamentPopupVisible] = useState(false);
  const [isDeleteTournamentPopupVisible, setIsDeleteTournamentPopupVisible] = useState(false);
  const [contactNoErrors, setContactNoErrors] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const initialEditedData = () => ({
    name: userData.name,
    email: userData.email,
    contactNo: userData.contactNo,
    country: userData.country,
  });
  const [editedData, setEditedData] = useState(initialEditedData);

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

        const sortedTournaments = response.data.sort((a, b) => {
          const dateA = new Date(a.startDate);
          const dateB = new Date(b.startDate);
          return dateA - dateB;
        });
        setUpcomingTournaments(sortedTournaments);
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
        const sortedTournaments = response.data.sort((a, b) => {
          const dateA = new Date(a.startDate);
          const dateB = new Date(b.startDate);
          return dateA - dateB;
        });
        setPastTournaments(sortedTournaments);
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

  const handleEditClick = () => {
    setIsEditing(!isEditing); // Toggle between view and edit mode
    if (!isEditing) {
      setEditedData(initialEditedData); // Reset edited data
    }
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    console.log(name, value);
    setEditedData((prevData) => ({ ...prevData, [name]: value }));
    console.log("Edited data:", editedData);
  };

  const validateEditInputs = () => {
    const newErrors = {};
    if (!validator.isMobilePhone(editedData.contactNo, 'any', { strictMode: true })) {
      newErrors.contactNo = "Please enter a valid phone number with country code!";
    }
    setContactNoErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleEditSubmit = async () => {
    console.log("Edited data:", editedData);
    if (validateEditInputs()) {
      try {
        await OrganiserService.updateProfile(editedData);
        setUserData((prevData) => ({ ...prevData, ...editedData }));
        setIsEditing(false);
      } catch (error) {
        console.error("Error saving profile:", error);
      }
    }
  };

  const cancelEditProfile = () => {
    setEditedData(initialEditedData); // Reset
    setIsEditing(false);
    setContactNoErrors({});
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
            <div className="flex">{isEditing ? (
            <input
              name="contactNo"
              type="text"
              value={editedData.contactNo}
              onChange={handleEditChange}
              className={`border p-1 rounded-lg ${contactNoErrors.contactNo ? 'border-red-500' : ''}`}
              placeholder="Contact Number (e.g. +65********)"
            />
          ) : (
            userData.contactNo
          )}
          {contactNoErrors.contactNo && (
            <div className="text-red-500 text-sm ml-4">
              {contactNoErrors.contactNo}
            </div>
          )}
          </div>
            <div className="flex font-medium">Country:</div>
            <div className="flex">{userData.country}</div>
            <div className="flex font-medium">Verification Status:</div>
            <div className="flex">
              {userData.verified ? "Verified" : "Pending Verification"}
            </div>
            {isEditing && (
            <div>
              <button
                onClick={handleEditSubmit}
                className="bg-green-400 text-white mt-2 px-2 py-1 mr-4 rounded"
              >
                Confirm Changes
              </button>
              <button
                onClick={cancelEditProfile}
                className="bg-red-400 text-white mt-2 px-2 py-1 rounded"
              >
                Cancel Changes
              </button>
            </div>
          )}
          </div>
        </div>

        {/* Edit Icon */}
        <div
          className="absolute top-4 right-4 cursor-pointer text-gray-600"
          onClick={handleEditClick}
        >
          <img src={editIcon} alt="Edit profile button" className="w-6 h-6" />
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-col flex-grow w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="Ongoing Tournaments Hosted">
            <div className="py-4">
              {ongoingTournaments.length > 0 ? (
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
                    {ongoingTournaments.map((item, index) => (
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
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No ongoing tournaments available yet
                  </h2>
                </div>
              )}
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments Hosted">
            <div className="py-4">
              {upcomingTournaments.length > 0 ? (
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
                    {upcomingTournaments.map((item, index) => (
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
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No upcoming tournaments available yet
                  </h2>
                </div>
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
            <div className="py-4">
              {pastTournaments.length > 0 ? (
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
                    {pastTournaments.map((item, index) => (
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
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="flex justify-center items-center h-full">
                  <h2 className="text-lg font-medium">
                    No past tournaments available yet
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

export default OrganiserDashboard;
