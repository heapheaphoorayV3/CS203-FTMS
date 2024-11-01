import { useState, useEffect } from "react";
import PaginationButton from "./Others/Pagination";
import FencerService from "../Services/Fencer/FencerService";
import sender1 from "../Assets/sender1.svg";
import user1 from "../Assets/user1.svg";
import InputField from "./Others/InputField";

export default function Chatbot() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  //   if (loading) {
  //     return <div className="mt-10">Loading...</div>; // Show loading state
  //   }

  //   if (error) {
  //     return <div className="mt-10">{error}</div>; // Show error message if any
  //   }

  return (
    <div className="bg-white h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">Chatbot</h1>
      <div className="flex w-[80%] ml-12 mb-12">
        <img src={sender1} alt="Sender image" className="w-[200px] h-auto" />
        <div className="flex flex-col h-auto w-full" id="messages">
          <div className="rounded-md bg-blue-500 p-4 text-white mb-4 mr-12">
            <p>Hello! What would you like to know? Pick an option below:</p>
          </div>
          <div className="flex justify-between gap-4 mr-12">
            <div className="rounded-md bg-blue-500 p-4 text-white w-full">
              <p>Get my projected points</p>
            </div>
            <div className="rounded-md bg-blue-500 p-4 text-white w-full">
              <p>Get my win rate</p>
            </div>
            <div className="rounded-md bg-blue-500 p-4 text-white w-full">
              <p>Recommend me tournaments</p>
            </div>
          </div>
        </div>
      </div>
      <div className="flex justify-end w-[80%] ml-[200px]">
        <div className="flex flex-col h-auto w-full" id="messages">
          <div className="rounded-md bg-blue-800 p-4 text-white mb-4 ml-12">
            <p>choice made</p>
          </div>
        </div>
        <img src={user1} alt="Sender image" className="w-[200px] h-auto" />
      </div>
    </div>
  );
}
