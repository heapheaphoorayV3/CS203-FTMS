import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { DateTime } from "luxon";
import EventService from "../../Services/Event/EventService";
import { XCircleIcon } from "@heroicons/react/16/solid";

const UpdateEvent = ({ tournamentDates, selectedEvent, onClose, fetchTournamentData }) => {
  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm();

  useEffect(() => {
    if (selectedEvent) {
      console.log("Selected Event:", selectedEvent);
      // Prefill form with the event details
      setValue("startTime", selectedEvent.startTime);
      setValue("endTime", selectedEvent.endTime);
      setValue("gender", selectedEvent.gender);
      setValue("minParticipants", selectedEvent.minParticipants);
      setValue("date", selectedEvent.date);
      setValue("weapon", selectedEvent.weapon);
    }
  }, [selectedEvent, setValue]);

  function getGender(gender) {
    if (gender === "M") {
      return "Male";
    } 
    return "Female";
  }

  function getWeapon(weapon) {
    if (weapon === "F") {
      return "Foil";
    } else if (weapon === "E") {
      return "Epee";
    }
    return "Sabre";
  }

  const onSubmit = async (data) => {
    // Create DateTime objects and format time to include seconds
    let startTimeString = data.startTime;
    let endTimeString = data.endTime;
    if (data.startTime.length === 5) {
      startTimeString += ":00";
    }
    if (data.endTime.length === 5) {
      endTimeString += ":00";
    }

    const startTime = DateTime.fromFormat(startTimeString, "HH:mm:ss");
    const endTime = DateTime.fromFormat(endTimeString, "HH:mm:ss");

    const formattedStartTime = startTime.toFormat("HH:mm:ss");
    const formattedEndTime = endTime.toFormat("HH:mm:ss");

    // Prepare formData to be sent to the API
    const formData = {
      ...data,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
    };

    try {
      console.log("Updating event with data:", formData);
      await EventService.updateEvent(selectedEvent.id, formData); // Call the update method
      onClose();
      fetchTournamentData(); 
    } catch (error) {
      console.error("Error updating event:", error);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white rounded-lg w-1/3 min-w-[300px] flex flex-col justify-center pt-6 py-12 lg:px-8">
        {/* Close Button --> ml-auto pushes button to the right of the form */}
        <button
          onClick={onClose}
          className="ml-auto w-5 text-gray-300 hover:text-gray-800 focus:outline-none"
          aria-label="Close"
        >
          <XCircleIcon /> {/* This is the close icon (×) */}
        </button>

        <h2 className="text-2xl font-bold mb-6 text-center">Update Event</h2>

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4"
        >
          {/* Gender Dropdown */}
          <div>
            <label className="block font-medium mb-1">Gender</label>
            <h1 className="font-medium">{getGender(selectedEvent.gender)}</h1>
          </div>

          {/* Weapon Dropdown */}
          <div>
            <label className="block font-medium mb-1">Weapon</label>
            <h1 className="font-medium">{getWeapon(selectedEvent.weapon)}</h1>
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
                  const tournamentStart = new Date(tournamentDates[0]);
                  const tournamentEnd = new Date(tournamentDates[1]);
                  return (
                    (selectedDate >= tournamentStart &&
                      selectedDate <= tournamentEnd) ||
                    "Event Date must be within Tournament Time Frame!"
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
              className={`w-full border rounded-md p-2 ${
                errors.startTime ? "border-red-500" : "border-gray-300"
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
                  // Check if selectedEvent is available and startTime is defined
                  if (!selectedEvent || !selectedEvent.startTime) {
                    return true; // No validation if selectedEvent is not set
                  }
                  // Validate that endTime is after startTime
                  return (
                    value > selectedEvent.startTime ||
                    "End Time must be after Start Time!"
                  );
                },
              })}
              className={`w-full border rounded-md p-2 ${
                errors.endTime ? "border-red-500" : "border-gray-300"
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
                  value >= 8 || "Please enter a number greater than or equal to 8!",
              })}
              className={`w-full border rounded-md p-2 ${
                errors.minParticipants ? "border-red-500" : "border-gray-300"
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
              Update Event
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateEvent;
