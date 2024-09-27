import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import ViewCard from "../Others/ViewCard.jsx";
import EventList from "../Others/EventList.jsx";
import Navbar from "../Navbar.jsx";
import Sidebar from "../Sidebar.jsx";
import { Tabs, Tab } from "../Others/DashboardTabs.jsx";
import SubmitButton from "../Others/SubmitButton.jsx";

export default function ViewEvent() {
  return (
    <div className="row-span-2 col-start-2 bg-gray-300 overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        Event Name
      </h1>

      <div className="ml-12 mr-8 mb-10 grid grid-cols-3 auto-rows-fr gap-x-[10px] gap-y-[10px]">
        <div className="font-semibold text-lg">Date</div>
        <div className="font-semibold text-lg">Start Time</div>
        <div className="font-semibold text-lg">End Time</div>
        <div className="text-lg">Date</div>
        <div className="text-lg">Start Time</div>
        <div className="text-lg">End Time</div>
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
