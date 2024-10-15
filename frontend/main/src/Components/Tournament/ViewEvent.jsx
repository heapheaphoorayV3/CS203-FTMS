import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ViewCard from "../Others/ViewCard.jsx";
import EventList from "../Others/EventList.jsx";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import EventService from "../../Services/Event/EventService.js";

export default function ViewEvent() {
  const { eventID } = useParams();

  const [eventData, setEventData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await EventService.getEventDetails(eventID);
        setEventData(response.data);
        console.log("response.data => ", response.data);
      } catch (error) {
        console.error("Error fetching event data:", error);
        setError("Failed to load event data.");
      } finally {
        setLoading(false);
      }
    };

    if (eventID) {
      fetchData();
    }
  }, [eventID]);

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  const formatDate = (date) => {
    const formattedDate = new Date(date).toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    });
    return `${formattedDate}`;
  };

  return (
    <div className="row-span-2 col-start-2 bg-gray-300 h-full">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        {eventData.tournamentName} - {eventData.eventName}
      </h1>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-3 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Date</div>
        <div className="font-semibold text-lg">Start Time</div>
        <div className="font-semibold text-lg">End Time</div>
        <div className="text-lg">{formatDate(eventData.date)}</div>
        <div className="text-lg">{eventData.startTime}</div>
        <div className="text-lg">{eventData.endTime}</div>
      </div>

      <div className="ml-12 mr-8 text-lg overflow-x-auto">
        <Tabs>
          <Tab label="Poules">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Poules</h2>
              <label className="block font-medium mb-1">Poule Number</label>
              <select
              >
                <option value="">Select Poule Number</option>
                <option value="1">Poule 1</option>
                <option value="2">Poule 2</option>
              </select>
              <table className="table text-lg">
                {/* head */}
                <thead className="text-lg">
                  <tr>
                    <th className="w-60">Fencer</th>
                    <th className="w-24"></th>
                    <th className="text-center w-24">1</th>
                    <th className="text-center w-24">2</th>
                    <th className="text-center w-24">3</th>
                    <th className="text-center w-24">4</th>
                    <th className="text-center w-24">5</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td className="w-60">Name1</td>
                    <td className="font-bold text-center border-r border-black w-24">
                      1
                    </td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                  </tr>
                  <tr>
                    <td className="w-60">Name1</td>
                    <td className="font-bold text-center border-r border-black w-24">
                      2
                    </td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                  </tr>
                  <tr>
                    <td className="w-60">Name1</td>
                    <td className="font-bold text-center border-r border-black w-24">
                      3
                    </td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                  </tr>
                  <tr>
                    <td className="w-60">Name1</td>
                    <td className="font-bold text-center border-r border-black w-24">
                      4
                    </td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                  </tr>
                  <tr>
                    <td className="w-60">Name1</td>
                    <td className="font-bold text-center border-r border-black w-24">
                      5
                    </td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                    <td className="border border-black">result</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </Tab>
          <Tab label="Bracket">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Bracket</h2>
            </div>
          </Tab>
          <Tab label="Ranking">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Ranking</h2>
            </div>
          </Tab>
          <Tab label="Participants">
            <div className="py-4">
              <h2 className="text-lg font-medium mb-2">Participants</h2>
            </div>
          </Tab>
        </Tabs>
      </div>
    </div>
  );
}
