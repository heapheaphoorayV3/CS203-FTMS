import React, { useEffect, useLayoutEffect, useState } from "react";
import jackinpic from "../Assets/jackinpic.jpg";
import editLogo from "../Assets/edit.png";
import FencerService from "../Services/Fencer/FencerService";
import { Tabs, Tab } from "./Others/Tabs";
import LineGraph from "./Others/LineGraph";

const FencerDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [rankingData, setRankingData] = useState(null);
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

    fetchData();
    fetchInternationalRanking();
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
        `Invalid input. Minimum debut year is ${
          new Date(dateOfBirth).getFullYear() + 8
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

  // Data and options for the Rank Graph
  const rankGraphData = {
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
      {
        label: "International Rank",
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
        data: [65, 59, 80, 81, 56, 55, 40],
      },
    ],
  };

  const rankGraphOptions = {
    scales: {
      y: {
        beginAtZero: true,
        reverse: true,
      },
    },
  };

  const pointsGraphData = {
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
      {
        label: "Total Points",
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
        data: [65, 59, 80, 81, 56, 55, 40],
      },
    ],
  };

  const pointsGraphOptions = {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  };

  const getUserRank = () => {
    if (!rankingData || !Array.isArray(rankingData)) {
      return "Ranking data not available";
    }
    
    const sortedRankingData = rankingData.sort((a, b) => b.points - a.points);
    const userIndex = sortedRankingData.findIndex((rank) => rank.id === userData.id);
    return userIndex !== -1 ? userIndex + 1 : "User rank not found";
  };

  console.log(userData);

  return (
    <div className="bg-white w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative overflow-x-hidden">
        <div className="w-1/4 flex-shrink-0 flex flex-col items-center my-auto">
          <div className="text-4xl font-semibold mr-4">{userData.name}'s</div>
          <div className="text-4xl font-semibold mr-4">Dashboard</div>
        </div>

        <div className="grid grid-cols-[2fr_8fr] gap-y-2 gap-x-4 ml-4 my-4 text-xl w-full">
          {/* Email, Birth Date, Gender, Category, Hand, Year, Org, Country */}
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
                {`Invalid input. Minimum debut year is ${
                  new Date(userData.dateOfBirth).getFullYear() + 8
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
                <div className="flex">
                  {userData.tournaments ? userData.tournaments : "-"}
                </div>
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
              <h2 className="text-lg font-medium mb-2">Tab 2</h2>
              <p className="text-gray-700">
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime
                mollitia, molestiae quas vel sint commodi repudiandae
                consequuntur voluptatum laborum numquam blanditiis harum
                quisquam eius sed odit fugiat iusto fuga praesentium optio,
                eaque rerum! Provident similique accusantium nemo autem.
                Veritatis obcaecati tenetur iure eius earum ut molestias
                architecto voluptate aliquam nihil, eveniet aliquid culpa
                officia aut! Impedit sit sunt quaerat, odit, tenetur error,
                harum nesciunt ipsum debitis quas aliquid. Reprehenderit, quia.
                Quo neque error repudiandae fuga? Ipsa laudantium molestias eos
                sapiente officiis modi at sunt excepturi expedita sint? Sed
                quibusdam recusandae alias error harum maxime adipisci amet
                laborum.
              </p>
            </div>
          </Tab>
          <Tab label="Upcoming Tournaments">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Tab 3</h2>
              <p className="text-gray-700">
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime
                mollitia, molestiae quas vel sint commodi repudiandae
                consequuntur voluptatum laborum numquam blanditiis harum
                quisquam eius sed odit fugiat iusto fuga praesentium optio,
                eaque rerum! Provident similique accusantium nemo autem.
                Veritatis obcaecati tenetur iure eius earum ut molestias
                architecto voluptate aliquam nihil, eveniet aliquid culpa
                officia aut! Impedit sit sunt quaerat, odit, tenetur error,
                harum nesciunt ipsum debitis quas aliquid. Reprehenderit, quia.
                Quo neque error repudiandae fuga? Ipsa laudantium molestias eos
                sapiente officiis modi at sunt excepturi expedita sint? Sed
                quibusdam recusandae alias error harum maxime adipisci amet
                laborum.
              </p>
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
};

export default FencerDashboard;
