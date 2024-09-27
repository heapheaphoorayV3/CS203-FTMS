import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import Navbar from "../Navbar";
import Sidebar from "../Sidebar";
import logo from "../../Assets/logo.png";
import TournamentService from "../../Services/Tournament/TournamentService";

const CreateTournament = () => {
  const { register, watch, handleSubmit, formState: { errors }} = useForm();

  // Start Date cannot be before End Date
  const startDate = watch("startDate");

  const navigate = useNavigate();

  const onSubmit = async (data) => {

    console.log(data);
    
    try {
      await TournamentService.createTournament(data).then(() => {
        navigate("/dashboard");
      });
    } catch (error) {
      console.log(error);
    }

  };

  return (
    <div className="app-container">
      <div className="navbar">
        <Navbar />
      </div>
      <div className="sidebar">
        <Sidebar />
      </div>

      <div className="flex flex-col items-center bg-gray-200 relative">
        <div className="flex flex-col items-center bg-white mt-24 mb-4 rounded-lg shadow-lg w-[600px]">
          <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
            <img src={logo} alt="OnlyFence" className="h-12 mx-auto mb-4" />
            <h2 className="text-2xl font-bold mb-6 text-center">
              Create New Tournament
            </h2>

            <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Tournament Name */}
              <div>
                <label className="block font-medium mb-1">
                  Tournament Name
                </label>
                <input
                  type="text"
                  {...register('tournamentName', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 ${errors.tournamentName ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.tournamentName && <p className="text-red-500 text-sm italic">{errors.tournamentName.message}</p>}
              </div>

              {/* Location */}
              <div>
                <label className="block font-medium mb-1">Location</label>
                <input
                  type="text"
                  {...register('location', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 ${errors.location ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.location && <p className="text-red-500 text-sm italic">{errors.location.message}</p>}
              </div>

              {/* Start Date */}
              <div>
                <label className="block font-medium mb-1">Start Date</label>
                <input
                  type="date"
                  {...register('startDate', 
                    { required: "Please fill this in!",
                      validate: value => {
                        const selectedDate = new Date(value);
                        const today = new Date();
                        today.setHours(0, 0, 0, 0); // Set time to midnight to compare only dates
                        return selectedDate >= today || "Please enter a valid start date!";} 
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.startDate ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.startDate && <p className="text-red-500 text-sm italic">{errors.startDate.message}</p>}
              </div>

              {/* End Date */}
              <div>
                <label className="block font-medium mb-1">End Date</label>
                <input
                  type="date"
                  {...register('endDate', 
                    { required: "Please fill this in!",
                      validate: value => {
                        const selectedDate = new Date(value);
                        const start = new Date(startDate);
                        return selectedDate >= start || "End Date must be same or after Start Date!" + startDate +  selectedDate;} 
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.endDate ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.endDate && <p className="text-red-500 text-sm italic">{errors.endDate.message}</p>}
              </div>

              {/* Signups End Date */}
              <div>
                <label className="block font-medium mb-1">
                  Sign Ups End Date
                </label>
                <input
                  type="date"
                  {...register('signupEndDate', 
                    { required: "Please fill this in!",
                      validate: value => {
                        const selectedDate = new Date(value);
                        const start = new Date(startDate);
                        const today = new Date();
                        today.setHours(0, 0, 0, 0); // Set time to midnight to compare only dates
                        return (selectedDate < start && selectedDate >= today) || "Please enter a valid signup end date!";}
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.signupEndDate ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.signupEndDate && <p className="text-red-500 text-sm italic">{errors.signupEndDate.message}</p>}
              </div>

              {/* poules Elimination % / advancement rate*/}
              <div>
                <label className="block font-medium mb-1">
                  Poules Elimination (%)
                </label>
                <input
                  type="number"
                  {...register('advancementRate', 
                    { required: "Please fill this in!",
                      validate: value => {
                        return value >= 0 && value <= 100 || "Please enter a valid percentage!";}
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.advancementRate ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.advancementRate && <p className="text-red-500 text-sm italic">{errors.advancementRate.message}</p>}
              </div>

              {/* Tournament Description and Rules */}
              <div className="md:col-span-2">
                <label className="block font-medium mb-1">
                  Tournament Description
                </label>
                <textarea
                  {...register('description', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 h-24 ${errors.description ? "border-red-500" : "border-gray-300"}`}
                ></textarea>
                {errors.description && <p className="text-red-500 text-sm italic">{errors.description.message}</p>}
              </div>

              {/* Tournament Rules */}
              <div className="md:col-span-2">
                <label className="block font-medium mb-1">
                  Tournament Rules
                </label>
                <textarea
                  {...register('rules', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 h-24 ${errors.rules ? "border-red-500" : "border-gray-300"}`}
                ></textarea>
                {errors.rules && <p className="text-red-500 text-sm italic">{errors.rules.message}</p>}
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
