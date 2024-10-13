import React, { useEffect, useState } from "react";
import jackinpic from "../Assets/jackinpic.jpg";
import OrganiserService from "../Services/Organiser/OrganiserService";
import { Tabs, Tab } from "./Others/DashboardTabs";

const OrganiserDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
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
    fetchData();
  }, []);



  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  console.log("verified=" + userData.verified);

  return (
    <div className="bg-gray-200 w-full h-full gap-2 p-8 overflow-auto">
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
          ✏️
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="Tab 1">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Tab 1</h2>
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
          <Tab label="Tab 2">
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
          <Tab label="Tab 3">
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
  );
};

export default OrganiserDashboard;
