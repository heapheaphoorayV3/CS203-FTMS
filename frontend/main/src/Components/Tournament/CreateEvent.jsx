import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { XCircleIcon } from "@heroicons/react/16/solid";

const CreateEvent = ({ eventTypes, tournamentDates, onClose, onSubmit }) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const startTime = watch("startTime");

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

          {/* EventType Dropdown */}
          <div>
            <label className="block font-medium mb-1">Event Type</label>
            <select
              {...register("eventName", { required: "Please fill this in!" })}
              className={`w-full border rounded-md p-2 ${errors.eventName ? "border-red-500" : "border-gray-300"
                }`}
            >
              {eventTypes.map((eventType) => (
                <option key={eventType.value} value={eventType.value}>
                  {eventType.label}
                </option>
              ))}
            </select>
            {errors.eventName && (
              <p className="text-red-500 text-sm italic">
                {errors.eventName.message}
              </p>
            )}
          </div>

          {/* Date */}
          <div>
            <label className="block font-medium mb-1">Date</label>
            <input
              type="date"
              {...register("eventDate", {
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
              className={`w-full border rounded-md p-2 ${errors.eventDate ? "border-red-500" : "border-gray-300"
                }`}
            />
            {errors.eventDate && (
              <p className="text-red-500 text-sm italic">
                {errors.eventDate.message}
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
                  value >= 8 || "Must have at least 8 participants!",
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
              onSubmit={handleSubmit()}
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
