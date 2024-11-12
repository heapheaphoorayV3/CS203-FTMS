import { XCircleIcon } from "@heroicons/react/20/solid";
import { useState } from "react";
import EventService from "../../Services/Event/EventService";

const EndEvent = ({ id, closeEndEventPopup }) => {
  const [error, setError] = useState(null);

  console.log("id:", id);

  const handleEndEvent = async () => {
    try {
      await EventService.endEvent(id);
      console.log("EVENT SERVICE END");
      closeEndEventPopup();
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError("Failed to end event, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("Failed to end event, please try again later.");
      }
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      style={{ zIndex: 3 }}
    >
      <div className="bg-white rounded-lg w-1/3 min-w-[300px] flex flex-col justify-center pt-6 py-12 lg:px-8">
        <button
          className="ml-auto w-5 text-gray-300 hover:text-gray-800 focus:outline-none"
          aria-label="Close"
          onClick={() => closeEndEventPopup()}
        >
          <XCircleIcon />
        </button>
        <div className="flex flex-col justify-center items-center gap-5">
          <h1 className="text-xl font-semibold text-center mt-5">
            Are you sure you want to end this event?
          </h1>
          <button
            className="w-1/5 bg-red-500 text-white font-semibold py-2 rounded-md hover:bg-red-600"
            onClick={handleEndEvent}
          >
            End Event
          </button>
          {error && (
            <div className="text-xl font-semibold text-center mt-5">
              <p>{error}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default EndEvent;
