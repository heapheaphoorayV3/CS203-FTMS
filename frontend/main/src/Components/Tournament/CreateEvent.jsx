import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import Navbar from "../Navbar";
import Sidebar from "../Sidebar";
import logo from "../../Assets/logo white bg.png";
import TournamentService from "../../Services/Tournament/TournamentService";

const CreateEvent = () => {

  const { register, watch, handleSubmit, formState: { errors }} = useForm();

  const startTime = watch("startTime");

  const navigate = useNavigate();

  // Get tournament
  // const { tournamentID } = useParams();
  // const [tournament, setTournament] = useState(null);
  // useEffect(() => {
  //   const fetchTournamentDetails = async () => {
  //     try {
  //       const details = await TournamentService.getTournamentDetails(tournamentID);
  //       setTournament(details);
  //     } catch (error) {
  //       console.error('Error fetching tournament details:', error);
  //     }
  //   };

  //   fetchTournamentDetails();
  // }, [tournamentID]);

  // HArdcode tournament details for now
  const tournament = {
    startDate: "2022-10-01",
    endDate : "2022-10-31"
  }
  // setTournament(tournamentHardCode);


  const onSubmit = async (data) => {
    console.log(data);
    
    try {
      await TournamentService.CreateEvent(data).then(() => {
        navigate("/organiser-dashboard");
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
        <div className="flex flex-col items-center bg-white mt-16 mb-4 rounded-lg shadow-lg w-[600px]">
          <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
            <img src={logo} alt="OnlyFence" className="h-12 mx-auto mb-4" />
            <h2 className="text-2xl font-bold mb-6 text-center">
              Create New Event
            </h2>

            <form onSubmit={handleSubmit(onSubmit)} className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">

              {/* Gender Dropdown */}
              <div>
                <label className="block font-medium mb-1">Gender</label>
                <select
                  {...register('gender', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 ${errors.gender ? "border-red-500" : "border-gray-300"}`}
                >
                  <option value="">Select Gender</option>
                  <option value="M">Male</option>
                  <option value="F">Female</option>
                </select>
                {errors.gender && <p className="text-red-500 text-sm italic">{errors.gender.message}</p>}
              </div>

              {/* Weapon Dropdown */}
              <div>
                <label className="block font-medium mb-1">Weapon</label>
                <select
                  {...register('weapon', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 ${errors.weapon ? "border-red-500" : "border-gray-300"}`}
                >
                  <option value="">Select Weapon</option>
                  <option value="F">Foil</option>
                  <option value="E">Épée</option>
                  <option value="S">Sabre</option>
                </select>
                {errors.weapon && <p className="text-red-500 text-sm italic">{errors.weapon.message}</p>}
              </div>

              {/* Date */}
              <div>
                <label className="block font-medium mb-1">Date</label>
                <input
                  type="date"
                  {...register('date', { required: "Please fill this in!",
                    validate: value => {
                      const selectedDate = new Date(value);
                      const tournamentStart = new Date(tournament.startDate);
                      const tournamentEnd = new Date(tournament.endDate);
                      return (selectedDate >= tournamentStart && selectedDate <= tournamentEnd) || "Event Date must within Tournament Time Frame!";
                    }
                  })}
                  className={`w-full border rounded-md p-2 ${errors.date ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.date && <p className="text-red-500 text-sm italic">{errors.date.message}</p>}
              </div>

              {/* Start Time */}
              <div>
                <label className="block font-medium mb-1">Start Time</label>
                <input
                  type="time"
                  {...register('startTime', { required: "Please fill this in!" })}
                  className={`w-full border rounded-md p-2 ${errors.startTime ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.startTime && <p className="text-red-500 text-sm italic">{errors.startTime.message}</p>}
              </div>

              {/* End Time */}
              <div>
                <label className="block font-medium mb-1">End Time</label>
                <input
                  type="time"
                  {...register('endTime', 
                    { required: "Please fill this in!",
                      validate: value => {
                        return value > startTime || "End Time must be after Start Time!";
                      }
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.endTime ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.endTime && <p className="text-red-500 text-sm italic">{errors.endTime.message}</p>}
              </div>

              {/* Minimum No. of Participants */}
              <div>
                <label className="block font-medium mb-1">
                  Min. No. of Participants
                </label>
                <input
                  type="number"
                  {...register('minParticipants', 
                    { required: "Please fill this in!",
                      validate: value => value > 1 || "Please enter a number more than 1!"
                    }
                  )}
                  className={`w-full border rounded-md p-2 ${errors.minParticipants ? "border-red-500" : "border-gray-300"}`}
                />
                {errors.minParticipants && <p className="text-red-500 text-sm italic">{errors.minParticipants.message}</p>}
              </div>

              {/* Submit Button */}
              <div className="md:col-span-2">
                <button
                  onSubmit={handleSubmit}
                  className="w-full bg-blue-500 text-white rounded-md py-2"
                >
                  Create Event
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CreateEvent;
