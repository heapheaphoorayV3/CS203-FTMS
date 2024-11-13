import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AdminService from "../../Services/Admin/AdminService";
import OrganiserService from "../../Services/Organiser/OrganiserService";
import FencerService from "../../Services/Fencer/FencerService";
import { Tabs, Tab } from "../Others/Tabs";
import PaginationButton from "../Others/PaginationButton";
import SearchBar from "../Others/SearchBar";
import LoadingPage from "../Others/LoadingPage";

const AdminDashboard = () => {
  const [userData, setUserData] = useState(null);
  const [allFencersData, setAllFencersData] = useState(null);
  const [allOrganisersData, setAllOrganisersData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [InputFencerSearch, setInputFencerSearch] = useState("");
  const [InputOrganiserSearch, setInputOrganiserSearch] = useState("");
  const [selectedWeapon, setSelectedWeapon] = useState("A");
  const [selectedGender, setSelectedGender] = useState("A");
  const [currentFencerPage, setCurrentFencerPage] = useState(1);
  const [currentOrganiserPage, setCurrentOrganiserPage] = useState(1);
  const [paginatedFencerData, setPaginatedFencerData] = useState([]);
  const [paginatedOrganiserData, setPaginatedOrganiserData] = useState([]);
  const [totalFencerPages, setTotalFencerPages] = useState(0);
  const [totalOrganiserPages, setTotalOrganiserPages] = useState(0);
  const limit = 10;

  useEffect(() => {
    const fetchData = async () => {
      try {
        console.log("Fetching user data...");
        const response = await AdminService.getProfile();
        setUserData(response.data);
      } catch (error) {
        console.error("Error fetching user data:", error);
        setError("Failed to load user data.");
      } finally {
        setLoading(false);
      }
    };

    const fetchAllFencersData = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getAllFencers();
        // console.log(response.data);
        setAllFencersData(response.data);
      } catch (error) {
        console.error("Error fetching all fencer data: ", error);
        setError("Failed to load all fencer data");
      } finally {
        setLoading(false);
      }
    };

    const fetchAllOrganisersData = async () => {
      setLoading(true);
      try {
        const response = await OrganiserService.getAllOrganisers();
        // console.log(response.data);
        setAllOrganisersData(response.data);
      } catch (error) {
        console.error("Error fetching all fencer data: ", error);
        setError("Failed to load all fencer data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
    fetchAllFencersData();
    fetchAllOrganisersData();
  }, []);

  const handleFencerPageChange = (page) => {
    setCurrentFencerPage(page);
  };

  const handleWeaponChange = (event) => {
    setSelectedWeapon(event.target.value);
  };

  const handleGenderChange = (event) => {
    setSelectedGender(event.target.value);
  };

  function handleFencerSearch(e) {
    setInputFencerSearch(e.target.value);
  }

  const handleOrganiserPageChange = (page) => {
    setCurrentOrganiserPage(page);
  };

  function handleOrganiserSearch(e) {
    setInputOrganiserSearch(e.target.value);
  }

  // Update Fencer page details based on search and pagination
  useEffect(() => {
    const getFilteredFencerData = () => {
      if (!allFencersData) return [];

      return allFencersData?.filter((fencer) => {
        const matchesSearch = fencer.name
          .toLowerCase()
          .includes(InputFencerSearch.toLowerCase());
        const matchesWeapon =
          selectedWeapon === "A" || fencer.weapon === selectedWeapon;
        const matchesGender =
          selectedGender === "A" || fencer.gender === selectedGender;
        return matchesSearch && matchesWeapon && matchesGender;
      });
    };

    const filteredFencerData = getFilteredFencerData();

    // Calculate fencer pagination details based on the filtered data
    setTotalFencerPages(Math.ceil(filteredFencerData.length / limit));
    setPaginatedFencerData(
      filteredFencerData.slice(
        (currentFencerPage - 1) * limit,
        currentFencerPage * limit
      )
    );

    // If any filter changes, reset to page 1
    if (currentFencerPage > totalFencerPages) {
      setCurrentFencerPage(1);
    }
  }, [
    allFencersData,
    currentFencerPage,
    totalFencerPages,
    InputFencerSearch,
    selectedWeapon,
    selectedGender,
  ]);

  // Update Organiser page details based on search and pagination
  useEffect(() => {
    const getFilteredOrganiserData = () => {
      if (!allOrganisersData) return [];

      return allOrganisersData?.filter((organiser) => {
        return organiser.name.toLowerCase().includes(InputOrganiserSearch.toLowerCase());
      }).map((organiser) => ({
        id: organiser.id,
        name: organiser.name,
        country: organiser.country,
        email: organiser.email,
      }));
    };

    const filteredOrganiserData = getFilteredOrganiserData();

    // Calculate organiser pagination details based on the filtered data
    setTotalOrganiserPages(Math.ceil(filteredOrganiserData.length / limit));
    setPaginatedOrganiserData(
      filteredOrganiserData.slice(
        (currentOrganiserPage - 1) * limit,
        currentOrganiserPage * limit
      )
    );

    // If any filter changes, reset to page 1
    if (currentOrganiserPage > totalOrganiserPages) {
      setCurrentOrganiserPage(1);
    }
  }, [
    allOrganisersData,
    currentOrganiserPage,
    totalOrganiserPages,
    InputOrganiserSearch,
  ]);

  if (loading || !userData) {
    return <LoadingPage />;
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  return (
    <div className="bg-white w-full h-full flex flex-col gap-2 p-8 overflow-auto">
      <div className="bg-white border rounded-2xl shadow-lg p-6 flex w-full relative overflow-x-hidden">
        <div className="w-full flex-shrink-0 flex flex-col my-4 ml-4">
          <div className="text-4xl font-semibold">
            Welcome back, {userData.name}
          </div>

          <div className="grid grid-cols-[2fr_8fr] gap-y-2 gap-x-4 my-4 text-xl w-full">
            {/* Email, ContactNo, Country, Verification Status */}
            <div className="flex font-medium">Email:</div>
            <div className="flex">{userData.email}</div>
            <div className="flex font-medium">Contact Number:</div>
            <div className="flex">{userData.contactNo}</div>
            <div className="flex font-medium">Country:</div>
            <div className="flex">{userData.country}</div>
          </div>
        </div>
      </div>

      <div className="bg-white border rounded-2xl shadow-lg p-6 flex flex-col flex-grow w-full relative mx-auto mt-4">
        <Tabs>
          <Tab label="List of Fencers">
            <div className="h-full px-4 pt-4">
              <div className="w-full max-w-sm min-w-[200px] ml-4 pb-8">
                <SearchBar
                  value={InputFencerSearch}
                  onChange={handleFencerSearch}
                  placeholder="Search Fencers by Name..."
                />
                <div className="grid grid-flow-col gap-4">
                  <div className="mt-4">
                    <label className="block font-medium mb-1 ml-1">
                      Select Gender
                    </label>
                    <select
                      value={selectedGender}
                      onChange={handleGenderChange}
                      className="block w-full py-2 px-3 border border-gray-300 rounded"
                    >
                      <option value="A">All</option>
                      <option value="M">Men's</option>
                      <option value="W">Women's</option>
                    </select>
                  </div>
                  <div className="mt-4">
                    <label className="block font-medium mb-1 ml-1">
                      Select Weapon
                    </label>
                    <select
                      value={selectedWeapon}
                      onChange={handleWeaponChange}
                      className="block w-full py-2 px-3 border border-gray-300 rounded"
                    >
                      <option value="A">All</option>
                      <option value="F">Foil</option>
                      <option value="E">Épée</option>
                      <option value="S">Sabre</option>
                    </select>
                  </div>
                </div>
              </div>

              <div className="ml-4 mr-4 mb-8 overflow-x-auto">
                {paginatedFencerData.length > 0 ? (
                  <table className="table text-lg border-collapse">
                    {/* head */}
                    <thead className="text-lg text-primary">
                      <tr className="border-b border-gray-300">
                        <th className="text-center w-20">ID</th>
                        <th className="w-1/2">Name</th>
                        <th className="text-center">Weapon</th>
                        <th className="text-center">Country</th>
                        <th className="text-center">Points</th>
                      </tr>
                    </thead>
                    <tbody>
                      {paginatedFencerData.map((item) => (
                        <tr
                          key={item.id}
                          className="border-b border-gray-300 hover:bg-gray-100"
                        >
                          <td className="text-center">{item.id}</td>
                          <td>{item.name}</td>
                          <td className="text-center">{item.weapon}</td>
                          <td className="text-center">{item.country}</td>
                          <td className="text-center">{item.points}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>) : (
                  <div className="flex justify-center items-center h-full">
                    <h2 className="text-lg font-medium">
                      No Fencer found
                    </h2>
                  </div>
                )}
                <div className="flex flex-col mt-2 justify-center items-center">
                  <PaginationButton
                    totalPages={totalFencerPages}
                    buttonSize="w-10 h-10"
                    currentPage={currentFencerPage}
                    onPageChange={handleFencerPageChange}
                  />
                </div>
              </div>
            </div>
          </Tab>
          <Tab label="List of Organisers">
            <div className="h-full px-4 pt-4">
              <div className="w-full px-4 pb-8">
                <div className="flex items-center justify-between">
                  <div className="min-w-[400px]">
                    <SearchBar
                      value={InputOrganiserSearch}
                      onChange={handleOrganiserSearch}
                      placeholder="Search Organisers by Name..."
                    />
                  </div>
                  <Link to="/verify-organisers" className="ml-4">
                    <button className="bg-blue-500 text-white px-4 py-2 rounded-md ml-auto">
                      Verify Pending Organisers
                    </button>
                  </Link>
                </div>
              </div>

              <div className="ml-4 mr-4 mb-8 overflow-x-auto">
                {paginatedOrganiserData.length > 0 ? (
                  <table className="table text-lg border-collapse">
                    {/* head */}
                    <thead className="text-lg text-primary">
                      <tr className="border-b border-gray-300">
                        <th className="text-center w-20">ID</th>
                        <th className="w-1/2">Name</th>
                        <th>Country</th>
                        <th>Email</th>
                      </tr>
                    </thead>
                    <tbody>
                      {paginatedOrganiserData.map((item) => (
                        <tr
                          key={item.id}
                          className="border-b border-gray-300 hover:bg-gray-100"
                        >
                          <td className="text-center">{item.id}</td>
                          <td>{item.name}</td>
                          <td>{item.country}</td>
                          <td>{item.email}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>) : (
                  <div className="flex justify-center items-center h-full">
                    <h2 className="mt-24 text-lg font-medium">
                      No Organiser found
                    </h2>
                  </div>
                )}
                <div className="flex flex-col mt-2 justify-center items-center">
                  <PaginationButton
                    totalPages={totalOrganiserPages}
                    buttonSize="w-10 h-10"
                    currentPage={currentOrganiserPage}
                    onPageChange={handleOrganiserPageChange}
                  />
                </div>
              </div>
            </div>
          </Tab>
        </Tabs>
      </div>
    </div >
  );
};

export default AdminDashboard;
