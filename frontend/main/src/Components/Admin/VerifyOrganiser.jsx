import React, { useEffect, useState } from "react";
import Pagination from "../Others/Pagination.jsx";

export default function VerifyOrganiser() {
    const [organisers, setOrganisers] = useState([]); // State to store fetched organisers
    const [currentPage, setCurrentPage] = useState(1); // State for current page
    const [totalPages, setTotalPages] = useState(1); // State for total pages
    const limit = 10; // Number of organisers per page

    // Store Array of Approved/Denied Organisers (to send Backend)
    const [checkboxState, setCheckboxState] = useState({}); // State to track checkboxes

    // Sample data for unverified organisers
    const allOrganisers = Array.from({ length: 50 }, (_, index) => ({
        id: index + 1,
        name: `Organisation ${index + 1}`,
        email: `organisation${index + 1}@example.com`,
    }));

    // Effect to update the organisers and total pages based on current page
    useEffect(() => {
        const startIndex = (currentPage - 1) * limit; // Calculate start index
        const endIndex = startIndex + limit; // Calculate end index
        setOrganisers(allOrganisers.slice(startIndex, endIndex)); // Set organisers for the current page
        setTotalPages(Math.ceil(allOrganisers.length / limit)); // Calculate total pages
    }, [currentPage]);

    // Handle page change from Pagination component
    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    // Function to handle checkbox changes
    const handleCheckboxChange = (id, type) => {
        setCheckboxState((prev) => {
            const newState = { ...prev };

            // If the organiser ID doesn't exist in the state, initialize it
            if (!newState[id]) {
                newState[id] = undefined; // Start with no selection
            }

            // Toggle checkbox state
            if (type === 'approve') {
                newState[id] = newState[id] === 'approved' ? undefined : 'approved'; // Toggle between approved and undefined
            } else if (type === 'deny') {
                newState[id] = newState[id] === 'denied' ? undefined : 'denied'; // Toggle between denied and undefined
            }

            // If both checkboxes are toggled, set to undefined
            if (newState[id] === 'approved' && prev[id] === 'denied') {
                newState[id] = 'approved'; // Approve overrides deny
            } else if (newState[id] === 'denied' && prev[id] === 'approved') {
                newState[id] = 'denied'; // Deny overrides approve
            } else if (newState[id] === undefined) {
                delete newState[id]; // Remove entry if undefined (no selection)
            }

            // Log the current state of all checkboxes
            console.log('Current Checkbox States:', newState);

            return newState;
        });

        console.log(`Organiser ID: ${id} - Action: ${type}`);
    };

    return (
        <div className="flex flex-col justify-center items-center gap-10 p-8">
            <h1 className="text-4xl font-bold">Verify Organisers</h1>
            <table className="table text-lg">
                <thead>
                    <tr className="text-lg">
                        <th>Organisation</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {organisers.map((organiser) => (
                        <tr key={organiser.id}>
                            <td>{organiser.name}</td>
                            <td>{organiser.email}</td>
                            <td className="flex gap-4">
                                <label className="flex items-center">
                                    <input
                                        type="checkbox"
                                        checked={checkboxState[organiser.id] === 'approved'} // Control checkbox state
                                        onChange={() => handleCheckboxChange(organiser.id, 'approve')}
                                    />
                                    <span className="ml-2">Approve</span>
                                </label>
                                <label className="flex items-center">
                                    <input
                                        type="checkbox"
                                        checked={checkboxState[organiser.id] === 'denied'} // Control checkbox state
                                        onChange={() => handleCheckboxChange(organiser.id, 'deny')}
                                    />
                                    <span className="ml-2">Deny</span>
                                </label>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <Pagination totalPages={totalPages} buttonSize="w-10 h-10" onPageChange={handlePageChange} />
        </div>
    );
}
