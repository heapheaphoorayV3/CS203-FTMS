import { useState, useEffect } from "react";
import PaginationButton from "./Others/Pagination";

export default function InternationalRanking() {
  const [rankingData, setRankingData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 10;

  const testData = Array.from({ length: 20 }, (_, index) => ({
    id: index + 1,
    name: "Name",
    country: "SG",
    score: 0,
  }));

  const [paginatedData, setPaginatedData] = useState([]);
  useEffect(() => {
    const startIndex = (currentPage - 1) * limit;
    const endIndex = startIndex + limit;
    const paginatedData = testData.slice(startIndex, endIndex);
    setPaginatedData(paginatedData); // Set paginated data for the current page
  }, [currentPage]);
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };
  const totalPages = Math.ceil(testData.length / limit);

  //   if (loading) {
  //     return <div className="mt-10">Loading...</div>; // Show loading state
  //   }

  //   if (error) {
  //     return <div className="mt-10">{error}</div>; // Show error message if any
  //   }

  return (
    <div className="row-span-2 col-start-2 bg-gray-200 h-full overflow-y-auto">
      <h1 className="my-10 ml-12 text-left text-4xl font-semibold">
        International Ranking
      </h1>
      <h1 className="my-10 ml-12 text-left text-2xl font-semibold">
        Search bar/filter etc
      </h1>

      <div className="ml-12 mr-8 mb-8 overflow-x-auto">
        <table className="table text-lg">
          {/* head */}
          <thead className="text-lg text-neutral">
            <tr>
              <th className="text-center w-20">Rank</th>
              <th className="w-1/2">Name</th>
              <th className="text-center">Country</th>
              <th className="text-center">Points</th>
            </tr>
          </thead>
          <tbody>
            {paginatedData.map((item) => (
              <tr key={item.id}>
                <td className="text-center">{item.id}</td>
                <td>{item.name}</td>
                <td className="text-center">{item.country}</td>
                <td className="text-center">{item.score}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="flex flex-col justify-center items-center">
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
