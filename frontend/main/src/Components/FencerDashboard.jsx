import React, { useEffect, useLayoutEffect, useState } from "react";
import Navbar from "./Navbar";
import Sidebar from "./Sidebar";
import jackinpic from "../Assets/jackinpic.jpg";
import FencerService from "../Services/Fencer/FencerService";
import { Tabs, Tab } from "./Others/DashboardTabs";
import LineGraph from "./Others/LineGraph";

const FencerDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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
    fetchData();
  }, []);


  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString('en-GB', {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
    });
    return formattedDate;
  };

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  // Data and options for the Rank Graph
  const rankGraphData = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'National Rank',
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
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

  // Data and options for the Points Graph
  const pointsGraphData = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'Total Points',
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
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
  }

  return (
    <div className="main">
      <div id="body" className="bg-gray-200 w-full gap-2 p-4">
        <div className="right w-full flex gap-2 flex-col p-4">
          <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-row w-full relative overflow-x-hidden">
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
              {/* Email, Birth Date, Gender, Category, Hand, Year, Org, Country */}
              <div className="flex font-medium">Email:</div>
              <div className="flex">{userData.email}</div>
              <div className="flex font-medium">Birth Date:</div>
              <div className="flex">{formatDate(userData.dateOfBirth)}</div>
              <div className="flex font-medium">Gender:</div>
              <div className="flex">{userData.gender && userData.gender.trim() !== "" && userData.gender !== "\u0000" ? userData.gender : "-"}</div>
              <hr className="col-span-2 my-4 border-gray-300 w-full" />
              <div className="flex font-medium">Category:</div>
              <div className="flex">{userData.weapon && userData.weapon.trim() !== "" && userData.weapon !== "\u0000" ? userData.weapon : "-"}</div>
              <div className="flex font-medium">Dominant Arm:</div>
              <div className="flex">{userData.hand && userData.hand.trim() !== "" && userData.hand !== "\u0000" ? userData.hand : "-"}</div>
              <div className="flex font-medium">Debut Year:</div>
              <div className="flex">{userData.debutYear ? userData.debutYear : "-"}</div>
              <div className="flex font-medium">Organisation:</div>
              <div className="flex">{userData.club ? userData.club : "-"}</div>
              <div className="flex font-medium">Country:</div>
              <div className="flex">{userData.country}</div>
            </div>

            {/* Edit Icon */}
            <div className="absolute top-4 right-4 cursor-pointer text-gray-600">
              ✏️
            </div>
          </div>

          <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-row w-full relative mx-auto mt-4">
            <Tabs>
              <Tab label="User Ranking">
                <div className="grid grid-cols-[2fr_3fr_3fr]">
                  <div className="grid grid-cols-[2fr_1fr] gap-y-2 ml-4 my-4 text-xl w-60">
                    <div className="flex font-medium">National Rank</div>
                    <div className="flex">{userData.rank ? userData.rank : "-"}</div>
                    <div className="flex font-medium">Total Points</div>
                    <div className="flex">{userData.points ? userData.points : "-"}</div>
                    <div className="flex font-medium">Tournament Participations</div>
                    {/* placeholder stuff */}
                    <div className="flex">{userData.tournaments ? userData.tournaments : "-"}</div>
                  </div>

                  <div className="w-[99%] h-full">
                      <LineGraph data={rankGraphData} options={rankGraphOptions} height={200} />
                    
                  </div>
                  <div className="w-[99%] h-full">
                    <LineGraph data={pointsGraphData} options={pointsGraphOptions} height={200}/>
                  </div>


                </div>
              </Tab>
              <Tab label="Past Tournaments">
                <div className="py-4">
                  <h2 className="text-lg font-medium mb-2">Tab 2</h2>
                  <p className="text-gray-700">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Maxime mollitia, molestiae quas vel sint commodi repudiandae
                    consequuntur voluptatum laborum numquam blanditiis harum
                    quisquam eius sed odit fugiat iusto fuga praesentium optio,
                    eaque rerum! Provident similique accusantium nemo autem.
                    Veritatis obcaecati tenetur iure eius earum ut molestias
                    architecto voluptate aliquam nihil, eveniet aliquid culpa
                    officia aut! Impedit sit sunt quaerat, odit, tenetur error,
                    harum nesciunt ipsum debitis quas aliquid. Reprehenderit,
                    quia. Quo neque error repudiandae fuga? Ipsa laudantium
                    molestias eos sapiente officiis modi at sunt excepturi
                    expedita sint? Sed quibusdam recusandae alias error harum
                    maxime adipisci amet laborum.
                  </p>
                </div>
              </Tab>
              <Tab label="Upcoming Tournaments">
                <div className="py-4">
                  <h2 className="text-lg font-medium mb-2">Tab 3</h2>
                  <p className="text-gray-700">
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Maxime mollitia, molestiae quas vel sint commodi repudiandae
                    consequuntur voluptatum laborum numquam blanditiis harum
                    quisquam eius sed odit fugiat iusto fuga praesentium optio,
                    eaque rerum! Provident similique accusantium nemo autem.
                    Veritatis obcaecati tenetur iure eius earum ut molestias
                    architecto voluptate aliquam nihil, eveniet aliquid culpa
                    officia aut! Impedit sit sunt quaerat, odit, tenetur error,
                    harum nesciunt ipsum debitis quas aliquid. Reprehenderit,
                    quia. Quo neque error repudiandae fuga? Ipsa laudantium
                    molestias eos sapiente officiis modi at sunt excepturi
                    expedita sint? Sed quibusdam recusandae alias error harum
                    maxime adipisci amet laborum.
                  </p>
                </div>
              </Tab>
            </Tabs>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FencerDashboard;
