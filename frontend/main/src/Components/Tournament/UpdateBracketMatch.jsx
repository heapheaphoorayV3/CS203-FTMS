import { React, useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { XCircleIcon } from "@heroicons/react/16/solid";
import EventService from "../../Services/Event/EventService";

const UpdateBracketMatch = ({ matches, onClose, eventID }) => {
  const [matchData, setMatchData] = useState(["Select Match"]); // Match data for the dropdown
  const [showConfirmation, setShowConfirmation] = useState(false); // Confirmation popup state
  const [trackSelectedMatch, setTrackSelectedMatch] = useState(null); // Track the selected match
  const [pendingData, setPendingData] = useState(null); // State to store form data temporarily
  const [error, setError] = useState(null); // Error state
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  let roundTypeValue = watch("roundTypeInput");
  let roundTypes = ["Select Round"];

  // The number of match types/rounds (Top 16, Quarters, Semis, Finals, etc)
  let numRoundTypes = Math.log2(matches.length + 1); // Add 1 to account for the final match
  while (numRoundTypes >= 1) {
    // Add the match type of the first match of each type to the array, which is match 2^(n-1), -1 for array index
    roundTypes.push(matches[Math.pow(2, numRoundTypes - 1) - 1].name);
    numRoundTypes--;
  }

  useEffect(() => {
    for (let i = 0; i < matches.length; i++) {
      let newMatchData = ["Select Match"];
      newMatchData = newMatchData.concat(
        matches
          .filter(match => match.participants.length === 2 && match.name === roundTypeValue)
          .map(match => `${match.participants[0].name} vs ${match.participants[1].name}`)
      );
      if (roundTypeValue === "Select Round") setMatchData(["Select Match"]);
      else if (newMatchData.length === 0) setMatchData(["No matches available"]);
      else setMatchData(newMatchData);
    }
  }, [roundTypeValue]);

  // Handler to update trackSelectedMatch on selection
  const handleMatchSelect = (selectedMatch) => {
    const matchObject = matches.find(
      match => match.participants.length === 2
        && `${match.participants[0].name} vs ${match.participants[1].name}` === selectedMatch
    );
    setTrackSelectedMatch(matchObject);
    console.log("Selected Match: ", matchObject);
  };

  // Trigger the confirmation popup instead of directly submitting
  const handleFormSubmit = (data) => {
    setPendingData(data); // Store the form data temporarily
    setShowConfirmation(true); // Show the confirmation popup
  };

  // To pass this particular match data to the ViewEvent component, confirms submission if the user chooses "Yes"
  const confirmSubmit = () => {
    setShowConfirmation(false); // Hide the popup
    submitUpdateBracketMatches({ ...pendingData, trackSelectedMatch }); // Submit the form data
  };

  // Method to submit the updated match
  const submitUpdateBracketMatches = async (data) => {
    try {
      const matchId = data.trackSelectedMatch.id;

      // Structure the combined data to match the backend's expected DTO format
      const combinedData = {
        matchId: matchId,
        score1: data.firstScore,
        score2: data.secondScore,
      };

      console.log(combinedData);

      await EventService.updateDEMatch(eventID, combinedData);
      onClose();
    } catch (error) {
      if (error.response) {
        console.log("Error response data: ", error.response.data);
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        console.log("Error request: ", error.request);
        setError("Failed to update match, please try again later.");
      } else {
        // Something happened in setting up the request that triggered an Error
        console.log("Unknown Error: " + error);
        setError("Failed to update match, please try again later.");
      }
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

        <h2 className="text-2xl font-bold mb-6 text-center">
          Update Match
        </h2>

        <form
          onSubmit={handleSubmit(handleFormSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
        >

          {/* Match Round Dropdown */}
          <div>
            <label className="block font-medium mb-1">Match Round</label>
            <select
              {...register("roundTypeInput", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.roundTypeInput ? "border-red-500" : "border-gray-300"
                }`}
            >
              {roundTypes.map((match) => (
                <option key={match} value={match}>
                  {match}
                </option>
              ))}
            </select>
            {errors.roundTypeInput && (
              <p className="text-red-500 text-sm italic">
                {errors.roundTypeInput.message}
              </p>
            )}
          </div>

          {/* Select Match Dropdown */}
          <div>
            <label className="block font-medium mb-1">Match</label>
            <select
              {...register("match", {
                required: "Please fill this in!",
                validate: (value) => {
                  return (
                    (value != "Select Match") ||
                    "Please select a match"
                  );
                },
              })}
              onChange={(e) => handleMatchSelect(e.target.value)}
              className={`w-full border rounded-md p-2 ${errors.match ? "border-red-500" : "border-gray-300"
                }`}
            >
              {matchData.map((matchVs) => (
                <option key={matchVs} value={matchVs}>
                  {matchVs}
                </option>
              ))}
            </select>
            {errors.match && (
              <p className="text-red-500 text-sm italic">
                {errors.match.message}
              </p>
            )}
          </div>

          {/* Fencer 1 Score Field */}
          <div>
            <label className="block font-medium mb-1">
              First Fencer's Score
            </label>
            <input
              type="number"
              {...register("firstScore", {
                required: "Please fill this in!",
                validate: (value) => {
                  return (
                    (value >= 0) ||
                    "Please enter a valid score"
                  );
                },
              })}
              className={`w-full border rounded-md p-2 ${errors.firstScore
                ? "border-red-500"
                : "border-gray-300"
                }`}
            />
            {errors.firstScore && (
              <p className="text-red-500 text-sm italic">
                {errors.firstScore.message}
              </p>
            )}
          </div>

          {/* Fencer 2 Score Field */}
          <div>
            <label className="block font-medium mb-1">
              Second Fencer's Score
            </label>
            <input
              type="number"
              {...register("secondScore", {
                required: "Please fill this in!",
                validate: (value) => {
                  return (
                    (value >= 0) ||
                    "Please enter a valid score"
                  );
                },
              })}
              className={`w-full border rounded-md p-2 ${errors.secondScore
                ? "border-red-500"
                : "border-gray-300"
                }`}
            />
            {errors.secondScore && (
              <p className="text-red-500 text-sm italic">
                {errors.secondScore.message}
              </p>
            )}
          </div>

          {/* Submit Button */}
          <div className="md:col-span-2">
            <button
              type="submit"
              className="w-full bg-blue-500 text-white rounded-md py-2"
            >
              Update Match
            </button>
          </div>
        </form>
        {/* Confirmation popup */}
        {showConfirmation && (
          <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
            <div className="relative top-[-30%] bg-white p-6 rounded-lg shadow-lg text-center">
              <h2 className="text-lg font-bold">Confirm Update</h2>
              <p>Are you sure you want to make the match update below?</p>
              <div className="mt-4 flex justify-center">
                <button
                  onClick={() => confirmSubmit()}
                  className="px-4 py-2 bg-blue-500 text-white rounded-lg mr-2"
                >
                  Update
                </button>
                <button
                  onClick={() => setShowConfirmation(false)}
                  className="px-4 py-2 bg-red-400 text-white rounded-lg"
                >
                  Cancel
                </button>
              </div>
              {error && <h2 className="text-red-500 text-center mt-4"> {error} </h2>}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default UpdateBracketMatch;
