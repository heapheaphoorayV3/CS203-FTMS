import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ViewCard from "../Others/ViewCard.jsx";
import EventList from "../Others/EventList.jsx";
import Navbar from "../Navbar.jsx";
import Sidebar from "../Sidebar.jsx";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import SubmitButton from "../Others/SubmitButton.jsx";
import Breadcrumbs from "../Others/Breadcrumbs.jsx";
import FencerService from "../../Services/Fencer/FencerService.js";
import TournamentService from "../../Services/Tournament/TournamentService.js";

export default function ViewTournament() {
  const breadcrumbs = [
    { name: "Home", link: "/" },
    { name: "Tournaments", link: "/tournaments" },
  ];

  // Retrieve tournament ID from URL
  const { tournamentID } = useParams();

  const [tournamentData, setTournamentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // Fetch tournament data if tournamentID changes
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await TournamentService.getTournamentDetails(tournamentID);
        setTournamentData(response.data);
        console.log("response.data => ", response.data);
      } catch (error) {
        console.error("Error fetching tournament data:", error);
        setError("Failed to load tournament data.");
      } finally {
        setLoading(false);
      }
    };

    if (tournamentID) {
      fetchData();
    }
  }, [tournamentID]);

  // Loading / Error states
  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  const handleSubmit = async (data) => {
    try {
      await FencerService.registerEvent(data).then(() => {
        navigate("/fencer-dashboard");
      });
    } catch (error) {
      console.log(error);
    }
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


  return (
    // Grid for Navbar, Sidebar and Content

    <div className="row-span-2 col-start-2 bg-gray-300 h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        {tournamentData.name}
      </h1>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-4 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Organiser</div>
        <div className="font-semibold text-lg">Dates</div>
        <div className="font-semibold text-lg">Location</div>
        <div className="font-semibold text-lg">Status</div>
        <div className="text-lg">{tournamentData.organiserName}</div>
        <div className="text-lg">
          {formatDateRange(tournamentData.startDate, tournamentData.endDate)}
        </div>
        <div className="text-lg">{tournamentData.location}</div>
        <div className="text-lg">{getTournamentStatus(tournamentData.startDate, tournamentData.endDate)}</div>
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
            <table className="table text-lg">
              {/* head */}
              <thead className="text-lg">
                <tr>
                  <th></th>
                  <th>Event Name</th>
                  <th>Date</th>
                  <th>Start Time</th>
                  <th>End Time</th>
                  <th>Sign Up</th>
                </tr>
              </thead>
              <tbody>
                {/* row 1 */}
                <tr className="hover:bg-gray-200">
                  <th>1</th>
                  <td>
                    <a
                      href="/view-event"
                      className="underline hover:text-accent"
                    >
                      Event 1
                    </a>
                  </td>
                  <td>Date</td>
                  <td>Start</td>
                  <td>End</td>
                  <td>
                    <SubmitButton onSubmit={handleSubmit}>Sign Up</SubmitButton>
                  </td>
                </tr>
                {/* row 2 */}
                <tr className="hover:bg-gray-200">
                  <th>2</th>
                  <td>Event 1</td>
                  <td>Date</td>
                  <td>Start</td>
                  <td>End</td>
                  <td></td>
                </tr>
                {/* row 3 */}
                <tr className="hover:bg-gray-200">
                  <th>3</th>
                  <td>Event 1</td>
                  <td>Date</td>
                  <td>Start</td>
                  <td>End</td>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </Tab>
          <Tab label="Ranking">
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
}
