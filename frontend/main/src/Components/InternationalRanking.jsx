import { useState, useEffect } from "react";
import PaginationButton from "./Others/Pagination";
import FencerService from "../Services/Fencer/FencerService";

export default function InternationalRanking() {
  const [rankingData, setRankingData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [InputSearch, setInputSearch] = useState("");
  const [selectedWeapon, setSelectedWeapon] = useState("F");
  const [selectedGender, setSelectedGender] = useState("M");
  const [currentPage, setCurrentPage] = useState(1);
  const [paginatedData, setPaginatedData] = useState([]);
  const limit = 10;

  useEffect(() => {
    const fetchInternationalRanking = async () => {
      setLoading(true);
      try {
        const response = await FencerService.getInternationalRanking();
        setRankingData(response.data);
      } catch (error) {
        console.error("Error fetching international ranking: ", error);
        setError("Failed to load international ranking");
      } finally {
        setLoading(false);
      }
    };

    fetchInternationalRanking();
  }, []);

  useEffect(() => {
    if (Array.isArray(rankingData) && rankingData.length) {
      const sortedRanking = [...rankingData].sort(
        (a, b) => b.points - a.points
      );

      const startIndex = Math.max(0, (currentPage - 1) * limit);
      const endIndex = Math.min(sortedRanking.length, startIndex + limit);
      setPaginatedData(sortedRanking.slice(startIndex, endIndex));
    } else {
      setPaginatedData([]);
    }
  }, [rankingData, currentPage, limit]);

  console.log(rankingData);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };
  const totalPages = Math.ceil(paginatedData.length / limit);

  const handleWeaponChange = (event) => {
    setSelectedWeapon(event.target.value);
  };

  const handleGenderChange = (event) => {
    setSelectedGender(event.target.value);
  };

  function handleSearch(e) {
    setInputSearch(e.target.value);
  }

  const filteredFencerData = paginatedData?.filter((fencer) => {
    return (
      fencer.name.toLowerCase().includes(InputSearch.toLowerCase()) &&
      (selectedGender ? fencer.gender === selectedGender : true) &&  
      (selectedWeapon ? fencer.weapon === selectedWeapon : true) 
    );
  });

  console.log("==========");
  console.log(filteredFencerData);

  if (loading) {
    return <div className="mt-10">Loading...</div>; // Show loading state
  }

  if (error) {
    return <div className="mt-10">{error}</div>; // Show error message if any
  }

  return (
    <div className="row-span-2 col-start-2 bg-white h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        International Ranking
      </h1>
      <div className="w-full max-w-sm min-w-[200px] ml-12 pb-8">
        <div className="relative">
          <input
            className="w-full bg-slate-50 placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md pl-3 pr-28 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
            placeholder="Search Fencers by Name..."
            onChange={handleSearch}
          />
          <div className="absolute top-1 right-1 flex items-center pt-1.5 px-1">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              class="w-4 h-4 mr-2"
            >
              <path
                fill-rule="evenodd"
                d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
        </div>
        <div className="grid grid-flow-col gap-4">
          <div className="mt-4">
            <label className="block font-medium mb-1 ml-1">Select Gender</label>
            <select
              value={selectedGender}
              onChange={handleGenderChange}
              className="block w-full py-2 px-3 border border-gray-300 rounded"
            >
              <option value="M">Men's</option>
              <option value="W">Women's</option>
            </select>
          </div>
          <div className="mt-4">
            <label className="block font-medium mb-1 ml-1">Select Weapon</label>
            <select
              value={selectedWeapon}
              onChange={handleWeaponChange}
              className="block w-full py-2 px-3 border border-gray-300 rounded"
            >
              <option value="F">Foil</option>
              <option value="E">Épée</option>
              <option value="S">Sabre</option>
            </select>
          </div>
        </div>
      </div>

      <div className="ml-12 mr-8 mb-8 overflow-x-auto">
        <table className="table text-lg border-collapse">
          {/* head */}
          <thead className="text-lg text-neutral">
            <tr className="border-b border-gray-300">
              <th className="text-center w-20">Rank</th>
              <th className="w-1/2">Name</th>
              <th className="text-center">Country</th>
              <th className="text-center">Points</th>
            </tr>
          </thead>
          <tbody>
            {filteredFencerData.map((item, index) => (
              <tr
                key={item.id}
                className="border-b border-gray-300 hover:bg-gray-100"
              >
                <td className="text-center">{index + 1}</td>
                <td>{item.name}</td>
                <td className="text-center">{item.country}</td>
                <td className="text-center">{item.points}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="flex flex-col mt-2 justify-center items-center">
          <PaginationButton
            totalPages={totalPages}
            buttonSize="w-10 h-10"
            currentPage={currentPage}
            onPageChange={handlePageChange}
          />
        </div>
      </div>
    </div>
  );
}