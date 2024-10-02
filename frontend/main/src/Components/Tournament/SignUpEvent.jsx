import React, { useState } from 'react';
import Navbar from '../Navbar';
import Sidebar from '../Sidebar';
import logo from "../../Assets/logo.png";
import SubmitButton from '../Others/SubmitButton';

const SignUpEvent = () => {
    const [eventData, setEventData] = useState({
        eventName: '',
        gender: '',
        category: '',
        date: '',
        startTime: '',
        endTime: '',
        minParticipants: '',
        poulesElimination: '',
        description: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEventData({ ...eventData, [name]: value });
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
                <div className="flex flex-col items-center bg-white mt-32 rounded-lg shadow-lg w-[600px]">
                    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">

                        <img src={logo} alt="OnlyFence" className="h-12 mx-auto mb-4" />
                        <h2 className="text-2xl font-bold mb-6 text-center">Signing up for</h2>

                        <form className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">
                            {/* Event Name */}
                            <div>
                                <label className="block font-medium mb-1">Event Name</label>
                                <p>Super Tournament Event</p>
                            </div>

                            {/* Gender Dropdown */}
                            <div>
                                <label className="block font-medium mb-1">Gender</label>
                                <p>Male</p>
                            </div>

                            {/* Category Dropdown */}
                            <div>
                                <label className="block font-medium mb-1">Category</label>
                                <p>Sabre</p>
                            </div>

                            {/* Date */}
                            <div>
                                <label className="block font-medium mb-1">Date</label>
                                <p>2022-12-31</p>
                            </div>

                            {/* Start Time */}
                            <div>
                                <label className="block font-medium mb-1">Start Time</label>
                                <p>12:00</p>
                            </div>

                            {/* End Time */}
                            <div>
                                <label className="block font-medium mb-1">End Time</label>
                                <p>18:00</p>
                            </div>

                            {/* Minimum No. of Participants */}
                            <div>
                                <label className="block font-medium mb-1">Min. No. of Participants</label>
                                <p>16</p>
                            </div>

                            {/* poules Elimination % */}
                            <div>
                                <label className="block font-medium mb-1">poules Elimination (%)</label>
                                <p>50</p>
                            </div>

                            {/* Event Description */}
                            <div className="md:col-span-2">
                                <label className="block font-medium mb-1">Event Description</label>
                                <p>
                                    A super fun tournament event for all fencers to participate in. 
                                    Come and join us for a day of fun and excitement!
                                </p>
                            </div>

                            {/* Submit Button */}
                            <div className="md:col-span-2">
                                <SubmitButton>Confirm Sign Up</SubmitButton>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SignUpEvent;
