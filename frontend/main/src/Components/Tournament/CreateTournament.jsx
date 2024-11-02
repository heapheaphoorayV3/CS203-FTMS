import React from "react";
import { useNavigate } from "react-router-dom";
import { useForm, Controller } from "react-hook-form";
import TournamentService from "../../Services/Tournament/TournamentService";

const CreateTournament = () => {
  const {
    register,
    watch,
    handleSubmit,
    formState: { errors },
    control,
  } = useForm();

  // Start Date cannot be before End Date
  const signupEndDate = watch("signupEndDate");
  const startDate = watch("startDate");

  const navigate = useNavigate();

  const onSubmit = async (data) => {
    try {
      if (typeof data.signupEndDate === "string") {
        data.signupEndDate = new Date(`${data.signupEndDate}T00:00:00`)
          .toISOString()
          .slice(0, 19);
      }
      console.log("Data being sent to the server:", data);
      const tournamentId = await TournamentService.createTournament(data);
      navigate(`/tournaments/${tournamentId.data.id}`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="app-container">
      <div className="flex flex-col items-center bg-white relative">
        <div className="flex flex-col items-center bg-white mt-8 mb-4 rounded-lg shadow-lg w-[600px]">
          <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
            <h2 className="text-2xl font-bold mb-6 text-center">
              Create New Tournament
            </h2>

            <form
              onSubmit={handleSubmit(onSubmit)}
              className="grid grid-cols-1 md:grid-cols-2 gap-6"
            >
              {/* Tournament Name */}
              <div>
                <label className="block font-medium mb-1">
                  Tournament Name
                </label>
                <input
                  type="text"
                  {...register("name", {
                    required: "Please fill this in!",
                  })}
                  className={`w-full border rounded-md p-2 ${
                    errors.tournamentName ? "border-red-500" : "border-gray-300"
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
                  className={`w-full border rounded-md p-2 ${
                    errors.location ? "border-red-500" : "border-gray-300"
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
                  className={`w-full border rounded-md p-2 ${
                    errors.advancementRate
                      ? "border-red-500"
                      : "border-gray-300"
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
                  className={`w-full border rounded-md p-2 ${
                    errors.difficulty ? "border-red-500" : "border-gray-300"
                  }`}
                  defaultValue=""
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
                  className={`w-full border rounded-md p-2 ${
                    errors.startDate ? "border-red-500" : "border-gray-300"
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
                  className={`w-full border rounded-md p-2 ${
                    errors.endDate ? "border-red-500" : "border-gray-300"
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
                <label className="block font-medium mb-1">
                  Sign Ups End Date
                </label>
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
                  className={`w-full border rounded-md p-2 ${
                    errors.signupEndDate ? "border-red-500" : "border-gray-300"
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
                  className={`w-full border rounded-md p-2 h-24 ${
                    errors.description ? "border-red-500" : "border-gray-300"
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
                <label className="block font-medium mb-1">
                  Tournament Rules
                </label>
                <textarea
                  {...register("rules", { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 h-24 ${
                    errors.rules ? "border-red-500" : "border-gray-300"
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
                  onSubmit={handleSubmit}
                  className="w-full bg-blue-500 text-white rounded-md py-2"
                >
                  Create Tournament
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateTournament;
