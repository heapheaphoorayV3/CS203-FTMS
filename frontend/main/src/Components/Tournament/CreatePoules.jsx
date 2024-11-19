import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { XCircleIcon } from "@heroicons/react/16/solid";
import EventService from "../../Services/Event/EventService";

const CreatePoules = ({ onClose, eventID }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const [recommendedPoulesData, setRecommendedPoulesData] = useState([]);
  const [recommendedPoulesError, setRecommendedPoulesError] = useState(null);
  const [createPouleError, setCreatePouleError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchRecommendedPoules = async () => {
    try {
      const response = await EventService.getRecommendedPoules(eventID);
      setRecommendedPoulesData(response.data);
    } catch (error) {
      if (error.response) {
        setRecommendedPoulesError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        setRecommendedPoulesError(
          "Recommended Poules have failed to load, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        setRecommendedPoulesError(
          "Recommended Poules have failed to load, please try again later."
        );
      }
    }
  };

  useEffect(() => {
    fetchRecommendedPoules();
  }, []);

  // Submit Poule Creation
  const onSubmit = async (data) => {
    const payload = {
      eid: String(eventID), // Add the eventID to the payload
      ...data, // Spread the rest of the data
    };
    setIsLoading(true);
    try {
      await EventService.createPoules(payload.eid, payload);
      onClose();
    } catch (error) {
      if (error.response) {
        setCreatePouleError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        setCreatePouleError("Poule Creation Failed, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        setCreatePouleError("Poule Creation Failed, please try again later.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white rounded-lg w-1/3 min-w-[500px] flex flex-col justify-center pt-6 py-12 lg:px-8">
        {/* Close Button --> ml-auto pushes button to the right of the form */}
        <button
          onClick={onClose}
          className="ml-auto w-5 text-gray-300 hover:text-gray-800 focus:outline-none"
          aria-label="Close"
        >
          <XCircleIcon /> {/* This is the close icon (×) */}
        </button>

        <h2 className="text-2xl font-bold mb-4 text-center">Create Poules</h2>
        <div className="mb-4">
          <p className="font-medium">Recommended Poules</p>
          {recommendedPoulesError ? (
            <p className="text-red-500">{recommendedPoulesError}</p>
          ) : recommendedPoulesData.length > 0 ? (
            <ul>
              {recommendedPoulesData.map((poule, index) => (
                <li key={index}>{poule}</li>
              ))}
            </ul>
          ) : (
            <p className="text-red-500">No recommended poules available.</p>
          )}
        </div>
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
        >
          {/* Number of poules input */}
          <div>
            <label className="block font-medium mb-1">Number of Poules</label>
            <input
              type="number"
              {...register("pouleCount", {
                required: "Please fill this in!",
                validate: (value) => {
                  const parsedValue = parseInt(value, 10); // Parse the input to an integer
                  // Check if the parsed value is a positive integer
                  if (!Number.isInteger(parsedValue) || parsedValue <= 0) {
                    return "Please enter a positive number!";
                  }
                  return true; // If valid, return true
                },
              })}
              className={`w-full border rounded-md p-2 ${errors.pouleCount ? "border-red-500" : "border-gray-300"
                }`}
            />
            {errors.pouleCount && (
              <p className="text-red-500 text-sm italic">
                {errors.pouleCount.message}
              </p>
            )}
          </div>

          {/* Submit Button */}
          <div className="md:col-span-2 flex justify-center items-center">
            {!isLoading ? (<button
              onClick={handleSubmit}
              className={`w-full rounded-md py-2 ${recommendedPoulesData.length === 0
                  ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                  : "bg-blue-500 text-white"
                }`}
              disabled={recommendedPoulesData.length === 0} // Disable button when the length is not 0
            >
              Create Poules
            </button>) : (
              <button
                className="py-2 relative"
                disabled
              >
                <div className="animate-spin rounded-full h-7 w-7 border-b-2 border-sky-500 mx-auto"></div>
              </button>)}
          </div>
        </form>
        {createPouleError && (
          <h2 className="text-red-500 text-center mt-4">
            {createPouleError}
          </h2>
        )}
      </div>
    </div>
  );
};

export default CreatePoules;
