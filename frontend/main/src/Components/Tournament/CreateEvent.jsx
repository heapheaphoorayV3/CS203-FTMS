import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { DateTime } from "luxon";
import TournamentService from "../../Services/Tournament/TournamentService";
import EventService from "../../Services/Event/EventService";
import { XCircleIcon } from "@heroicons/react/16/solid";

const CreateEvent = ({eventDetails, onClose}) => {
  const {
    register,
    watch,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const startTime = watch("startTime");

  const navigate = useNavigate();

  // Get tournament
  const { tournamentID } = useParams();
  const [tournament, setTournament] = useState(null);
  useEffect(() => {
    const fetchTournamentDetails = async () => {
      try {
        const details = await TournamentService.getTournamentDetails(
          tournamentID
        );
        // details --> contain data object --> contains tournament object
        setTournament(details.data);
      } catch (error) {
        console.error("Error fetching tournament details:", error);
      }
    };

    fetchTournamentDetails();
  }, [tournamentID]);

  const onSubmit = async (data) => {
    // modify time to add seconds
    const startTimeString = data.startTime + ":00"; // Adding seconds
    const endTimeString = data.endTime + ":00"; // Adding seconds

    // Create DateTime objects
    const startTime = DateTime.fromFormat(startTimeString, "HH:mm:ss");
    const endTime = DateTime.fromFormat(endTimeString, "HH:mm:ss");

    // Format to hh:mm:ss
    const formattedStartTime = startTime.toFormat("HH:mm:ss");
    const formattedEndTime = endTime.toFormat("HH:mm:ss");

    console.log(typeof formattedStartTime); // e.g., "21:00:00"
    console.log("Formatted End Time:", formattedEndTime); // e.g., "22:00:00"

    // Now you can assign these to your formData
    const formData = {
      ...data,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
    };
    console.log("Data being sent to the server:", formData);

    try {
      await EventService.createEvent(tournamentID, formData).then(() => {
        navigate("/view-tournament/:tournamentID");
      });
    } catch (error) {
      console.log(error);
    }
  };

  // return (
  //   <div className="app-container">
  //     <div className="flex flex-col items-center bg-gray-200 relative">
  //       <div className="flex flex-col items-center bg-white mt-8 mb-4 rounded-lg shadow-lg w-[600px]">
  // <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
  //   <h2 className="text-2xl font-bold mb-6 text-center">
  //     Create New Event
  //   </h2>

  //   <form
  //     onSubmit={handleSubmit(onSubmit)}
  //     className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
  //   >
  //     {/* Gender Dropdown */}
  //     <div>
  //       <label className="block font-medium mb-1">Gender</label>
  //       <select
  //         {...register("gender", { required: "Please fill this in!" })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.gender ? "border-red-500" : "border-gray-300"
  //         }`}
  //       >
  //         <option value="">Select Gender</option>
  //         <option value="M">Male</option>
  //         <option value="F">Female</option>
  //       </select>
  //       {errors.gender && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.gender.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* Weapon Dropdown */}
  //     <div>
  //       <label className="block font-medium mb-1">Weapon</label>
  //       <select
  //         {...register("weapon", { required: "Please fill this in!" })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.weapon ? "border-red-500" : "border-gray-300"
  //         }`}
  //       >
  //         <option value="">Select Weapon</option>
  //         <option value="F">Foil</option>
  //         <option value="E">Épée</option>
  //         <option value="S">Sabre</option>
  //       </select>
  //       {errors.weapon && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.weapon.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* Date */}
  //     <div>
  //       <label className="block font-medium mb-1">Date</label>
  //       <input
  //         type="date"
  //         {...register("date", {
  //           required: "Please fill this in!",
  //           validate: (value) => {
  //             const selectedDate = new Date(value);
  //             const tournamentStart = new Date(tournament.startDate);
  //             const tournamentEnd = new Date(tournament.endDate);
  //             return (
  //               (selectedDate >= tournamentStart &&
  //                 selectedDate <= tournamentEnd) ||
  //               "Event Date must within Tournament Time Frame!" +
  //                 tournamentStart +
  //                 tournamentEnd
  //             );
  //           },
  //         })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.date ? "border-red-500" : "border-gray-300"
  //         }`}
  //       />
  //       {errors.date && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.date.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* Start Time */}
  //     <div>
  //       <label className="block font-medium mb-1">Start Time</label>
  //       <input
  //         type="time"
  //         {...register("startTime", {
  //           required: "Please fill this in!",
  //         })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.startTime ? "border-red-500" : "border-gray-300"
  //         }`}
  //       />
  //       {errors.startTime && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.startTime.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* End Time */}
  //     <div>
  //       <label className="block font-medium mb-1">End Time</label>
  //       <input
  //         type="time"
  //         {...register("endTime", {
  //           required: "Please fill this in!",
  //           validate: (value) => {
  //             return (
  //               value > startTime ||
  //               "End Time must be after Start Time!"
  //             );
  //           },
  //         })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.endTime ? "border-red-500" : "border-gray-300"
  //         }`}
  //       />
  //       {errors.endTime && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.endTime.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* Minimum No. of Participants */}
  //     <div>
  //       <label className="block font-medium mb-1">
  //         Min. No. of Participants
  //       </label>
  //       <input
  //         type="number"
  //         {...register("minParticipants", {
  //           required: "Please fill this in!",
  //           validate: (value) =>
  //             value > 1 || "Please enter a number more than 1!",
  //         })}
  //         className={`w-full border rounded-md p-2 ${
  //           errors.minParticipants
  //             ? "border-red-500"
  //             : "border-gray-300"
  //         }`}
  //       />
  //       {errors.minParticipants && (
  //         <p className="text-red-500 text-sm italic">
  //           {errors.minParticipants.message}
  //         </p>
  //       )}
  //     </div>

  //     {/* Submit Button */}
  //     <div className="md:col-span-2">
  //       <button
  //         onSubmit={handleSubmit}
  //         className="w-full bg-blue-500 text-white rounded-md py-2"
  //       >
  //         Create Event
  //       </button>
  //     </div>
  //   </form>
  // </div>
  //       </div>
  //     </div>
  //   </div>
  // );

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
          Create New Event
        </h2>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
        >
          {/* Gender Dropdown */}
          <div>
            <label className="block font-medium mb-1">Gender</label>
            <select
              {...register("gender", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.gender ? "border-red-500" : "border-gray-300"
                }`}
            >
              <option value="">Select Gender</option>
              <option value="M">Male</option>
              <option value="F">Female</option>
            </select>
            {errors.gender && (
              <p className="text-red-500 text-sm italic">
                {errors.gender.message}
              </p>
            )}
          </div>

          {/* Weapon Dropdown */}
          <div>
            <label className="block font-medium mb-1">Weapon</label>
            <select
              {...register("weapon", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.weapon ? "border-red-500" : "border-gray-300"
                }`}
            >
              <option value="">Select Weapon</option>
              <option value="F">Foil</option>
              <option value="E">Épée</option>
              <option value="S">Sabre</option>
            </select>
            {errors.weapon && (
              <p className="text-red-500 text-sm italic">
                {errors.weapon.message}
              </p>
            )}
          </div>

          {/* Date */}
          <div>
            <label className="block font-medium mb-1">Date</label>
            <input
              type="date"
              {...register("date", {
                required: "Please fill this in!",
                validate: (value) => {
                  const selectedDate = new Date(value);
                  const tournamentStart = new Date(tournament.startDate);
                  const tournamentEnd = new Date(tournament.endDate);
                  return (
                    (selectedDate >= tournamentStart &&
                      selectedDate <= tournamentEnd) ||
                    "Event Date must within Tournament Time Frame!" +
                    tournamentStart +
                    tournamentEnd
                  );
                },
              })}
              className={`w-full border rounded-md p-2 ${errors.date ? "border-red-500" : "border-gray-300"
                }`}
            />
            {errors.date && (
              <p className="text-red-500 text-sm italic">
                {errors.date.message}
              </p>
            )}
          </div>

          {/* Start Time */}
          <div>
            <label className="block font-medium mb-1">Start Time</label>
            <input
              type="time"
              {...register("startTime", {
                required: "Please fill this in!",
              })}
              className={`w-full border rounded-md p-2 ${errors.startTime ? "border-red-500" : "border-gray-300"
                }`}
            />
            {errors.startTime && (
              <p className="text-red-500 text-sm italic">
                {errors.startTime.message}
              </p>
            )}
          </div>

          {/* End Time */}
          <div>
            <label className="block font-medium mb-1">End Time</label>
            <input
              type="time"
              {...register("endTime", {
                required: "Please fill this in!",
                validate: (value) => {
                  return (
                    value > startTime ||
                    "End Time must be after Start Time!"
                  );
                },
              })}
              className={`w-full border rounded-md p-2 ${errors.endTime ? "border-red-500" : "border-gray-300"
                }`}
            />
            {errors.endTime && (
              <p className="text-red-500 text-sm italic">
                {errors.endTime.message}
              </p>
            )}
          </div>

          {/* Minimum No. of Participants */}
          <div>
            <label className="block font-medium mb-1">
              Min. No. of Participants
            </label>
            <input
              type="number"
              {...register("minParticipants", {
                required: "Please fill this in!",
                validate: (value) =>
                  value > 1 || "Please enter a number more than 1!",
              })}
              className={`w-full border rounded-md p-2 ${errors.minParticipants
                ? "border-red-500"
                : "border-gray-300"
                }`}
            />
            {errors.minParticipants && (
              <p className="text-red-500 text-sm italic">
                {errors.minParticipants.message}
              </p>
            )}
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
  );
};

export default CreateEvent;
