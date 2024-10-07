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
        const response = await EventService.getEventDetails(
          eventID
        );
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
    <div className="row-span-2 col-start-2 bg-gray-300 overflow-y-auto">
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
              <p className="text-gray-700">Poules</p>
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
