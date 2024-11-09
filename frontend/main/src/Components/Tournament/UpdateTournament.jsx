import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { DateTime } from "luxon";
import TournamentService from "../../Services/Tournament/TournamentService";
import { XCircleIcon } from "@heroicons/react/16/solid";

const UpdateTournament = ({ selectedTournament, onClose }) => {
  const { tournamentID } = useParams();
  const {
    register,
    watch,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm();

  const signupEndDate = watch("signupEndDate");
  const startDate = watch("startDate");

  const navigate = useNavigate();

  useEffect(() => {
    if (selectedTournament) {
      console.log("Selected tournament:", selectedTournament);
      const advancementRate = parseInt(selectedTournament.advancementRate, 10);
      setValue("advancementRate", advancementRate);
      setValue("description", selectedTournament.description);
      setValue("difficulty", selectedTournament.difficulty);
      setValue("endDate", selectedTournament.endDate);
      setValue("location", selectedTournament.location);
      setValue("name", selectedTournament.name);
      setValue("rules", selectedTournament.rules);
      setValue("signupEndDate", selectedTournament.signupEndDate);
      setValue("startDate", selectedTournament.startDate);
    }
  }, [selectedTournament, setValue]);

  const onSubmit = async (data) => {
    // Prepare formData to be sent to the API
    const difficultyChar = data.difficulty.charAt(0);
    const formData = {
      ...data,
      difficulty: difficultyChar,
    };

    console.log("Updating tournament with data:", formData);

    try {
      await TournamentService.updateTournament(tournamentID, formData); // Call the update method
      onClose(); // Redirect to a view page after update
    } catch (error) {
      console.error("Error updating tournament:", error);
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center"
      style={{ zIndex: 3 }}
    >
      <div className="mt-20 bg-white rounded-lg w-1/3 max-h-[70vh] overflow-y-hidden flex flex-col justify-center lg:pl-8">
        <div className="flex flex-col overflow-y-auto pr-4 my-4">
          
          {/* Close Button --> ml-auto pushes button to the right of the form */}
          <button
            onClick={onClose}
            className="ml-auto w-5 text-gray-300 hover:text-gray-800 focus:outline-none"
            aria-label="Close"
          >
            <XCircleIcon /> {/* This is the close icon (×) */}
          </button>

          <h2 className="text-2xl font-bold mb-4 text-center">Update Tournament</h2>

          <form
            onSubmit={handleSubmit(onSubmit)}
            className="grid grid-cols-1 md:grid-cols-2 gap-6"
          >
            {/* Tournament Name */}
            <div>
              <label className="block font-medium mb-1">Tournament Name</label>
              <input
                type="text"
                {...register("name", {
                  required: "Please fill this in!",
                })}
                className={`w-full border rounded-md p-2 ${errors.tournamentName ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.name && (
                <p className="text-red-500 text-sm italic">
                  {errors.name.message}
                </p>
              )}
            </div>

            {/* Location */}
            <div>
              <label className="block font-medium mb-1">Location</label>
              <input
                type="text"
                {...register("location", {
                  required: "Please fill this in!",
                })}
                className={`w-full border rounded-md p-2 ${errors.location ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.location && (
                <p className="text-red-500 text-sm italic">
                  {errors.location.message}
                </p>
              )}
            </div>

            {/* poules Elimination % / advancement rate*/}
            <div>
              <label className="block font-medium mb-1">
                Advancement Rate (%)
              </label>
              <input
                id="advancementRate"
                type="number"
                {...register("advancementRate", {
                  required: "Please fill this in!",
                  validate: (value) => {
                    return (
                      (value >= 60 && value <= 100) ||
                      "Please enter a valid percentage from 60 to 100!"
                    );
                  },
                })}
                className={`w-full border rounded-md p-2 ${errors.advancementRate ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.advancementRate && (
                <p className="text-red-500 text-sm italic">
                  {errors.advancementRate.message}
                </p>
              )}
            </div>

            {/* Difficulty */}
            <div>
              <label className="block font-medium mb-1">Difficulty</label>
              <select
                {...register("difficulty", {
                  required: "Please select a difficulty level!",
                })}
                className={`w-full border rounded-md p-2 ${errors.difficulty ? "border-red-500" : "border-gray-300"
                  }`}
                defaultValue={selectedTournament?.difficulty || ""}
              >
                <option value="" disabled>
                  Select difficulty
                </option>
                <option value="B">Beginner</option>
                <option value="I">Intermediate</option>
                <option value="A">Advanced</option>
              </select>
              {errors.difficulty && (
                <p className="text-red-500 text-sm italic">
                  {errors.difficulty.message}
                </p>
              )}
            </div>

            {/* Start Date */}
            <div>
              <label className="block font-medium mb-1">Start Date</label>
              <input
                type="date"
                {...register("startDate", {
                  required: "Please fill this in!",
                  validate: (value) => {
                    const signupEnd = new Date(signupEndDate);
                    signupEnd.setDate(signupEnd.getDate() + 1); // Add 2 days to sign up end date
                    const selectedDate = new Date(value);
                    return (
                      selectedDate > signupEnd ||
                      "Start Date must be 2 days after Sign Ups End Date!"
                    );
                  },
                })}
                className={`w-full border rounded-md p-2 ${errors.startDate ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.startDate && (
                <p className="text-red-500 text-sm italic">
                  {errors.startDate.message}
                </p>
              )}
            </div>

            {/* End Date */}
            <div>
              <label className="block font-medium mb-1">End Date</label>
              <input
                type="date"
                {...register("endDate", {
                  required: "Please fill this in!",
                  validate: (value) => {
                    const selectedDate = new Date(value);
                    const start = new Date(startDate);
                    return (
                      selectedDate >= start ||
                      "End Date cannot be before Start Date!"
                    );
                  },
                })}
                className={`w-full border rounded-md p-2 ${errors.endDate ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.endDate && (
                <p className="text-red-500 text-sm italic">
                  {errors.endDate.message}
                </p>
              )}
            </div>

            {/* Signups End Date */}
            <div>
              <label className="block font-medium mb-1">Sign Ups End Date</label>
              <input
                type="date"
                {...register("signupEndDate", {
                  required: "Please fill this in!",
                  validate: (value) => {
                    const today = new Date();
                    today.setHours(0, 0, 0, 0); // Set time to midnight to compare only dates
                    const selectedDate = new Date(value);
                    return (
                      selectedDate >= today ||
                      "Signup End Date must be after today!"
                    );
                  },
                })}
                className={`w-full border rounded-md p-2 ${errors.signupEndDate ? "border-red-500" : "border-gray-300"
                  }`}
              />
              {errors.signupEndDate && (
                <p className="text-red-500 text-sm italic">
                  {errors.signupEndDate.message}
                </p>
              )}
            </div>

            {/* Tournament Description and Rules */}
            <div className="md:col-span-2">
              <label className="block font-medium mb-1">
                Tournament Description
              </label>
              <textarea
                {...register("description", {
                  required: "Please fill this in!",
                })}
                className={`w-full border rounded-md p-2 h-24 ${errors.description ? "border-red-500" : "border-gray-300"
                  }`}
              ></textarea>
              {errors.description && (
                <p className="text-red-500 text-sm italic">
                  {errors.description.message}
                </p>
              )}
            </div>

            {/* Tournament Rules */}
            <div className="md:col-span-2">
              <label className="block font-medium mb-1">Tournament Rules</label>
              <textarea
                {...register("rules", { required: "Please fill this in!" })}
                className={`w-full border rounded-md p-2 h-24 ${errors.rules ? "border-red-500" : "border-gray-300"
                  }`}
              ></textarea>
              {errors.rules && (
                <p className="text-red-500 text-sm italic">
                  {errors.rules.message}
                </p>
              )}
            </div>

            {/* Submit Button */}
            <div className="md:col-span-2">
              <button
                //   onSubmit={handleSubmit}
                className="w-full bg-blue-500 text-white rounded-md py-2"
              >
                Update Tournament
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default UpdateTournament;
