import React from "react";
import { useForm } from "react-hook-form";
import { XCircleIcon } from "@heroicons/react/16/solid";

const UpdateBracketMatch = ({ matchTypes, matches, onClose, onSubmit }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

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
          onSubmit={handleSubmit(onSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
        >

          {/* Match Round Dropdown */}
          <div>
            <label className="block font-medium mb-1">Match Round</label>
            <select
              {...register("matchType", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.matchType ? "border-red-500" : "border-gray-300"
                }`}
            >
              {matchTypes.map((matchType) => (
                <option key={matchType.value} value={matchType.value}>
                  {matchType.label}
                </option>
              ))}
            </select>
            {errors.matchType && (
              <p className="text-red-500 text-sm italic">
                {errors.matchType.message}
              </p>
            )}
          </div>

          {/* Select Match Dropdown */}
          <div>
            <label className="block font-medium mb-1">Select Match</label>
            <select
              {...register("match", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.match ? "border-red-500" : "border-gray-300"
                }`}
            >
              {matches.map((match) => (
                <option key={match.value} value={match.value}>
                  {match.label}
                </option>
              ))}
            </select>
            {errors.match && (
              <p className="text-red-500 text-sm italic">
                {errors.match.message}
              </p>
            )}
          </div>

          {/* Submit Button */}
          <div className="md:col-span-2">
            <button
              onSubmit={handleSubmit}
              className="w-full bg-blue-500 text-white rounded-md py-2"
            >
              Update Match
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateBracketMatch;
