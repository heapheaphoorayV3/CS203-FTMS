import React, { useEffect, useState } from "react";
import Pagination from "../Others/PaginationButton.jsx";
import AdminService from "../../Services/Admin/AdminService.js";
import LoadingPage from "../Others/LoadingPage.jsx";

export default function VerifyOrganisers() {
  const [organisers, setOrganisers] = useState([]); // State to store fetched organisers
  const [currentPage, setCurrentPage] = useState(1); // State for current page
  const [totalPages, setTotalPages] = useState(1); // State for total pages
  const limit = 8; // Number of organisers per page
  const [loading, setLoading] = useState(true); // State for loading state
  const [error, setError] = useState(null); // State for error handling
  const [submitError, setSubmitError] = useState(null); // State for error handling
  const [checkboxState, setCheckboxState] = useState({}); // State to track checkboxes --> Array of Approved/Denied Organisers (to send Backend)

  // Function to fetch organisers
  const fetchOrganisers = async () => {
    try {
      const response = await AdminService.getUnverifiedOrganisers();
      setOrganisers(response.data);
      setTotalPages(Math.ceil(organisers.length / limit));
    } catch (error) {
      if (error.response) {
        setError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        setError(
          "Failed to fetch unverified organisers, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        setError(
          "Failed to fetch unverified organisers, please try again later."
        );
      }
    }
  };

  // Get unverified organisers (only on first load)
  useEffect(() => {
    setLoading(true);
    fetchOrganisers().then(() => {setLoading(false);});
  }, []);

  // Effect to update the organisers and total pages based on current page
  useEffect(() => {
    const startIndex = (currentPage - 1) * limit; // Calculate start index
    const endIndex = startIndex + limit; // Calculate end index
    setOrganisers(organisers.slice(startIndex, endIndex)); // Set organisers for the current page
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
      if (type === "A") {
        newState[id] = newState[id] === "A" ? undefined : "A"; // Toggle between approved and undefined
      } else if (type === "D") {
        newState[id] = newState[id] === "D" ? undefined : "D"; // Toggle between denied and undefined
      }

      // If both checkboxes are toggled, set to newState
      // If checkbox untoggled, set to undefined
      if (newState[id] === "A" && prev[id] === "D") {
        newState[id] = "A"; // Approve overrides deny
      } else if (newState[id] === "D" && prev[id] === "A") {
        newState[id] = "D"; // Deny overrides approve
      } else if (newState[id] === undefined) {
        delete newState[id]; // Remove entry if undefined (no selection)
      }

      return newState;
    });
  };

  // Categorise status of organiser
  function categoriseStatus(checkboxState) {
    let approve = [];
    let deny = [];

    for (let key in checkboxState) {
      if (checkboxState.hasOwnProperty(key)) {
        // Check if the key is part of the object and not from its prototype
        if (checkboxState[key] === "A") {
          approve.push(key);
        } else if (checkboxState[key] === "D") {
          deny.push(key);
        }
      }
    }

    return { approve, deny };
  }

  // Function to submit verifications
  const submitVerfications = async () => {
    //Convert checkboxState to array of objects
    const data = categoriseStatus(checkboxState);
    try {
      await AdminService.verifyOrganiser(data);
      setCheckboxState({});
      fetchOrganisers();
    } catch (error) {
      if (error.response) {
        setSubmitError(error.response.data);
      } else if (error.request) {
        // The request was made but no response was received
        setSubmitError(
          "Failed to submit verifications, please try again later."
        );
      } else {
        // Something happened in setting up the request that triggered an Error
        setSubmitError(
          "Failed to submit verifications, please try again later."
        );
      }
    }
  };
  // Loading / Error states
  if (loading) {
    return <LoadingPage />;
  }
  if (error) {
    return (
      <div className="flex justify-between mr-20 my-10">
        <h1 className=" ml-12 text-left text-2xl font-semibold">{error}</h1>
      </div>
    ); // Show error message if any
  }

  return (
    <div className="flex flex-col bg-white items-center h-full gap-10 p-8">
      <h1 className="text-4xl font-bold mt-4">Verify Organisers</h1>
      {organisers.length > 0 ? (
        <table className="table text-lg">
          <thead>
            <tr className="text-lg text-primary border-b border-gray-300">
              <th>Organisation</th>
              <th>Email</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {organisers.map((organiser) => (
              <tr key={organiser.id} className="border-b border-gray-300">
                <td>{organiser.name}</td>
                <td>{organiser.email}</td>
                <td className="flex gap-4">
                  <label className="flex items-center">
                    <input
                      type="checkbox"
                      checked={checkboxState[organiser.id] === "A"}
                      onChange={() => handleCheckboxChange(organiser.id, "A")}
                    />
                    <span className="ml-2">Approve</span>
                  </label>
                  <label className="flex items-center">
                    <input
                      type="checkbox"
                      checked={checkboxState[organiser.id] === "D"}
                      onChange={() => handleCheckboxChange(organiser.id, "D")}
                    />
                    <span className="ml-2">Deny</span>
                  </label>
                </td>
              </tr>
            ))}

            {/* Add empty rows if there are less than 8 rows */}
            {Array.from({ length: limit - organisers.length }).map(
              (_, index) => (
                <tr key={`empty-${index}`} className="border-transparent">
                  <td>&nbsp;</td> {/* Empty cells */}
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
              )
            )}
          </tbody>
        </table>
      ) : (
        <h1 className="mt-16 text-xl font-semibold text-center text-black">
          No unverified organisers yet
        </h1>
      )}
      {Object.keys(checkboxState).length > 0 && (
        <button
          onClick={submitVerfications}
          className="bg-green-400 text-white px-4 py-2 rounded"
        >
          Confirm Changes
        </button>
      )}
      {submitError && (
        <h1 className="text-red-500 text-center mt-4">{submitError}</h1>
      )}
      <Pagination
        totalPages={totalPages}
        buttonSize="w-10 h-10"
        currentPage={currentPage}
        onPageChange={handlePageChange}
      />
    </div>
  );
}
